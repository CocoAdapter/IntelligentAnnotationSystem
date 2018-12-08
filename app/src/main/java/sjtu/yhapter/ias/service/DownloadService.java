package sjtu.yhapter.ias.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DownloadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface DownloadListener {
        /**
         * called when the download start
         * @param totalSize total size of the file to be downloaded
         */
        void onStart(long totalSize);

        /**
         * called when the current already downloaded size is updated
         * @param currSize the current downloaded size
         */
        void onProgress(long currSize);

        /**
         * called when the download is failed, which means the onFinish() will never be called
         * @param errorInfo error info
         */
        void onFail(String errorInfo);

        /**
         * called when the download is finished with no error
         */
        void onFinish();
    }
}
