package sjtu.yhapter.ias.ui;

import android.os.Bundle;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.reader.widget.ReaderView;

/**
 * Created by CocoAdapter on 2018/11/11.
 */

public class ReadActivity extends BaseActivity {
    private ReaderView readerView;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_read);
//        readerView = findViewById(R.id.reader_view);
    }
}
