package sjtu.yhapter.reader;

import android.app.Application;

import sjtu.yhapter.common.IModuleApplication;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class App extends Application implements IModuleApplication {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void init(Application application) {
        instance = application;
    }
}
