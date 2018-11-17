package sjtu.yhapter.reader.widget.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import sjtu.yhapter.reader.model.Constants;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public abstract class HorizontalPageAnim extends PageAnimation {
    protected static final int CANCEL_SENSITIVITY = 0;

    protected Bitmap currBitmap;
    protected Bitmap nextBitmap;

    protected boolean isCancel = false;

    private int moveX = 0;
    private int moveY = 0;
    private boolean isMoving = false;
    private boolean isNext = false;
    private boolean noNext = false;

    public HorizontalPageAnim(Context context, int w, int h) {
        this(context, w, h, 0, 0);
    }

    public HorizontalPageAnim(Context context, int w, int h, int marginWidth, int marginHeight) {
        super(context, w, h, marginWidth, marginHeight);

        currBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        nextBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
    }

    // TODO 看能不能封装到内部来，换页可以不暴露给上层
    public void changePage() {
        Bitmap bitmap = currBitmap;
        currBitmap = nextBitmap;
        nextBitmap = bitmap;
    }

    public abstract void drawStatic(Canvas canvas);

    public abstract void drawMove(Canvas canvas);

    @Override
    public void onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        setTouchPoint(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = 0;
                moveY = 0;

                isMoving = false;
                noNext = false;
                isNext = false;
                isRunning = false;
                isCancel = false;

                setStartPoint(x, y);
                abortAnim();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoving)
                    isMoving = Math.abs(startX - x) > Constants.SLOP;

                if (isMoving) {
                    if (moveX == 0 && moveY == 0) {
                        if (x - startX > 0) {
                            isNext = false;
                            boolean hasPrev = pageCarver.hasPrePage();
                            setDirection(Direction.PRE);

                            if (!hasPrev) {
                                noNext = true;
                                return;
                            }
                        } else {
                            isNext = true;
                            boolean hasNext = pageCarver.hasNextPage();
                            setDirection(Direction.NEXT);

                            if (!hasNext) {
                                noNext = true;
                                return;
                            }
                        }
                    } else {
                        if (isNext)
                            isCancel = x - moveX > CANCEL_SENSITIVITY;
                        else
                            isCancel = moveX - x > CANCEL_SENSITIVITY;
                    }
                    moveX = x;
                    moveY = y;
                    isRunning = true;
                    pageCarver.requestInvalidate(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoving) {
                    // if is's a click action
                    isNext = x >= screenWidth / 2;
                    if (isNext) {
                        boolean hasNext = pageCarver.hasNextPage();
                        setDirection(Direction.NEXT);

                        if (!hasNext) {
                            noNext = true;
                            return;
                        }
                    } else {
                        boolean hasPrev = pageCarver.hasPrePage();
                        setDirection(Direction.PRE);

                        if (!hasPrev) {
                            noNext = true;
                            return;
                        }
                    }
                }

                if (isCancel)
                    pageCarver.cancelPage();

                if (!noNext) {
                    // finish the remaining animation
                    startAnimation();
                    pageCarver.requestInvalidate(false);
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isRunning) {
            drawMove(canvas);
        } else {
            if (isCancel) {
                nextBitmap = currBitmap.copy(Bitmap.Config.RGB_565, true);
            }
            drawStatic(canvas);
        }
    }

    @Override
    public void scroll() {
        if (scroller.computeScrollOffset()) {
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();

            setTouchPoint(x, y);

            if (scroller.getFinalX() == x && scroller.getFinalY() == y) {
                isRunning = false;
            }
            pageCarver.requestInvalidate(false);
        }
    }

    @Override
    public void abortAnim() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
            isRunning = false;
            setTouchPoint(scroller.getFinalX(), scroller.getFinalY());
            pageCarver.requestInvalidate(false);
        }
    }

    @Override
    public Bitmap getFrontBitmap() {
        return nextBitmap;
    }

    @Override
    public Bitmap getBackBitmap() {
        return nextBitmap;
    }
}
