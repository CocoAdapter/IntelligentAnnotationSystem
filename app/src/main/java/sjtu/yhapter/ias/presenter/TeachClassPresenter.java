package sjtu.yhapter.ias.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.presenter.contract.TeachClassContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.util.LogUtil;

public class TeachClassPresenter extends RxPresenter<TeachClassContract.View> implements TeachClassContract.Presenter {
    @Override
    public void loadTeachClass(String userId) {
        Single.create((SingleOnSubscribe<List<TeachClass>>) emitter -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {}

            List<TeachClass> teachClasses = new ArrayList<>();
            TeachClass teachClass = new TeachClass();
            teachClass.setId(1L);
            teachClass.setName("计算机系统原理");
            teachClass.setStatus(1);
            teachClass.setStudentId(userId);
            teachClasses.add(teachClass);

            TeachClass teachClass1 = new TeachClass();
            teachClass1.setId(2L);
            teachClass1.setName("学术英语");
            teachClass1.setStatus(2);
            teachClass1.setStudentId(userId);
            teachClasses.add(teachClass1);

            App.getDaoInstant().getTeachClassDao().insertOrReplaceInTx(teachClasses);

            LogUtil.log("teachClassPresenter: " + teachClasses.toString());
            emitter.onSuccess(teachClasses);
        }).subscribeOn(Schedulers.io())
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
                        view.showError();
                    }
                });
    }

    @Override
    public void joinTeachClass(String userId, String classCode) {
        Single.create((SingleOnSubscribe<TeachClass>) emitter -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {}

            TeachClass teachClass1 = new TeachClass();
            teachClass1.setName("过程建模及优化");
            teachClass1.setId(3L);
            teachClass1.setStatus(1);
            teachClass1.setStudentId(userId);

            App.getDaoInstant().getTeachClassDao().insertOrReplaceInTx(teachClass1);
            emitter.onSuccess(teachClass1);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TeachClass>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(TeachClass teachClass) {
                        view.onJoinTeachClass(teachClass);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError();
                    }
                });
    }
}
