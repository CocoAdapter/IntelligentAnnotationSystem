package sjtu.yhapter.ias.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sjtu.yhapter.ias.ui.base.BaseService;

public class DownloadService extends BaseService {
    //加载状态
    private static final int LOAD_ERROR= -1;
    private static final int LOAD_NORMAL= 0;
    private static final int LOAD_PAUSE = 1;
    private static final int LOAD_DELETE = 2; //正在加载时候，用户删除收藏书籍的情况。

    //线程池
    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    //Handler
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(getMainLooper());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class TaskBuilder extends Binder {

    }
}
