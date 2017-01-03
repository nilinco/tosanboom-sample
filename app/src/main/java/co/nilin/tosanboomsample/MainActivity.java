package co.nilin.tosanboomsample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import co.nilin.tosanboomsample.util.AppUrls;
import co.nilin.tosanboomsample.widget.MyMediumTextView;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyMediumTextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.login));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        loginToMarket();

        findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToMarket();
            }
        });
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                loginToBank(username, password);
            }
        });
        ((EditText) findViewById(R.id.etPassword)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
                    String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                    loginToBank(username, password);
                    return true;
                }
                return false;
            }
        });
    }

    private void loginToMarket() {
        findViewById(R.id.btnRetry).setVisibility(View.GONE);
        findViewById(R.id.loginContent).setVisibility(View.GONE);
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);

        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("username", MyApplication.TOSAN_BOOM_USERNAME);
        loginRequest.addProperty("password", MyApplication.TOSAN_BOOM_PASSWORD);

        Ion.with(this)
                .load(AppUrls.MARKET_LOGIN_URL)
                .addHeader("Accept-Language", "fa-IR")
                .addHeader("Device-Id", "3082a1dfdbbaf34a")
                .addHeader("App-Key", "12395")
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(loginRequest)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        findViewById(android.R.id.progress).setVisibility(View.GONE);

                        if (e == null) {
                            if (result.getHeaders().code() == 200) {
                                findViewById(R.id.loginContent).setVisibility(View.VISIBLE);
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MarketToken", result.getResult().get("token").getAsString()).apply();
                            } else {
                                findViewById(R.id.btnRetry).setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, String.format("%s: %s", result.getResult().get("code").getAsString(), result.getResult().get("message").getAsString()), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            findViewById(R.id.btnRetry).setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginToBank(String username, String password) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage(getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("username", username);
        loginRequest.addProperty("password", password);

        String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MarketToken", null);
        Ion.with(this)
                .load(AppUrls.BANK_LOGIN_URL)
                .addHeader("Accept-Language", "fa-IR")
                .addHeader("Device-Id", "3082a1dfdbbaf34a")
                .addHeader("App-Key", "12395")
                .addHeader("Content-Type", "application/json")
                .addHeader("Bank-Id", "ANSBIR")
                .addHeader("Sandbox", "false")
                .addHeader("Token-Id", token)
                .setJsonObjectBody(loginRequest)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        progress.dismiss();

                        if (e == null) {
                            JsonObject response = result.getResult();
                            if (result.getHeaders().code() == 200) {
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("BankToken", response.get("session_id").getAsString()).apply();
                                startActivity(new Intent(MainActivity.this, DepositsListActivity.class)
                                        .putExtra(DepositsListActivity.CUSTOMER_TITLE, response.get("title").getAsString())
                                        .putExtra(DepositsListActivity.CUSTOMER_NAME, response.get("name").getAsString()));
                            } else
                                Toast.makeText(MainActivity.this, String.format("%s: %s", response.get("code").getAsString(), response.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
