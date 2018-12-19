package sjtu.yhapter.ias.presenter.contract;

import java.util.List;

import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.base.BaseContract;

public interface TeachClassContract {

    interface View extends BaseContract.BaseView {
        void onTeachClassLoaded(List<TeachClass> teachClasses);

        void onJoinTeachClass(TeachClass teachClass);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadTeachClass(Long userId);

        void joinTeachClass(Long userId, String classCode);
    }
}
