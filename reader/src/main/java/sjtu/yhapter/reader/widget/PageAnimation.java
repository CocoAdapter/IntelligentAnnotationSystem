package sjtu.yhapter.reader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by newbiechen on 17-7-24.
 * 翻页动画抽象类
 */

public abstract class PageAnimation {
    //滑动装置
    protected Scroller mScroller;
    //监听器
    protected PageCarver mListener;
    //移动方向
    protected Direction mDirection = Direction.NONE;

    protected boolean isRunning = false;

    //屏幕的尺寸
    protected int mScreenWidth;
    protected int mScreenHeight;
    //屏幕的间距
    protected int mMarginWidth;
    protected int mMarginHeight;
    //视图的尺寸
    protected int mViewWidth;
    protected int mViewHeight;
    //起始点
    protected float mStartX;
    protected float mStartY;
    //触碰点
    protected float mTouchX;
    protected float mTouchY;
    //上一个触碰点
    protected float mLastX;
    protected float mLastY;

    public PageAnimation(Context context, int w, int h){
        this(context, w, h, 0, 0);
    }

    public PageAnimation(Context context, int w, int h, int marginWidth, int marginHeight){
        mScreenWidth = w;
        mScreenHeight = h;

        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;

        mViewWidth = mScreenWidth - mMarginWidth * 2;
        mViewHeight = mScreenHeight - mMarginHeight * 2;

        mScroller = new Scroller(context, new LinearInterpolator());
    }

    public void setPageCarver(PageCarver mListener) {
        this.mListener = mListener;
    }

    public void setStartPoint(float x, float y){
        mStartX = x;
        mStartY = y;

        mLastX = mStartX;
        mLastY = mStartY;
    }

    public void setTouchPoint(float x,float y){
        mLastX = mTouchX;
        mLastY = mTouchY;

        mTouchX = x;
        mTouchY = y;
    }

    public boolean isRunning(){
        return isRunning;
    }

    /**
     * 开启翻页动画
     */
    public void startAnim(){
        if (isRunning){
            return;
        }
        isRunning = true;
    }

    public void setDirection(Direction direction){
        mDirection = direction;
    }

    public Direction getDirection(){
        return mDirection;
    }

    /**
     * 点击事件的处理
     * @param event
     */
    public abstract boolean dispatchTouchEvent(MotionEvent event);

    /**
     * 绘制图形
     * @param canvas
     */
    public abstract void draw(Canvas canvas);

    /**
     * 滚动动画
     * 必须放在computeScroll()方法中执行
     */
    public abstract void scroll();

    /**
     * 取消动画
     */
    public abstract void abortAnim();

    /**
     * 获取背景板
     * @return
     */
    public abstract Bitmap getBackBitmap();

    /**
     * 获取内容显示版面
     */
    public abstract Bitmap getFrontBitmap();

    public enum Direction {
        NONE(true),NEXT(true), PRE(true), UP(false), DOWN(false);

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
