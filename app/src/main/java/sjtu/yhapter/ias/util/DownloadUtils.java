package sjtu.yhapter.ias.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import sjtu.yhapter.ias.model.remote.download.DownloadApi;
import sjtu.yhapter.ias.model.remote.download.DownloadInterceptor;
import sjtu.yhapter.ias.model.remote.download.DownloadListener;
import sjtu.yhapter.reader.util.IOUtil;

public class DownloadUtils {
    private static final int DEFAULT_TIMEOUT = 15;

    private Retrofit retrofit;
    private DownloadListener downloadListener;

    public DownloadUtils(String baseUrl, DownloadListener downloadListener) {
        this.downloadListener = downloadListener;

        DownloadInterceptor interceptor = new DownloadInterceptor(downloadListener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Disposable download(@NonNull String url, final String filePath) {
        return retrofit.create(DownloadApi.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(ResponseBody::byteStream)
                .observeOn(Schedulers.computation())
                .doOnSuccess(inputStream -> {
                    String s = writeFile(inputStream, filePath);
                    if (s != null) throw new Exception(s);
                })
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(is -> downloadListener.onFinish(),
                        (e) -> downloadListener.onFail(e.getMessage()));
    }

    /**
     *
     * @param is inputStream
     * @param filePath the local path to store the inputStream
     * @return err info, null if no error
     */
    private String writeFile(InputStream is, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean success = file.delete();
            if (!success) return "本地文件已存在且无法删除";
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[2048];
            int len;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            return null;
        } catch (FileNotFoundException e) {
            return "本地文件不存在";
        } catch (IOException e) {
            return "I/O异常";
        } finally {
            IOUtil.close(is);
            IOUtil.close(fos);
        }
    }
}
