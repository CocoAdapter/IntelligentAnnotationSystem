package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.presenter.BookShelfPresenter;
import sjtu.yhapter.ias.presenter.contract.BookShelfContract;
import sjtu.yhapter.ias.ui.adapter.BookShelfAdapter;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;
import sjtu.yhapter.ias.ui.dialog.BookShelfMenu;
import sjtu.yhapter.ias.widget.refresh.ScrollRefreshRecyclerView;
import sjtu.yhapter.ias.widget.refresh.divider.SpacesItemDecoration;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;

public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter> implements BookShelfContract.View {
    private ScrollRefreshRecyclerView rvBooks;
    private View menuView;
    private BookShelfMenu bookShelfMenu;

    private BookShelfAdapter adapter;
    private long menuIndex;

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void complete() {
        if (rvBooks.isRefreshing())
            rvBooks.finishRefresh();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frgm_book_shelf;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        adapter = new BookShelfAdapter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        menuView = findViewById(R.id.img_menu);
        bookShelfMenu = new BookShelfMenu(getActivity());
        rvBooks = findViewById(R.id.rl_book_shelf);
        rvBooks.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvBooks.addItemDecoration(new SpacesItemDecoration(ScreenUtil.dpToPx(10), ScreenUtil.dpToPx(10)));
        rvBooks.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        rvBooks.setOnRefreshListener(() -> {
            presenter.requestBooks(menuIndex);
        });
        bookShelfMenu.setOnItemClickListener(position -> {
            menuIndex = position;
            refreshByMenu(menuIndex);
        });

        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.img_menu:
                    if (bookShelfMenu.isShowing())
                        bookShelfMenu.dismiss();
                    else
                        bookShelfMenu.showAsDropDown(menuView);
                    break;
            }
        };
        menuView.setOnClickListener(ocl);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        refreshByMenu(BookShelfMenu.ID_RECENTLY_READ); // init loading
    }

    @Override
    public void onRequestBooks(List<Book> books) {
        adapter.refreshItems(books);
    }

    @Override
    public void onRequestMenu(List<TeachClass> teachClasses) {
        bookShelfMenu.refreshItems(teachClasses);
    }

    private void refreshByMenu(long index) {
        rvBooks.startRefresh();
        presenter.requestBooks(index); // init loading
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            LogUtil.log("onResume");
            presenter.requestMenu(); // refresh teach classes info
        }
    }
}
