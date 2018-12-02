package sjtu.yhapter.ias.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    protected CompositeDisposable disposable;
    private View root;

    @LayoutRes
    protected abstract int getLayoutResID();

    protected void addDisposable(Disposable d) {
        if (disposable == null){
            disposable = new CompositeDisposable();
        }
        disposable.add(d);
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initWidget(Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void processLogic();

    protected <V extends View> V findViewById(int id) {
        if (root == null)
            return null;
        return root.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutResID();
        root = inflater.inflate(resId, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(savedInstanceState);
        initWidget(savedInstanceState);
        initListener();
        processLogic();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposable != null){
            disposable.clear();
        }
    }
}
