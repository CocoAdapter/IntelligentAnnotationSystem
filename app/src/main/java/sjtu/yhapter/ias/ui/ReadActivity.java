package sjtu.yhapter.ias.ui;

import android.os.Bundle;
import android.widget.Button;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.reader.animation.PageAnimationMode;
import sjtu.yhapter.reader.reader.ReaderView;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseActivity {
    private ReaderView readerView;
    private Button btn;

    private boolean isAnim = true;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_read);
        readerView = findViewById(R.id.reader_view);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            PageAnimationMode mode = isAnim ? PageAnimationMode.NONE : PageAnimationMode.COVER;
            readerView.setAnimation(mode);
            isAnim = !isAnim;
        });
    }
}
