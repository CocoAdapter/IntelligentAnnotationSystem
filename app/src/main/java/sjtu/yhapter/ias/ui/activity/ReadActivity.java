package sjtu.yhapter.ias.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.List;

import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.presenter.ReadPresenter;
import sjtu.yhapter.ias.presenter.contract.ReadContract;
import sjtu.yhapter.ias.ui.base.BaseMVPActivity;
import sjtu.yhapter.ias.ui.fragment.CategoryFragment;
import sjtu.yhapter.ias.ui.fragment.HotLineFragment;
import sjtu.yhapter.ias.ui.fragment.NoteFragment;
import sjtu.yhapter.reader.loader.BookLoader;
import sjtu.yhapter.reader.model.pojo.Book;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.page.PageElement;
import sjtu.yhapter.reader.reader.ReaderView;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.SharedPrefUtil;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseMVPActivity<ReadContract.Presenter> implements ReadContract.View {
    private DrawerLayout drawer;
    private ReaderView readerView;
    private TabLayout tab;
    private View topMenu, bottomMenu;
    private ImageView imgBack;
    private ImageView imgMenu, imgProgress, imgNight, imgFont;
    private boolean isMenuShowing;
    private Animation animTopIn, animTopOut, animBottomIn, animBottomOut;

    private Fragment[] drawerFragments;
    private PageElement pageElement;
    private Book book;

    @Override
    public void onBackPressed() {
        onBackClick();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_read;
    }

    @Override
    protected ReadContract.Presenter bindPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        drawerFragments = new Fragment[3];
        drawerFragments[0] = new CategoryFragment();
        // not implemented now
        drawerFragments[1] = new NoteFragment();
        drawerFragments[2] = new HotLineFragment();

        animTopIn = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_top_enter);
        animTopOut = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_top_exit);
        animBottomIn = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_bottom_enter);
        animBottomOut = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_bottom_exit);

        book = getIntent().getParcelableExtra("book");
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        topMenu = findViewById(R.id.rl_bar);
        imgBack = topMenu.findViewById(R.id.img_back);
        bottomMenu = findViewById(R.id.ll_bottom);
        imgMenu = bottomMenu.findViewById(R.id.img_menu);
        imgProgress = bottomMenu.findViewById(R.id.img_progress);
        imgNight = bottomMenu.findViewById(R.id.img_night);
        imgFont = bottomMenu.findViewById(R.id.img_font);

        drawer = findViewById(R.id.drawer_layout);
        readerView = findViewById(R.id.reader_view);
        tab = findViewById(R.id.tab_layout);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                tx.replace(R.id.container, drawerFragments[tab.getPosition()]);
                tx.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.addTab(tab.newTab().setText(R.string.drawer_catalog), true);
        tab.addTab(tab.newTab().setText(R.string.drawer_note));
        tab.addTab(tab.newTab().setText(R.string.drawer_hotline));

        long uid = Long.valueOf(SharedPrefUtil.getInstance().getString(Constants.UID));
        readerView.setUserId(uid);
        pageElement = readerView.getPageElement();
    }

    @SuppressWarnings("all")
    protected void initListener() {
        super.initListener();

        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.img_menu:
                    drawer.openDrawer(Gravity.START);
                    break;
            }
        };
        imgBack.setOnClickListener(ocl);
        imgMenu.setOnClickListener(ocl);
        imgProgress.setOnClickListener(ocl);
        imgNight.setOnClickListener(ocl);
        imgFont.setOnClickListener(ocl);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        readerView.setOnClickListener(new ReaderView.OnClickListener() {
            @Override
            public void onCenterClick() {
                if (!isMenuShowing) {
                    toggleMenu(true);
                }
            }

            @Override
            public boolean canTouch() {
                if (isMenuShowing) {
                    if (drawer.isDrawerOpen(Gravity.START))
                        drawer.closeDrawer(Gravity.START);
                    toggleMenu(false);
                    return false;
                }
                return true;
            }
        });

        pageElement.setOnPageChangeListener(new BookLoader.OnPageChangeListener() {
            @Override
            public void onChaptersLoaded(List<? extends ChapterData> chapters) {
                CategoryFragment categoryFragment = (CategoryFragment) drawerFragments[0];
                categoryFragment.setCategories(chapters);
            }
        });

        CategoryFragment categoryFragment = (CategoryFragment) drawerFragments[0];
        categoryFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (drawer.isDrawerOpen(Gravity.START))
                    drawer.closeDrawer(Gravity.START);
                toggleMenu(false);

                pageElement.skipToChapter(position);
            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        pageElement.openBook(book);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 关闭书本
    }

    private void toggleMenu(boolean isOpen) {
        // TODO 增删状态栏会触发触发ReaderView on Size Changed
        // TODO 微信的状态栏似乎是画在自己定制的View上的
//        getWindow().getDecorView().setSystemUiVisibility(isOpen ?
//                View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN);
        isMenuShowing = isOpen;
        topMenu.setEnabled(isOpen);
        bottomMenu.setEnabled(isOpen);
        topMenu.startAnimation(isOpen ? animTopIn : animTopOut);
        bottomMenu.startAnimation(isOpen ? animBottomIn : animBottomOut);
        topMenu.setVisibility(isOpen ? View.VISIBLE: View.GONE);
        bottomMenu.setVisibility(isOpen ? View.VISIBLE: View.GONE);
    }

    private void onBackClick() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
            return;
        }

        if (isMenuShowing) {
            isMenuShowing = false;
            toggleMenu(false);
            return;
        }

        finish();
    }
}
