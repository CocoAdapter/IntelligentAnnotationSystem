package sjtu.yhapter.ias.presenter;

import org.reactivestreams.Subscription;

import sjtu.yhapter.ias.presenter.contract.ReadContract;
import sjtu.yhapter.ias.ui.base.RxPresenter;

public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {

    private Subscription chapterSub;

    @Override
    public void detachView() {
        super.detachView();
        if (chapterSub != null)
            chapterSub.cancel();
    }
}
