package sjtu.yhapter.reader.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import sjtu.yhapter.reader.App;

/**
 * Created by CocoAdapter on 2018/11/12.
 */

public class SharedPrefUtil {
    private static final String SHARED_PREF_NAME = "IReader_pref";
    private static volatile SharedPrefUtil instance;

    private SharedPreferences sharedReadable;
    private SharedPreferences.Editor sharedWritable;

    /**
     * TODO MODE_MULTI_PROCESS 需要修改
     */
    @SuppressLint("CommitPrefEdits")
    private SharedPrefUtil() {
        sharedReadable = App.getInstance()
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_MULTI_PROCESS);
        sharedWritable = sharedReadable.edit();
    }

    public static SharedPrefUtil getInstance(){
        if(instance == null){
            synchronized (SharedPrefUtil.class){
                if (instance == null){
                    instance = new SharedPrefUtil();
                }
            }
        }
        return instance;
    }

    public void putString(String key,String value){
        sharedWritable.putString(key,value);
        sharedWritable.commit();
    }

    public String getString(String key){
        return sharedReadable.getString(key,"");
    }

    public void putInt(String key,int value){
        sharedWritable.putInt(key, value);
        sharedWritable.commit();
    }

    public int getInt(String key,int def){
        return sharedReadable.getInt(key, def);
    }

    public void putBoolean(String key,boolean value){
        sharedWritable.putBoolean(key, value);
        sharedWritable.commit();
    }

    public boolean getBoolean(String key,boolean def){
        return sharedReadable.getBoolean(key, def);
    }
}
