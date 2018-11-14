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

//        adapter = new PageAdapter(this, null);
//        readerView.setPageAdapter(adapter);
//        readerView.setOnActionListener(new BookReaderView.OnActionListener() {
//            @Override
//            public void onClick() {
//                LogUtil.log(ReadActivity.this, "onClick");
//            }
//
//            @Override
//            public void onCenterClick() {
//                LogUtil.log(ReadActivity.this, "onCenterClick");
//            }
//
//            @Override
//            public void onPrePage() {
//                // 调 Adapter 来更新
//                LogUtil.log(ReadActivity.this, "onPrePage");
//            }
//
//            @Override
//            public void onNextPage() {
//                LogUtil.log(ReadActivity.this, "onNextPage");
//            }
//
//            @Override
//            public void onCancel() {
//                LogUtil.log(ReadActivity.this, "onCancel");
//            }
//        });
    }
}
