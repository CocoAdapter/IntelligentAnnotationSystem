package sjtu.yhapter.ias.presenter.contract;

import sjtu.yhapter.ias.ui.base.BaseContract;

public interface RegisterContract {

    interface View extends BaseContract.BaseView {
        void onRegister(boolean success, String msg);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void register(String name, String stuId, String phone, String password);
    }
}
