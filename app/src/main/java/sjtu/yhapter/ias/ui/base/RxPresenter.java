package sjtu.yhapter.ias.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {
    protected T view;
    protected CompositeDisposable disposable;

    protected void unSubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    protected void addDisposable(Disposable subscription) {
        if (disposable == null) {
            disposable = new CompositeDisposable();
        }
        disposable.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        unSubscribe();
    }
}
