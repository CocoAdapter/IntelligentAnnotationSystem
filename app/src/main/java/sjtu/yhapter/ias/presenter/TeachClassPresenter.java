package sjtu.yhapter.ias.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.TeachClassContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.util.LogUtil;

public class TeachClassPresenter extends RxPresenter<TeachClassContract.View> implements TeachClassContract.Presenter {
    @Override
    public void loadTeachClass(Long userId) {
        RemoteRepository.getInstance().getTeachClassList(userId)
                .doOnSuccess(teachClasses ->
                        App.getDaoInstant().getTeachClassDao().insertOrReplaceInTx(teachClasses))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TeachClass>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<TeachClass> teachClasses) {
                        view.complete();
                        view.onTeachClassLoaded(teachClasses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.log(e.toString());
                        view.complete();
                        view.showError(e.getMessage());
                    }
                });
    }

    @Override
    public void joinTeachClass(Long userId, String classCode) {
        RemoteRepository.getInstance()
                .joinClass(classCode, userId + "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        view.onJoinTeachClass(aBoolean, "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onJoinTeachClass(false, e.getMessage());
                    }
                });
    }
}
