package sjtu.yhapter.ias;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import sjtu.yhapter.common.IModuleApplication;
import sjtu.yhapter.ias.model.dao.DaoMaster;
import sjtu.yhapter.ias.model.dao.DaoSession;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.reader.util.LogUtil;

public class App extends Application {
    private static final String[] MODULES = {"sjtu.yhapter.reader.App"};

    private static final String DB_NAME = "sjtu.yhapter.ias";
    private static DaoSession daoSession;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        setupDatabase();

        modulesApplicationInit();

        // TODO test
        Student student = new Student();
        student.setId(1L);
        student.setName("何昊西");
        student.setPassword("mimashi123");
        student.setStudentId("123456789");

        getDaoInstant().getStudentDao().save(student);
    }

    private void setupDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                this, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private void modulesApplicationInit(){
        for (String moduleImpl : MODULES){
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IModuleApplication)
                    ((IModuleApplication) obj).init(instance);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static DaoSession getDaoInstant(){
        return daoSession;
    }
}