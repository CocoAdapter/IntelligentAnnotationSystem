package sjtu.yhapter.reader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import sjtu.yhapter.common.IModuleApplication;
import sjtu.yhapter.reader.model.dao.DaoMaster;
import sjtu.yhapter.reader.model.dao.DaoSession;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class App extends Application implements IModuleApplication {
    private static Application instance;

    private static final String DB_NAME = "sjtu.yhapter.reader";
    private static DaoSession daoSession;

    public static long USER_ID;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void init(Application application) {
        instance = application;
        setupDatabase();
    }

    private void setupDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                instance, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant(){
        return daoSession;
    }
}
