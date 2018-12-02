package sjtu.yhapter.ias.ui.adapter;

import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.adapter.view.TeachClassHolder;
import sjtu.yhapter.ias.ui.base.adapter.IViewHolder;
import sjtu.yhapter.ias.widget.adapter.WholeAdapter;

public class TeachClassAdapter extends WholeAdapter<TeachClass> {

    @Override
    protected IViewHolder<TeachClass> createViewHolder(int viewType) {
        return new TeachClassHolder();
    }
}
