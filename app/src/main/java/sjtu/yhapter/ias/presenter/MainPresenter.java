package sjtu.yhapter.ias.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.MainContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.util.LogUtil;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    @Override
    public void getTeachClassInfo(String stuId) {
        Disposable disposable = RemoteRepository.getInstance()
                .requestTeachClassInfo(stuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(teachClass -> {
                    view.onTeachClassInfo(teachClass);
                    view.complete();
                }, (e) -> {
                    if (isNull(e)) {
                        view.onTeachClassInfo(null);
                    } else {
                        LogUtil.log(e.toString());
                        view.showError(e.toString());
                        view.complete();
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void joinTeachClass(String stuId, long teachClassId) {
//        Disposable disposable = RemoteRepository.getInstance()
//                .joinTeachClassInfo(stuId, teachClassId)
//                .subscribeOn(Schedulers.io())
//                .doOnSuccess(teachClass -> {
//                    Student student = App.getInstance().getStudent();
//                    student.setTeachClassId(teachClass.getId());
//
//                    App.getDaoInstant().getStudentDao().save(student);
//                    App.getDaoInstant().getTeachClassDao().save(teachClass);
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(teachClass -> {
//                    view.complete();
//                    view.onTeachClassInfo(teachClass);
//                }, (e) -> {
//                    LogUtil.log(e.toString());
//                    view.showError(e.toString());
//                    view.complete();
//                });
//        addDisposable(disposable);
    }

    private boolean isNull(Throwable e) {
        if (e instanceof HttpException) {
            HttpException ex = (HttpException) e;
            return ex.code() == 404;
        }
        return false;
    }
}
