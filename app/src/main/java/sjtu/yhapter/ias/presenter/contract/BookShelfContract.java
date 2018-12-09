package sjtu.yhapter.ias.presenter.contract;

import java.util.List;

import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.model.remote.download.DownloadListener;
import sjtu.yhapter.ias.ui.base.BaseContract;

public interface BookShelfContract {

    interface View extends BaseContract.BaseView {
        void onRequestBooks(List<Book> books);

        void onRequestMenu(List<TeachClass> teachClasses);

        /**
         *
         * @param pos pos of task send in by {@link BookShelfContract.Presenter#downloadBook(int, Book)} pos}
         * @param post if the caller is running on non-UI thread
         */
        void onDownloadStatusUpdate(int pos, boolean post);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void requestBooks(long menuIndex);
        void requestMenu();
        void downloadBook(int pos, Book book);
    }

}
