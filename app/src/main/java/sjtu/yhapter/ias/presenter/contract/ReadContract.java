package sjtu.yhapter.ias.presenter.contract;

import android.util.SparseArray;

import sjtu.yhapter.ias.ui.base.BaseContract;
import sjtu.yhapter.reader.model.pojo.Annotation;

public interface ReadContract extends BaseContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void saveAnnotation(Annotation annotation, String classId);

        void deleteAnnotation(Annotation annotation);

        void requestAnnotationFeedback(long classId, String userId, long bookId, long chapterId, SparseArray<String> holder);
    }
}
