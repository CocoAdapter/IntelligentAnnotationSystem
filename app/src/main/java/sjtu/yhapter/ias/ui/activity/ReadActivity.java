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

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.ui.fragment.BaseFragment;
import sjtu.yhapter.ias.ui.fragment.CatalogFragment;
import sjtu.yhapter.ias.ui.fragment.HotLineFragment;
import sjtu.yhapter.ias.ui.fragment.NoteFragment;
import sjtu.yhapter.reader.reader.ReaderView;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseActivity {
    private DrawerLayout drawer;
    private ReaderView readerView;
    private TabLayout tab;

    private Fragment[] drawerFragments;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_read);
        drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        readerView = findViewById(R.id.reader_view);
        readerView.setOnClickListener(new ReaderView.OnClickListener() {
            @Override
            public void onCenterClick() {
                // TODO 这里应该是toggle出来菜单
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                drawer.openDrawer(Gravity.START);
                tab.getTabAt(0).getCustomView().setSelected(true);
            }

            @Override
            public boolean canTouch() {
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                    return false;
                }
                return true;
            }
        });

        tab = findViewById(R.id.tab_layout);

        initFragments();
        initTab();
        initListener();
    }

    private void initListener() {

    }

    private void initFragments() {
        drawerFragments = new Fragment[3];
        drawerFragments[0] = new CatalogFragment();
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

}
