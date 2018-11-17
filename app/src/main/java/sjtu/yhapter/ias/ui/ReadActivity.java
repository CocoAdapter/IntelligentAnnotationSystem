package sjtu.yhapter.ias.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.reader.widget.page.BaseReaderView;
import sjtu.yhapter.reader.widget.page.PageAnimationMode;
import sjtu.yhapter.reader.widget.page.ReaderView;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseActivity {
    private ReaderView readerView;
    private Button btn;

    private boolean isAnim = false;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_read);
        readerView = findViewById(R.id.reader_view);
//        readerView.setAnimation(PageAnimationMode.COVER);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            PageAnimationMode mode = isAnim ? PageAnimationMode.COVER : PageAnimationMode.NONE;
            readerView.setAnimation(mode);
            isAnim = !isAnim;
        });
    }
}
