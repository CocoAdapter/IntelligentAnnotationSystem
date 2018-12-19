package sjtu.yhapter.ias.model.remote;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {

    @GET("enquiryClassStatus")
    Single<String> getClassList(@Query("user") long userId);

    @GET("enquiryClassJoined/books")
    Single<String> getClassBooks(@Query("classID") long classId);

    @Streaming
    @GET
    Single<ResponseBody> downloadBook(@Url String link);
}
