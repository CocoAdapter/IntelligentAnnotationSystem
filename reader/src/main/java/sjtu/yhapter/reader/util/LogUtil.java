package sjtu.yhapter.reader.util;

import android.util.Log;

/**
 * Created by CocoAdapter on 2018/11/12.
 */

public class LogUtil {
    private static final String NAME = "IAS";

    public static<T> void log(T name, String value) {
        Log.e(name.getClass().getSimpleName(), value);
    }

    public static void log(String value) {
        Log.e(NAME, value);
    }
}
