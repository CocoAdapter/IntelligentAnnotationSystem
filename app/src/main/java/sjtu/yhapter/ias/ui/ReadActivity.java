package sjtu.yhapter.ias.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.reader.widget.page.PageAdapter;
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
        readerView.setPageAdapter(new TestPageAdapter());
//        readerView.setPageElement(new DefaultPageElement());
//        readerView.setPageLoader(new DefaultPageLoader());
//        readerView.setAnimation(PageAnimationMode.COVER);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            PageAnimationMode mode = isAnim ? PageAnimationMode.NONE : PageAnimationMode.COVER;
            readerView.setAnimation(mode);
            isAnim = !isAnim;
        });
    }

    private static class TestPageAdapter implements PageAdapter {
        private TextPaint textPaint = new TextPaint();

        public TestPageAdapter() {
            textPaint = new TextPaint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(28);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void draw(int position, Canvas canvas) {
            canvas.drawColor(position % 2 == 0 ? Color.MAGENTA : Color.CYAN);

            String text = "index: " + position;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textHeight = fontMetrics.top - fontMetrics.bottom;
            float textWidth = textPaint.measureText(text);
            canvas.drawText(text, (1080 - textWidth)/ 2, (1920 - textHeight) / 2, textPaint);
        }
    }
}
