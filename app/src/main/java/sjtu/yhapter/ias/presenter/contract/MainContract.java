package sjtu.yhapter.ias.presenter.contract;

import sjtu.yhapter.ias.ui.base.BaseContract;

public interface MainContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

    }
}
