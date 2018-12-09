package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.presenter.HotLinePresenter;
import sjtu.yhapter.ias.presenter.contract.HotLineContract;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;

/**
 * Created by CocoAdapter on 2018/11/30.
 */

public class HotLineFragment extends BaseMVPFragment<HotLineContract.Presenter> implements HotLineContract.View {
    @Override
    protected HotLineContract.Presenter bindPresenter() {
        return new HotLinePresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frgm_hotline;
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
