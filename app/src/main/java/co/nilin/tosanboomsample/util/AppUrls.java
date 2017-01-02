package co.nilin.tosanboomsample.util;

/**
 * Created by data on 1/2/2017.
 */

public class AppUrls {
    private static final String API_BASE_URL = "https://app.tosanboom.com:4432/v1/";

    public static final String MARKET_LOGIN_URL = API_BASE_URL + "auth/market/login/";
    public static final String BANK_LOGIN_URL = API_BASE_URL + "auth/login/";
    public static final String DEPOSITS_LIST_URL = API_BASE_URL + "deposits?length=%s&offset=%s";
    public static final String DEPOSIT_STATEMENTS_URL = API_BASE_URL + "deposits/%s/statements?from_date=%s&to_date=%s&length=%s&offset=%s";
}
