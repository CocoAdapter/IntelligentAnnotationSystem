package sjtu.yhapter.ias.model.remote;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.reader.util.LogUtil;

public class RemoteHelper {
    private static volatile RemoteHelper instance;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private RemoteHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (response.isSuccessful())
                    LogUtil.log("intercept: " + request.url().toString());
                    return response;
                }).build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
    }

    public static RemoteHelper getInstance() {
        if (instance == null) {
            synchronized (RemoteHelper.class) {
                if (instance == null)
                    instance = new RemoteHelper();
            }
        }
        return instance;
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
