package sjtu.yhapter.ias.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.base.adapter.ViewHolderImpl;
import sjtu.yhapter.ias.util.DownloadUtils;
import sjtu.yhapter.reader.util.LogUtil;

public class BookShelfHolder extends ViewHolderImpl<Book> {
    private ImageView imgCover;
    private TextView tvTitle;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_shelf;
    }

    @Override
    public void initView() {
        imgCover = findViewById(R.id.img_cover);
        tvTitle = findViewById(R.id.tv_title);
    }

    @Override
    public void onBind(Book book, int pos) {
        tvTitle.setText(book.getTitle());
        String coverPath = book.getCoverPath();
        if (coverPath != null && !coverPath.equals("")) {
            // Glide.
        }
    }
}
