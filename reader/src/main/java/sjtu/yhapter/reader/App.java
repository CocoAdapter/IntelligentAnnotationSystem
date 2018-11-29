package sjtu.yhapter.reader;

import android.app.Application;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class App extends Application {
    private static volatile App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

    public static App getInstance() {
        return instance;
    }
}
