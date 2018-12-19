package sjtu.yhapter.ias.presenter;

import org.reactivestreams.Subscription;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.ReadContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.util.LogUtil;

public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {
    @Override
    public void saveAnnotation(Annotation annotation, String classId) {
        RemoteRepository.getInstance()
                .saveAnnotation(annotation, classId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.log(e.getMessage());
                    }
                });
    }

    @Override
    public void deleteAnnotation(Annotation annotation) {
        // do nothing (no deletion of data on server)
    }
}
