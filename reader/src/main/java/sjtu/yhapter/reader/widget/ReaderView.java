package sjtu.yhapter.reader.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public class ReaderView extends BaseReaderView {
    private int index = 1024;
    private boolean isLastMovingNext = false;


    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        TODO 临时测试

        drawCurrPage(index);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean hasPrePage() {
        if (index == -1)
            return false;
        LogUtil.log(this, "requestPrePage");
        // test
        drawNextPage(--index);
        isLastMovingNext = false;
        // test
        return true;
    }

    @Override
    public boolean hasNextPage() {
        if (index == -1)
            return false;
        LogUtil.log(this, "requestNextPage");
        // test
        drawNextPage(++index);
        isLastMovingNext = true;
        // test
        return true;
    }

    @Override
    public void cancelPage() {
        LogUtil.log(this, "cancelPage");
        if (isLastMovingNext)
            index--;
        else
            index++;
        invalidate();
    }

    public void drawCurrPage(int index) {
        Canvas canvas = new Canvas(pageAnimation.getFrontBitmap());
        drawPage(canvas, index);
    }

    public void drawNextPage(int index) {
        ((HorizonPageAnim) pageAnimation).changePage();

        Canvas canvas = new Canvas(pageAnimation.getFrontBitmap());
        drawPage(canvas, index);
    }

    private void drawPage(Canvas canvas, int index) {
        LogUtil.log(this, "drawPage " + index);

        canvas.drawColor(index % 2 == 0 ? Color.MAGENTA : Color.CYAN);

        String text = "index: " + index;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, (viewWidth - textWidth)/ 2, (viewHeight - textHeight) / 2, textPaint);

        invalidate();
    }
}
