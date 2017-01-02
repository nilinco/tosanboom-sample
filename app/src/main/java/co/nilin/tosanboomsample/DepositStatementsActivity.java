package co.nilin.tosanboomsample;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.nilin.tosanboomsample.model.Statement;
import co.nilin.tosanboomsample.util.AppUrls;
import co.nilin.tosanboomsample.util.DateUtils;
import co.nilin.tosanboomsample.widget.MyMediumTextView;
import co.nilin.tosanboomsample.widget.MyTextView;

/**
 * Created by data on 1/2/2017.
 */

public class DepositStatementsActivity extends MyActivity {
    public static final String DEPOSIT_NUMBER = "DepositNumber";

    private SwipeRefreshLayout mSwipeLayout;
    private DepositStatementsListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_statements);
        ((MyMediumTextView) findViewById(R.id.toolbar_title)).setText(getIntent().getStringExtra(DEPOSIT_NUMBER));

        initUI();

        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                fetchStatements(getIntent().getStringExtra(DEPOSIT_NUMBER));
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                fetchStatements(getIntent().getStringExtra(DEPOSIT_NUMBER));
            }
        });
    }

    private void initUI() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);

        mAdapter = new DepositStatementsListAdapter(this);
        ((ListView) findViewById(android.R.id.list)).setAdapter(mAdapter);
    }

    private void fetchStatements(String depositNumber) {
        mSwipeLayout.setRefreshing(true);

        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MarketToken", null);
        String session = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("BankToken", null);
        Ion.with(this)
                .load(String.format(AppUrls.DEPOSIT_STATEMENTS_URL, depositNumber, "2016-01-01T00:00:00Z", "2017-01-03T23:59:59Z", "50", "0"))
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
                                JsonArray statements = response.get("statemens").getAsJsonArray();
                                for (JsonElement s : statements)
                                    mAdapter.add(Statement.toStatement(s.getAsJsonObject()));
                            } else
                                Toast.makeText(DepositStatementsActivity.this, String.format("%s: %s", response.get("code").getAsString(), response.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(DepositStatementsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class DepositStatementsListAdapter extends ArrayAdapter<Statement> {
        private LayoutInflater mmInflater;
        private DecimalFormat mmFormatter = new DecimalFormat("###,###");
        private DateFormat mmDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        public DepositStatementsListAdapter(Context context) {
            super(context, 0);
            this.mmInflater = LayoutInflater.from(DepositStatementsActivity.this);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mmInflater.inflate(R.layout.item_statement, parent, false);

            Statement item = getItem(position);
            ((MyMediumTextView) convertView.findViewById(R.id.tvAmount)).setCompoundDrawablesWithIntrinsicBounds(0, 0, item.getTransferAmount() >= 0 ? R.drawable.ic_increase : R.drawable.ic_decrease, 0);
            ((MyMediumTextView) convertView.findViewById(R.id.tvAmount)).setTextColor(ContextCompat.getColor(DepositStatementsActivity.this, item.getTransferAmount() >= 0 ? R.color.material_green_500 : R.color.material_red_500));
            ((MyMediumTextView) convertView.findViewById(R.id.tvAmount)).setText(String.format("%s %s", mmFormatter.format(Math.abs(item.getTransferAmount())), getString(R.string.rials)));
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mmDateFormatter.parse(item.getDate()));
                String date = DateUtils.getJalaliDate(cal).toStringWithTimeStandard();
                ((MyTextView) convertView.findViewById(R.id.tvDate)).setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((MyTextView) convertView.findViewById(R.id.tvDescription)).setText(item.getDescription());
            ((MyTextView) convertView.findViewById(R.id.tvReferenceNumber)).setText(Html.fromHtml(String.format("<font color=#aaaaaa>%s:</font> %s", getString(R.string.reference_number), item.getReferenceNumber())));

            return convertView;
        }
    }
}
