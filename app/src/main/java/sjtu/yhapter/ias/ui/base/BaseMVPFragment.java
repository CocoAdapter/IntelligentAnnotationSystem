package sjtu.yhapter.ias.ui.base;

public abstract class BaseMVPFragment<T extends BaseContract.BasePresenter> extends BaseFragment implements BaseContract.BaseView {

    protected T presenter;
    protected abstract T bindPresenter();

    @SuppressWarnings("unchecked")
    @Override
    protected void processLogic() {
        presenter = bindPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
