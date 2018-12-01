package sjtu.yhapter.ias.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable disposable;

    protected void addDisposable(Disposable d) {
        if (disposable == null){
            disposable = new CompositeDisposable();
        }
        disposable.add(d);
    }

    @LayoutRes
    protected abstract int getLayoutResID();

    protected void initData(Bundle savedInstanceState) {

    }

    protected void initWidget() {

    }

    protected void initListener() {

    }

    protected void processLogic() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initData(savedInstanceState);
        initWidget();
        initListener();
        processLogic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    protected void startActivity(Class<? extends AppCompatActivity> activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


}
