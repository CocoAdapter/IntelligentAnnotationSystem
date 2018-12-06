package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.Student;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.presenter.TeachClassPresenter;
import sjtu.yhapter.ias.presenter.contract.TeachClassContract;
import sjtu.yhapter.ias.ui.adapter.TeachClassAdapter;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;
import sjtu.yhapter.ias.ui.dialog.JoinTeachClassDialog;
import sjtu.yhapter.ias.widget.refresh.ScrollRefreshRecyclerView;
import sjtu.yhapter.reader.util.LogUtil;

public class TeachClassFragment extends BaseMVPFragment<TeachClassContract.Presenter> implements TeachClassContract.View {
    private FloatingActionButton fabAdd;
    private ScrollRefreshRecyclerView rvList;
    private TeachClassAdapter teachClassAdapter;

    private JoinTeachClassDialog joinDialog;

    private Student student;

    @Override
    protected TeachClassContract.Presenter bindPresenter() {
        return new TeachClassPresenter();
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void complete() {
        if (rvList.isRefreshing()) {
            rvList.finishRefresh();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frgm_teach_class;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        student = App.getDaoInstant().getStudentDao().loadAll().get(0);

        teachClassAdapter = new TeachClassAdapter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        fabAdd = findViewById(R.id.fab_add);
        rvList = findViewById(R.id.rv_list);
        rvList.setVisibility(View.INVISIBLE);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        rvList.setAdapter(teachClassAdapter);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        rvList.setVisibility(View.VISIBLE);

        rvList.startRefresh();
        presenter.loadTeachClass(student.getStudentId()); // init loading
    }

    @Override
    protected void initListener() {
        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.fab_add:
                    if (joinDialog == null) {
                        joinDialog = new JoinTeachClassDialog(getActivity(), getActivity());
                        joinDialog.setOnJoinClickListener((code) ->
                                presenter.joinTeachClass(student.getId(), code));
                    }
                    joinDialog.show();
                    break;
            }
        };
        fabAdd.setOnClickListener(ocl);

        rvList.setOnRefreshListener(() -> {
            presenter.loadTeachClass(student.getStudentId());
        });
    }

    @Override
    public void onTeachClassLoaded(List<TeachClass> teachClasses) {
        teachClassAdapter.refreshItems(teachClasses);
    }

    @Override
    public void onJoinTeachClass(TeachClass teachClass) {
        teachClassAdapter.addItem(teachClass);
    }
}
