package co.nilin.tosanboomsample;

import android.app.Application;
import android.util.Log;

import com.koushikdutta.ion.Ion;

/**
 * Created by data on 1/2/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Ion.getDefault(this).configure().setLogging("MyLogs", Log.VERBOSE);
        Ion.getDefault(this).configure().proxy("192.168.0.114", 8888);
    }
}
