package sjtu.yhapter.reader.widget.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public abstract class PageAnimation {
    protected Scroller scroller;
    protected PageCarver pageCarver;
    protected Direction direction;

    protected boolean isRunning;

    protected int screenWidth;
    protected int screenHeight;

    protected int marginWidth;
    protected int marginHeight;

    protected int viewWidth;
    protected int viewHeight;

    protected float startX;
    protected float startY;

    protected float touchX;
    protected float touchY;

    protected float lastX;
    protected float lastY;

    public PageAnimation(Context context, int w, int h) {
        this(context, w, h, 0, 0);
    }

    public PageAnimation(Context context, int w, int h, int marginWidth, int marginHeight) {
        screenWidth = w;
        screenHeight = h;

        this.marginWidth = marginWidth;
        this.marginHeight = marginHeight;

        viewWidth = screenWidth - this.marginWidth * 2;
        viewHeight = screenHeight - this.marginHeight * 2;

        direction = Direction.NONE;
        isRunning = false;

        scroller = new Scroller(context, new LinearInterpolator());
    }

    public void setPageCarver(PageCarver pageCarver) {
        this.pageCarver = pageCarver;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void startAnimation() {
        if (isRunning) {
            return;
        }

        isRunning = true;
    }

    void setStartPoint(float x, float y) {
        startX = x;
        startY = y;

        lastX = startX;
        lastY = startY;
    }

    void setTouchPoint(float x, float y) {
        lastX = touchX;
        lastY = touchY;

        touchX = x;
        touchY = y;
    }

    void setDirection(Direction direction) {
        this.direction = direction;
    }


    public abstract void onTouchEvent(MotionEvent event);

    public abstract void draw(Canvas canvas);

    public abstract void scroll();

    public abstract void abortAnim();

    public abstract Bitmap getBackBitmap();

    public abstract Bitmap getFrontBitmap();

    public enum Direction {
        NONE(true), NEXT(true), PRE(true), UP(false), DOWN(false);

        public final boolean isHorizontal;

        Direction(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
        }
    }

    public interface PageCarver {
        boolean hasPrePage();

        boolean hasNextPage();

        void cancelPage();

        void requestInvalidate(boolean post);
    }
}
