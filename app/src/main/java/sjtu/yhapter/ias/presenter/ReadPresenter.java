package sjtu.yhapter.ias.presenter;

import android.util.SparseArray;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import sjtu.yhapter.ias.model.pojo.AnnotationWrapper;
import sjtu.yhapter.ias.model.remote.RemoteRepository;
import sjtu.yhapter.ias.presenter.contract.ReadContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.util.LogUtil;

public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {
    private Disposable requestFeedbackDip;

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

    @Override
    public void requestAnnotationFeedback(long classId, String userId, long bookId, long chapterId, SparseArray<String> holder) {
        if (requestFeedbackDip != null)
            requestFeedbackDip.dispose();

        RemoteRepository.getInstance().getFeedback(classId, userId, chapterId, bookId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AnnotationWrapper>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        requestFeedbackDip = d;
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<AnnotationWrapper> annotationWrappers) {
                        for (AnnotationWrapper wrapper : annotationWrappers) {
                            holder.put(wrapper.getId().intValue(), wrapper.getFeedback());
                        }
                        requestFeedbackDip = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.log(e.getMessage());
                        requestFeedbackDip = null;
                    }
                });
    }
}
