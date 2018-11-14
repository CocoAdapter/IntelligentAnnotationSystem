package sjtu.yhapter.reader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import sjtu.yhapter.reader.App;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public abstract class BaseReaderView extends View implements PageAnimation.PageCarver {
    protected final static int MOVE_SENSITIVITY = ViewConfiguration.get(App.getInstance()).getScaledTouchSlop();

    protected int viewWidth, viewHeight;

    protected int startX, startY;
    protected boolean isMoving;

    protected PageAnimation pageAnimation;

    protected TextPaint textPaint;

    public BaseReaderView(Context context) {
        this(context, null);
    }

    public BaseReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        pageAnimation = new CoverPageAnim(getContext(), viewWidth, viewHeight);
        pageAnimation.setPageCarver(this);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (pageAnimation != null)
            pageAnimation.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                isMoving = false;
                pageAnimation.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoving)
                    isMoving = Math.abs(startX - x) > MOVE_SENSITIVITY
                            || Math.abs(startY - y) > MOVE_SENSITIVITY;

                if (isMoving)
                    pageAnimation.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                pageAnimation.onTouchEvent(event);
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        pageAnimation.scroll();
    }

    @Override
    public void requestInvalidate(boolean post) {
        if (post)
            postInvalidate();
        else
            invalidate();
    }
}
