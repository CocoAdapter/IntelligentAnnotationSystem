package sjtu.yhapter.ias.presenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.LoginContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.util.SharedPrefUtil;

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {
    @Override
    public void login(String username, String password) {
        RemoteRepository
                .getInstance()
                .login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        SharedPrefUtil.getInstance().putString(Constants.UID, username);
                        view.onLogin(aBoolean, "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onLogin(false, e.getMessage());
                    }
                });

    }
}
