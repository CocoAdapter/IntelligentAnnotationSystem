package sjtu.yhapter.ias.ui.adapter;

import sjtu.yhapter.ias.ui.adapter.view.BookShelfHolder;
import sjtu.yhapter.ias.ui.base.adapter.IViewHolder;
import sjtu.yhapter.ias.widget.adapter.WholeAdapter;

public class BookShelfAdapter extends WholeAdapter<Object> {
    @Override
    protected IViewHolder<Object> createViewHolder(int viewType) {
        return new BookShelfHolder();
    }
}
