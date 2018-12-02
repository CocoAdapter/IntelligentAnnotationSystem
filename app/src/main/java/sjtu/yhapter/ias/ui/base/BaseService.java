package sjtu.yhapter.ias.ui.base;

import android.app.Service;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseService extends Service {
    private CompositeDisposable disposable;

    protected void addDisposable(Disposable disposable) {
        if (this.disposable == null)
            this.disposable = new CompositeDisposable();
        this.disposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
