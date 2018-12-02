package sjtu.yhapter.ias.presenter.contract;

import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.base.BaseContract;

public interface MainContract extends BaseContract {

    interface View extends BaseContract.BaseView {
        void showError(String msg);

        void onTeachClassInfo(TeachClass teachClass);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getTeachClassInfo(String stuId);

        void joinTeachClass(String stuId, long teachClassId);
    }
}
