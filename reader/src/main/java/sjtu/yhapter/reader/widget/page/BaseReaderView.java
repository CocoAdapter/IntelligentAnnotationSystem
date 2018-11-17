package sjtu.yhapter.reader.widget.page;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import sjtu.yhapter.reader.model.Constants;
import sjtu.yhapter.reader.widget.animation.NonePageAnim;
import sjtu.yhapter.reader.widget.animation.PageAnimation;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public abstract class BaseReaderView extends View implements PageAnimation.PageCarver {
    protected final static int MOVE_SENSITIVITY = Constants.SLOP;
    protected final static int LONG_CLICK_DURATION = 500;

    protected boolean isReady;
    protected int viewWidth, viewHeight;
    protected boolean isMoving;
    protected int startX, startY;

    // onTouch
    protected boolean canTouch;
    protected boolean isLongClick;
//    protected boolean isInAnimation;
    protected Timer timer;

    protected PageAnimation pageAnimation;
    protected OnTouchListener onTouchListener;

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

        isReady = true;
        canTouch = true;

        pageAnimation = new NonePageAnim(getContext(), viewWidth, viewHeight);
        pageAnimation.setPageCarver(this);
        // from portrait to landscape will trigger refreshing curr page, auto
        prepareCurrPage();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (pageAnimation != null)
            pageAnimation.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canTouch && event.getAction() != MotionEvent.ACTION_DOWN)
            return true;

        final int x = (int) event.getX();
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                isLongClick = false;
                isMoving = false;

                if (onTouchListener != null)
                    canTouch = onTouchListener.canTouch();

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (canTouch)
                            isLongClick = onTouchListener.onLongClickDown(x, y);
                    }
                }, LONG_CLICK_DURATION);
                // animation needs to reset
                if (pageAnimation != null)
                    pageAnimation.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoving)
                    isMoving = Math.abs(startX - x) > MOVE_SENSITIVITY
                            || Math.abs(startY - y) > MOVE_SENSITIVITY;

                if (isMoving) {
                    if (isLongClick && onTouchListener != null) {
                        onTouchListener.onLongClickMove(x, y);
                    } else {
                        cancelLongClickListen();

                        if (pageAnimation != null) {
//                            isInAnimation = true;
                            pageAnimation.onTouchEvent(event);
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isLongClick) {
                    // long click has been triggered
                    onTouchListener.onLongClickUp(x, y);

                    cancelLongClickListen();
                } else {
                    cancelLongClickListen();

                    if (!isMoving && onTouchListener != null) {
                        boolean intercept = onTouchListener.onClick(x, y);
                        if (intercept)
                            return true;
                    }

                    if (pageAnimation != null)
                        pageAnimation.onTouchEvent(event);
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (pageAnimation != null)
            pageAnimation.scroll();
    }

    @Override
    public void requestInvalidate(boolean post) {
        if (post)
            postInvalidate();
        else
            invalidate();
    }

    public void setPageAnimation(PageAnimation pageAnimation) {
        if (!isReady)
            return;

        this.pageAnimation = pageAnimation;
        this.pageAnimation.setPageCarver(this);
        prepareCurrPage();
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public abstract void prepareCurrPage();

    private void cancelLongClickListen() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isLongClick = false;
    }

    public interface OnTouchListener {
        /**
         * a ACTION_DOWN is triggered, returns if the reader view is allowed to
         * be touched.
         *
         * only if it returns true, the below methods will not be
         * intercepted.
         *
         * canTouch means the readerView will handle the MotionEvent,
         * including click, long click, animation, etc. until the next
         * call that returns false.
         *
         * @return if the view can be touch
         */
        boolean canTouch();

        /**
         * a possible click event is triggered at (x, y). return if this event is
         * consumed, which means intercept the control flow, indicating that
         * this is, and finally will also be an onClick event.
         *
         * in default code, u can think that no turing page action will be taken into
         * consideration.
         *
         * @param x x
         * @param y y
         * @return if this event is consumed.
         */
        boolean onClick(int x, int y);

        /**
         * a possible long click event is triggered at (x, y). return if this event is
         * consumed, which means intercept the control flow, indicating that
         * this is, and finally will also be an onLongClick event.
         *
         * in default code, u can think that no turing page action will be taken into
         * consideration. if a ACTION_MOVE is triggered, this will be regarded as movement
         * of longClick.
         *
         * @param x x
         * @param y y
         * @return if this event is consumed.
         */
        boolean onLongClickDown(int x, int y);

        /**
         * a movement happened during the longClick action.
         * @param x x
         * @param y y
         */
        void onLongClickMove(int x, int y);

        /**
         * the longClick is finished.
         * @param x x
         * @param y y
         */
        void onLongClickUp(int x, int y);
    }
}
