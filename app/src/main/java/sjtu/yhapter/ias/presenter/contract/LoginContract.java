package sjtu.yhapter.ias.presenter.contract;

import sjtu.yhapter.ias.ui.base.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void onLogin(boolean success, String msg);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void login(String username, String password);
    }
}
