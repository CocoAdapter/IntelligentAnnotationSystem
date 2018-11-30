package sjtu.yhapter.ias.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.ui.fragment.CategoryFragment;
import sjtu.yhapter.ias.ui.fragment.HotLineFragment;
import sjtu.yhapter.ias.ui.fragment.NoteFragment;
import sjtu.yhapter.reader.loader.BookLoader;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.reader.ReaderView;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseActivity {
    private DrawerLayout drawer;
    private ReaderView readerView;
    private TabLayout tab;

    private View topMenu, bottomMenu;
    private ImageView imgBack;
    private ImageView imgMenu, imgProgress, imgNight, imgFont;
    private boolean isMenuShowing;
    private Animation animTopIn, animTopOut, animBottomIn, animBottomOut;

    private Fragment[] drawerFragments;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_read);
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

        initFragments();
        initTab();
        initListener();
        initAnim();

        readerView.getPageElement().openBook();
    }

    private void initAnim() {
        animTopIn = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_top_enter);
        animTopOut = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_top_exit);
        animBottomIn = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_bottom_enter);
        animBottomOut = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.read_menu_bottom_exit);
    }

    @SuppressWarnings("all")
    private void initListener() {
        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.img_back:
                    onBackClick();
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
                    isMenuShowing = true;
                    toggleMenu(true);
                }
            }

            @Override
            public boolean canTouch() {
                if (isMenuShowing) {
                    isMenuShowing = false;
                    if (drawer.isDrawerOpen(Gravity.START))
                        drawer.closeDrawer(Gravity.START);
                    toggleMenu(false);
                    return false;
                }
                return true;
            }
        });

        readerView.getPageElement().setOnPageChangeListener(new BookLoader.OnPageChangeListener() {
            @Override
            public void onChaptersLoaded(List<? extends ChapterData> chapters) {
                for (ChapterData chapter : chapters) {
                    LogUtil.log(this, chapter.getTitle());
                }

                CategoryFragment categoryFragment = (CategoryFragment) drawerFragments[0];
                categoryFragment.setCategories(chapters);
            }
        });
    }

    @SuppressWarnings("all")
    private void initFragments() {
        drawerFragments = new Fragment[3];
        drawerFragments[0] = new CategoryFragment();
        // not implemented now
        drawerFragments[1] = new NoteFragment();
        drawerFragments[2] = new HotLineFragment();
    }

    private void initTab() {
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
    }

    private void toggleMenu(boolean isOpen) {
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

    @Override
    public void onBackPressed() {
        onBackClick();
    }
}
