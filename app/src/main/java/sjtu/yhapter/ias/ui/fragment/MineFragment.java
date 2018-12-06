package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.presenter.MinePresenter;
import sjtu.yhapter.ias.presenter.contract.MineContract;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;

public class MineFragment extends BaseMVPFragment<MineContract.Presenter> implements MineContract.View {

    @Override
    protected MineContract.Presenter bindPresenter() {
        return new MinePresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frgm_mine;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {

    }
}
