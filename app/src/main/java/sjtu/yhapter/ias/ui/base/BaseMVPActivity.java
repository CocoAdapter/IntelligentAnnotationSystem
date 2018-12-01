package sjtu.yhapter.ias.ui.base;

public abstract class BaseMVPActivity<T extends BaseContract.BasePresenter> extends BaseActivity {

    protected T presenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic() {
        attachView(bindPresenter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @SuppressWarnings("unchecked")
    private void attachView(T presenter){
        this.presenter = presenter;
        presenter.attachView(this);
    }
}
