package sjtu.yhapter.ias.presenter.contract;

import java.util.List;

import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.base.BaseContract;

public interface BookShelfContract {

    interface View extends BaseContract.BaseView {
        void onRequestBooks(List<Book> books);

        void onRequestMenu(List<TeachClass> teachClasses);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void requestBooks(long menuIndex);

        void requestMenu();
    }

}