package sjtu.yhapter.ias.model.remote.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import sjtu.yhapter.ias.service.DownloadService;

public class DownloadInterceptor implements Interceptor {
    private DownloadListener downloadListener;

    public DownloadInterceptor(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new ProgressResponseBody(response.body(), downloadListener)).build();
    }
}
