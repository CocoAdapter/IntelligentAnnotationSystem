package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.presenter.BookShelfPresenter;
import sjtu.yhapter.ias.presenter.contract.BookShelfContract;
import sjtu.yhapter.ias.ui.adapter.BookShelfAdapter;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;
import sjtu.yhapter.ias.widget.refresh.ScrollRefreshRecyclerView;

public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter> implements BookShelfContract.View {
    private ScrollRefreshRecyclerView rvBooks;
    private BookShelfAdapter adapter;

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

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
        rvBooks = findViewById(R.id.rl_book_shelf);
        rvBooks.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        // 分割？
    }

    @Override
    protected void initListener() {

    }
}
