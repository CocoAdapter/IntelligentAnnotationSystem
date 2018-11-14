package sjtu.yhapter.ias.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by Administrator on 2016/8/29 0029.
 * 原作者的GitHub Project Path:(https://github.com/PeachBlossom/treader)
 * 绘制页面显示内容的类
 */
public class PageView extends View {
    private int index = 1024;
    protected TextPaint textPaint;

    private final static String TAG = "BookPageWidget";

    private int mViewWidth = 0; // 当前View的宽
    private int mViewHeight = 0; // 当前View的高

    private int mStartX = 0;
    private int mStartY = 0;
    private boolean isMove = false;
    // 动画类
    private PageAnimation mPageAnim;
    // 动画监听类
    private PageAnimation.OnPageChangeListener mPageAnimListener = new PageAnimation.OnPageChangeListener() {
        @Override
        public boolean hasPrev() {
            return PageView.this.hasPrevPage();
        }

        @Override
        public boolean hasNext() {
            return PageView.this.hasNextPage();
        }

        @Override
        public void pageCancel() {
            PageView.this.pageCancel();
        }

        @Override
        public void requestInvalidate(boolean post) {
            if (post)
                postInvalidate();
            else
                invalidate();
        }
    };

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        mPageAnim = new CoverPageAnim(getContext(), mViewWidth, mViewHeight);
        mPageAnim.setListener(mPageAnimListener);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);
        
        drawCurPage();
    }
    
    public Bitmap getNextBitmap() {
        if (mPageAnim == null) return null;
        return mPageAnim.getNextBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "onDraw");
        //绘制动画
        mPageAnim.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                isMove = false;
                mPageAnim.dispatchTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // 判断是否大于最小滑动值。
                int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (!isMove) {
                    isMove = Math.abs(mStartX - event.getX()) > slop || Math.abs(mStartY - event.getY()) > slop;
                }

                // 如果滑动了，则进行翻页。
                if (isMove) {
                    mPageAnim.dispatchTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                mPageAnim.dispatchTouchEvent(event);
                break;
        }
        return true;
    }

    private boolean hasPrevPage() {
        index--;
        drawNextPage();
        return true;
    }
    
    private boolean hasNextPage() {
        index++;
        drawNextPage();
        return true;
    }

    private void pageCancel() {
//        pageCancel();
    }

    @Override
    public void computeScroll() {
        //进行滑动
        mPageAnim.scrollAnim();
//        super.computeScroll();
    }

    public void drawNextPage() {
        if (mPageAnim instanceof HorizonPageAnim) {
            ((HorizonPageAnim) mPageAnim).changePage();
        }
        drawPage(getNextBitmap());
    }

    void drawPage(Bitmap bitmap) {
        drawPage(bitmap, index);
        invalidate();
    }

    /**
     * 绘制当前页。
     *
     */
    public void drawCurPage() {

        drawPage(getNextBitmap());
    }

    private void drawPage(Bitmap bitmap, int index) {
        LogUtil.log(this, "drawPage " + index);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(index % 2 == 0 ? Color.MAGENTA : Color.CYAN);

        String text = "index: " + index;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, (mViewWidth - textWidth)/ 2, (mViewHeight - textHeight) / 2, textPaint);

        invalidate();
    }
}
