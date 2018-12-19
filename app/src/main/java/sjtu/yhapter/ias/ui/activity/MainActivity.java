package sjtu.yhapter.ias.ui.activity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.presenter.MainPresenter;
import sjtu.yhapter.ias.presenter.contract.MainContract;
import sjtu.yhapter.ias.ui.base.BaseFragmentActivity;
import sjtu.yhapter.ias.ui.fragment.BookShelfFragment;
import sjtu.yhapter.ias.ui.fragment.FindFragment;
import sjtu.yhapter.ias.ui.fragment.MineFragment;
import sjtu.yhapter.ias.ui.fragment.TeachClassFragment;
import sjtu.yhapter.reader.widget.ImageTextView;

public class MainActivity extends BaseFragmentActivity<MainContract.Presenter> implements MainContract.View {
    private ImageTextView tabFind, tabBookShelf, tabTeachClass, tabMine;

    private FindFragment findFragment;
    private BookShelfFragment bookShelfFragment;
    private TeachClassFragment teachClassFragment;
    private MineFragment mineFragment;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        findFragment = new FindFragment();
        bookShelfFragment = new BookShelfFragment();
        teachClassFragment = new TeachClassFragment();
        mineFragment = new MineFragment();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        tabFind = findViewById(R.id.tab_find);
        tabBookShelf = findViewById(R.id.tab_book_shelf);
        tabTeachClass = findViewById(R.id.tab_teach_class);
        tabMine = findViewById(R.id.tab_mine);
    }

    @Override
    protected void initListener() {
        super.initListener();
        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.tab_find:
                    setSelected(0);
                    switchFragment(findFragment).commit();
                    break;
                case R.id.tab_book_shelf:
                    setSelected(1);
                    switchFragment(bookShelfFragment).commit();
                    break;
                case R.id.tab_teach_class:
                    setSelected(2);
                    switchFragment(teachClassFragment).commit();
                    break;
                case R.id.tab_mine:
                    setSelected(3);
                    switchFragment(mineFragment).commit();
                    break;
            }
        };

        tabFind.setOnClickListener(ocl);
        tabBookShelf.setOnClickListener(ocl);
        tabTeachClass.setOnClickListener(ocl);
        tabMine.setOnClickListener(ocl);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        tabFind.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected MainContract.Presenter bindPresenter() {
        return new MainPresenter();
    }

    private void setSelected(int index) {
        tabFind.setSelected(0 == index);
        tabBookShelf.setSelected(1 == index);
        tabTeachClass.setSelected(2 == index);
        tabMine.setSelected(3 == index);
    }
}
