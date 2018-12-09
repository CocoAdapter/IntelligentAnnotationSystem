package sjtu.yhapter.ias.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import sjtu.yhapter.ias.App;

public class NetworkUtils {

    public static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) App
                .getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm == null ? null : cm.getActiveNetworkInfo();
    }

    public static boolean isAvailable() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isAvailable();
    }
}
