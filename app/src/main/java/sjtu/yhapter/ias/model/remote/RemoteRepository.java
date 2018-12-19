package sjtu.yhapter.ias.model.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.util.LogUtil;

public class RemoteRepository {
    private static volatile RemoteRepository instance;

    private Retrofit retrofit;
    private Api api;

    private RemoteRepository() {
        retrofit = RemoteHelper.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
    }

    public static RemoteRepository getInstance() {
        if (instance == null) {
            synchronized (RemoteRepository.class) {
                if (instance == null)
                    instance = new RemoteRepository();
            }
        }
        return instance;
    }

    public Single<List<TeachClass>> getTeachClassList(long userId) {
        return api.getClassList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(response -> new Gson()
                        .fromJson(response, new TypeToken<List<TeachClass>>() {
                        }.getType()));
    }

    public Single<List<Book>> getClassBookList(long classId, String userId) {
        return api.getClassBooks(classId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(response -> new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()
                        .fromJson(response, new TypeToken<List<Book>>() {
                        }.getType()));
    }

    public Single<Boolean> login(String username, String password) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("userID", username)
                    .put("passwd", password);
        } catch (JSONException ignore) {
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        return api.login(requestBody)
                .subscribeOn(Schedulers.io())
                .map(response -> true);
    }

    public Single<Boolean> register(String name, String stuId, String phone, String password) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name)
                    .put("studentID", stuId)
                    .put("phone", phone)
                    .put("passwd", password);
        } catch (JSONException ignore) {
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        return api.register(requestBody)
                .subscribeOn(Schedulers.io())
                .map(response -> true);
    }

    public Single<Boolean> joinClass(String classId, String uid) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("classID", classId)
                    .put("userID", uid);
        } catch (JSONException ignore) {
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        return api.joinClass(requestBody)
                .subscribeOn(Schedulers.io())
                .map(response -> true);
    }

    public Single<Boolean> saveAnnotation(Annotation annotation, String classId) {
        String str = new Gson().toJson(annotation);
        JSONObject obj = null;
        try {
            obj = new JSONObject(str)
                    .put("classid", classId);
        } catch (JSONException ignore) {
        }
        assert obj != null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        return api.saveAnnotation(requestBody)
                .subscribeOn(Schedulers.io())
                .map(response -> true);
    }
}
