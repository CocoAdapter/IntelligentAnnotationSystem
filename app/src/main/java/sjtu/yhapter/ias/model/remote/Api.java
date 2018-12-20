package sjtu.yhapter.ias.model.remote;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {

    @GET("enquiryClassStatus")
    Single<String> getClassList(@Query("user") long userId);

    @GET("enquiryClassJoined/books")
    Single<String> getClassBooks(@Query("classID") long classId, @Query("userID") String userId);

    @GET("getFeedback")
    Single<String> getAnnotationFeedback(@Query("classId") long classId,
                                         @Query("userId") String userId,
                                         @Query("chapterId") long chapterId,
                                         @Query("bookId") long bookId);

    @POST("studentLogin")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Single<String> login(@Body RequestBody info);

    @POST("studentRegister")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Single<String> register(@Body RequestBody info);

    @POST("applyClass")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Single<String> joinClass(@Body RequestBody info);

    @POST("uploadAnnotation")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Single<String> saveAnnotation(@Body RequestBody info);
}
