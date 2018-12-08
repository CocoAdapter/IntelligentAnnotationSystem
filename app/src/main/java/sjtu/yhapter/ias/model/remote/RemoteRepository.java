package sjtu.yhapter.ias.model.remote;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.reader.util.IOUtil;
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

    public Single<Student> login(String id, String password) {
        return api.login(id, password);
    }

    public Single<TeachClass> requestTeachClassInfo(String stuId) {
        return api.requestTeachClass(stuId);
    }

    public Single<TeachClass> joinTeachClassInfo(String stuId, long teachClassId) {
        return api.joinTeachClass(stuId, teachClassId);
    }

    public Single<String> downloadBook(String link, String localPath) {
        return api.downloadBook(link).map(responseBody -> {
            InputStream is = responseBody.byteStream();
            long totalLength = responseBody.contentLength();

            long currLength = 0;
            OutputStream os = null;
            try {
                os = new FileOutputStream(localPath);
                int len;
                byte[] buff = new byte[2048];
                while ((len = is.read(buff)) != -1) {
                    os.write(buff, 0, len);
                    currLength += len;
                    LogUtil.log(currLength + "/" + totalLength);
                }
                return localPath;
            } catch (IOException e) {
                LogUtil.log(e.toString());
                return null;
            } finally {
                IOUtil.close(is);
                IOUtil.close(os);
            }
        });
    }
}
