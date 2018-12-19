package sjtu.yhapter.ias.presenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.RegisterContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.util.SharedPrefUtil;

public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter {

    @Override
    public void register(String name, String stuId, String phone, String password) {
        RemoteRepository.getInstance()
                .register(name, stuId, phone, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        SharedPrefUtil.getInstance().putString(Constants.UID, stuId);
                        view.onRegister(aBoolean, "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onRegister(false, e.getMessage());
                    }
                });
    }
}
