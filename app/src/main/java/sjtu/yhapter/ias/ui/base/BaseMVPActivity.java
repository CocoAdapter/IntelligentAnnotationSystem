package sjtu.yhapter.ias.ui.base;

import android.os.Build;
import android.view.View;

public abstract class BaseMVPActivity<T extends BaseContract.BasePresenter> extends BaseActivity {

    protected T presenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic() {
        attachView(bindPresenter());
        // just for temperature usage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // set foreground color of status bar to be black
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }

    @SuppressWarnings("unchecked")
    private void attachView(T presenter){
        this.presenter = presenter;
        if (presenter != null)
            presenter.attachView(this);
    }


}
