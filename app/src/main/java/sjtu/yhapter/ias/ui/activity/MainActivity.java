package sjtu.yhapter.ias.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.presenter.MainPresenter;
import sjtu.yhapter.ias.presenter.contract.MainContract;
import sjtu.yhapter.ias.ui.adapter.TeachClassAdapter;
import sjtu.yhapter.ias.ui.base.BaseMVPActivity;
import sjtu.yhapter.ias.widget.refresh.ScrollRefreshRecyclerView;
import sjtu.yhapter.reader.util.LogUtil;

// TODO MainActivity 应该由四个Fragment组成，仿微信阅读，分别是发现（搜索书，推荐书），书架，教学班，我
public class MainActivity extends BaseMVPActivity<MainContract.Presenter> implements MainContract.View {
    private ScrollRefreshRecyclerView rvList;
    private TeachClassAdapter teachClassAdapter;

    private Student student;

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        student = App.getDaoInstant().getStudentDao().loadByRowId(0);

        teachClassAdapter = new TeachClassAdapter();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        rvList = findViewById(R.id.rv_list);
        rvList.setVisibility(View.INVISIBLE);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        rvList.setAdapter(teachClassAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rvList.setOnRefreshListener(() -> {
            fakeLoading();
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();

        // 模拟加载完了
        rvList.setVisibility(View.VISIBLE);
        rvList.startRefresh();
        fakeLoading();

//        JoinTeachClassDialog dialog = new JoinTeachClassDialog(this, this);
//        dialog.setOnJoinClickListener(code -> {
////            presenter.joinTeachClass(student.getStudentId(), code);
//            rvList.setVisibility(View.VISIBLE);
//            rvList.startRefresh(); // 将rvList设置成刷新状态
//            fakeLoading(); // 开始加载
//        });
//        dialog.show();
    }

    private void fakeLoading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.log("running");
                List<TeachClass> teachClasses = fateTeachClasses();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 只能在主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(teachClasses, new Comparator<TeachClass>() {
                            @Override
                            public int compare(TeachClass o1, TeachClass o2) {
                                return o2.getStatus() - o1.getStatus() ;
                            }
                        });
                        teachClassAdapter.refreshItems(teachClasses);
                        rvList.finishRefresh();
                    }
                });
            }
        }).start();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        if (rvList.isRefreshing()) {
            rvList.finishRefresh();
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected MainContract.Presenter bindPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onTeachClassInfo(TeachClass teachClass) {

    }

    private List<TeachClass> fateTeachClasses() {
        List<TeachClass> teachClasses = new ArrayList<>();
        TeachClass teachClass = new TeachClass();
        teachClass.setId(1L);
        teachClass.setStatus(1);
        teachClass.setStudentId(1L);
        teachClasses.add(teachClass);

        TeachClass teachClass1 = new TeachClass();
        teachClass1.setId(1L);
        teachClass1.setStatus(2);
        teachClass1.setStudentId(1L);
        teachClasses.add(teachClass1);

        return teachClasses;
    }
}
