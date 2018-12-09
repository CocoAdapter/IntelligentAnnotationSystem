package sjtu.yhapter.ias.util;

import android.os.Environment;

import sjtu.yhapter.ias.App;

public class FileUtils {

    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    @SuppressWarnings("all")
    public static String getCachePath() {
        if (isSDCardAvailable())
            return App.getInstance().getExternalCacheDir().getAbsolutePath();
        else
            return App.getInstance().getCacheDir().getAbsolutePath();
    }
}
