package sjtu.yhapter.ias.ui.adapter;

import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.ui.adapter.view.BookShelfHolder;
import sjtu.yhapter.ias.ui.base.adapter.IViewHolder;
import sjtu.yhapter.ias.widget.adapter.WholeAdapter;

public class BookShelfAdapter extends WholeAdapter<Book> {
    @Override
    protected IViewHolder<Book> createViewHolder(int viewType) {
        return new BookShelfHolder();
    }
}
