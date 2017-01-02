package co.nilin.tosanboomsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.text.DecimalFormat;

import co.nilin.tosanboomsample.model.Deposit;
import co.nilin.tosanboomsample.util.AppUrls;
import co.nilin.tosanboomsample.widget.MyMediumTextView;
import co.nilin.tosanboomsample.widget.MyTextView;

/**
 * Created by data on 1/2/2017.
 */

public class DepositsListActivity extends MyActivity {
    public static final String CUSTOMER_TITLE = "CustomerTitle";
    public static final String CUSTOMER_NAME = "CustomerName";

    private DepositsListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposits_list);
        ((MyMediumTextView) findViewById(R.id.toolbar_title)).setText(String.format("%s %s",
                getString(R.string.deposits_of),
                getIntent().getStringExtra(CUSTOMER_NAME)));

        initUI();

        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                fetchDeposits();
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                fetchDeposits();
            }
        });

        ((ListView) findViewById(android.R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deposit item = mAdapter.getItem(position);
                startActivity(new Intent(DepositsListActivity.this, DepositStatementsActivity.class)
                        .putExtra(DepositStatementsActivity.DEPOSIT_NUMBER, item.getNumber()));
            }
        });
    }

    private void initUI() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);

        mAdapter = new DepositsListAdapter(this);
        ((ListView) findViewById(android.R.id.list)).setAdapter(mAdapter);
    }

    private void fetchDeposits() {
        mSwipeLayout.setRefreshing(true);

        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MarketToken", null);
        String session = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("BankToken", null);
        Ion.with(this)
                .load(String.format(AppUrls.DEPOSITS_LIST_URL, "10", "0"))
                .addHeader("Accept-Language", "fa-IR")
                .addHeader("Device-Id", "3082a1dfdbbaf34a")
                .addHeader("App-Key", "12395")
                .addHeader("Content-Type", "application/json")
                .addHeader("Bank-Id", "ANSBIR")
                .addHeader("Sandbox", "false")
                .addHeader("Token-Id", token)
                .addHeader("Session", session)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        mSwipeLayout.setRefreshing(false);

                        if (e == null) {
                            JsonObject response = result.getResult();
                            if (result.getHeaders().code() == 200) {
                                JsonArray deposits = response.get("deposits").getAsJsonArray();
                                for (JsonElement d : deposits)
                                    mAdapter.add(Deposit.toDeposit(d.getAsJsonObject()));
                            } else
                                Toast.makeText(DepositsListActivity.this, String.format("%s: %s", response.get("code").getAsString(), response.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(DepositsListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class DepositsListAdapter extends ArrayAdapter<Deposit> {
        private LayoutInflater mmInflater;
        private DecimalFormat mmFormatter = new DecimalFormat("###,###");

        public DepositsListAdapter(Context context) {
            super(context, 0);
            this.mmInflater = LayoutInflater.from(DepositsListActivity.this);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mmInflater.inflate(R.layout.item_deposit, parent, false);

            final Deposit item = getItem(position);
            ((MyMediumTextView) convertView.findViewById(R.id.tvDepositNumber)).setText(Html.fromHtml(String.format("<font color=#aaaaaa>%s:</font> %s", getString(R.string.deposit_number), item.getNumber())));
            ((MyTextView) convertView.findViewById(R.id.tvIBAN)).setText(Html.fromHtml(String.format("<font color=#aaaaaa>%s:</font> %s", getString(R.string.iban), item.getIBAN())));
            ((MyTextView) convertView.findViewById(R.id.tvBalance)).setText(Html.fromHtml(String.format("<font color=#aaaaaa>%s:</font> %s %s", getString(R.string.balance), mmFormatter.format(item.getBalance()), getString(R.string.rials))));
            ((MyTextView) convertView.findViewById(R.id.tvAvailableBalance)).setText(Html.fromHtml(String.format("<font color=#aaaaaa>%s:</font> %s %s", getString(R.string.available_balance), mmFormatter.format(item.getAvailableBalance()), getString(R.string.rials))));

            return convertView;
        }
    }
}
