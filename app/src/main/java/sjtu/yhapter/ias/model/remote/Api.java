package sjtu.yhapter.ias.model.remote;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;

public interface Api {

    @POST("/login")
    Single<Student> login(@Query("stuId") String id, @Query("password") String password);

    @GET("/teachclass")
    Single<TeachClass> requestTeachClass(@Query("stuId") String stuId);

    @GET("/teachclass")
    Single<TeachClass> joinTeachClass(@Query("stuId") String stuId, @Query("teachClassId") long teachClassId);

    @Streaming
    @GET
    Single<ResponseBody> downloadBook(@Url String link);
}
