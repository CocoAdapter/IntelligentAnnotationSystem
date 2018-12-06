package sjtu.yhapter.ias.ui.base;

import android.app.Fragment;
import android.app.FragmentTransaction;

import sjtu.yhapter.ias.R;

public abstract class BaseFragmentActivity<T extends BaseContract.BasePresenter> extends BaseMVPActivity {
    protected Fragment currFragment;

    protected FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currFragment != null) {
                transaction.hide(currFragment);
            }
            transaction.add(R.id.container, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currFragment)
                    .show(targetFragment);
        }
        currFragment = targetFragment;
        return transaction;
    }
}
