package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;

import sjtu.yhapter.ias.presenter.contract.NoteContract;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;

/**
 * Created by CocoAdapter on 2018/11/30.
 */

public class NoteFragment extends BaseMVPFragment<NoteContract.Presenter> implements NoteContract.View {
    @Override
    protected NoteContract.Presenter bindPresenter() {
        return null;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getLayoutResID() {
        return 0;
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
