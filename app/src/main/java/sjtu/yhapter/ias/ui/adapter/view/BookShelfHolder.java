package sjtu.yhapter.ias.ui.adapter.view;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.Book;
import sjtu.yhapter.ias.model.pojo.DownloadTask;
import sjtu.yhapter.ias.service.DownloadService;
import sjtu.yhapter.ias.ui.base.adapter.ViewHolderImpl;
import sjtu.yhapter.reader.util.LogUtil;

public class BookShelfHolder extends ViewHolderImpl<Book> {
    private RelativeLayout rlShadow;
    private ImageView imgDownload;
    private ProgressBar pbDownload;

    private ImageView imgCover;
    private TextView tvTitle;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_shelf;
    }

    @Override
    public void initView() {
        rlShadow = findViewById(R.id.rl_shadow);
        imgDownload = findViewById(R.id.img_download);
        pbDownload = findViewById(R.id.pb_download);

        imgCover = findViewById(R.id.img_cover);
        tvTitle = findViewById(R.id.tv_title);
    }

    @Override
    public void onBind(Book book, int pos) {
        tvTitle.setText(book.getName());
        String coverPath = book.getPicture();
        if (coverPath != null && !coverPath.equals("")) {
            // Glide.
            Glide.with(getContext())
                    .load(Uri.parse(coverPath))
                    .into(imgCover);
        }

        // 下载状态
        DownloadTask task = book.getDownloadTask();
        switch (task.getStatus()) {
            case DownloadService.STATUS_WAIT:
                rlShadow.setVisibility(View.VISIBLE);
                imgDownload.setImageResource(R.drawable.ic_download_start);
                setProgress(task);
                break;
            case DownloadService.STATUS_DOWNLOADING:
                rlShadow.setVisibility(View.VISIBLE);
                imgDownload.setImageResource(R.drawable.ic_download_pause);
                setProgress(task);
                break;
            case DownloadService.STATUS_PAUSE:
                rlShadow.setVisibility(View.VISIBLE);
                imgDownload.setImageResource(R.drawable.ic_download_start);
                break;
            case DownloadService.STATUS_FINISH:
                rlShadow.setVisibility(View.GONE);
                break;
            case DownloadService.STATUS_ERROR:
                LogUtil.log("download error");
                rlShadow.setVisibility(View.VISIBLE);
                imgDownload.setImageResource(R.drawable.ic_download_start);
                break;
        }
    }

    private void setProgress(DownloadTask task) {
        // 大文件需要转换
        long size = task.getSize();
        long progress = task.getProgress();
        if (size > Integer.MAX_VALUE) {
            while (size > Integer.MAX_VALUE) {
                size /= 1024;
                progress /= 1024;
            }
        }

        pbDownload.setMax((int) size);
        pbDownload.setProgress((int) progress);
    }
}
