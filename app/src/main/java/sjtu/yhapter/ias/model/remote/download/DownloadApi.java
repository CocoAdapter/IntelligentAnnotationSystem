package sjtu.yhapter.ias.model.remote.download;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadApi {

    @Streaming
    @GET
    Single<ResponseBody> download(@Url String url);
}
