package sjtu.yhapter.ias.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sjtu.yhapter.ias.RxBus;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.model.pojo.DownloadTask;
import sjtu.yhapter.ias.model.remote.download.DownloadListener;
import sjtu.yhapter.ias.ui.base.BaseService;
import sjtu.yhapter.ias.util.DownloadUtils;
import sjtu.yhapter.reader.util.LogUtil;

public class DownloadService extends BaseService {
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_FINISH = 3;
    public static final int STATUS_ERROR = 4;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 主线程上
        LogUtil.log("onStartCommand");
        Disposable downloadDisp = RxBus.getInstance().toObservable(DownloadTask.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    DownloadUtils task = new DownloadUtils(Constants.BOOK_DOWNLOAD_BASE_URL, event.getDownloadListener());
                    addDisposable(task.download(event.getLink(), event.getLocalPath()));
                });
        addDisposable(downloadDisp);

        return super.onStartCommand(intent, flags, startId);
    }

    class TaskBuilder extends Binder implements DownloadManager {
        @Override
        public List<DownloadTask> getDownloadTasks() {
            return null;
        }

        @Override
        public void setOnDownloadListener(int position, DownloadListener downloadListener) {

        }

        @Override
        public void setStatus(int position, int status) {

        }
    }

    public interface DownloadManager {
        List<DownloadTask> getDownloadTasks();

        void setOnDownloadListener(int position, DownloadListener downloadListener);

        void setStatus(int position, int status);
    }
}
