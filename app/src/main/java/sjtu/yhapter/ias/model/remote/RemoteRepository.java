package sjtu.yhapter.ias.model.remote;

import io.reactivex.Single;
import retrofit2.Retrofit;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;

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

    public Single<Student> login(String id, String password) {
        return api.login(id, password);
    }

    public Single<TeachClass> requestTeachClassInfo(String stuId) {
        return api.requestTeachClass(stuId);
    }

    public Single<TeachClass> joinTeachClassInfo(String stuId, long teachClassId) {
        return api.joinTeachClass(stuId, teachClassId);
    }
}
