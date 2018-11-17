package sjtu.yhapter.reader.widget.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.widget.animation.CoverPageAnim;
import sjtu.yhapter.reader.widget.animation.HorizontalPageAnim;
import sjtu.yhapter.reader.widget.animation.NonePageAnim;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

@SuppressWarnings("unchecked")
public class ReaderView extends BaseReaderView {
    protected int index;
    protected PageAdapter pageAdapter;

    private RectF centerRect;
    private boolean isLastMovingNext;

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        index = 0;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean canTouch() {
                return true;
            }

            @Override
            public boolean onClick(int x, int y) {
                if (centerRect == null) {
                    centerRect = new RectF(viewWidth * 0.2f, viewHeight * 0.333f,
                            viewWidth * 0.8f, viewHeight * 0.666f);
                }
                if (centerRect.contains(x, y)) {
                    LogUtil.log(ReaderView.this, "onClick: " + x + ", " + y);
                    return true;
                }

                return false;
            }

            @Override
            public boolean onLongClickDown(int x, int y) {
                LogUtil.log(ReaderView.this, "onLongClickDown: " + x + ", " + y);
                return true;
            }

            @Override
            public void onLongClickMove(int x, int y) {
                LogUtil.log(ReaderView.this, "onLongClickMove: " + x + ", " + y);
            }

            @Override
            public void onLongClickUp(int x, int y) {
                LogUtil.log(ReaderView.this, "onLongClickUp: " + x + ", " + y);
            }
        });
    }
    
    @Override
    public boolean hasPrePage() {
        if (pageAdapter == null)
            return false;

        index--;
        if (index >= 0 && index < pageAdapter.getCount()) {
            if (pageAnimation instanceof HorizontalPageAnim)
                ((HorizontalPageAnim) pageAnimation).changePage();

            isLastMovingNext = false;
            pageAdapter.draw(index, new Canvas(pageAnimation.getFrontBitmap()));
            return true;
        } else {
            index++;
            return false;
        }
    }

    @Override
    public boolean hasNextPage() {
        if (pageAdapter == null)
            return false;

        index++;
        if (index >= 0 && index < pageAdapter.getCount()) {
            if (pageAnimation instanceof HorizontalPageAnim)
                ((HorizontalPageAnim) pageAnimation).changePage();

            isLastMovingNext = true;
            pageAdapter.draw(index, new Canvas(pageAnimation.getFrontBitmap()));
            return true;
        } else {
            index--;
            return false;
        }
    }

    @Override
    public void cancelPage() {
        index = isLastMovingNext ? index - 1 : index + 1;
    }

    @Override
    public void prepareCurrPage() {
        if (pageAdapter == null)
            return;

        if (index >= 0 && index < pageAdapter.getCount()) {
            pageAdapter.draw(index, new Canvas(pageAnimation.getFrontBitmap()));
        }
    }

    public void setAnimation(PageAnimationMode mode) {
        switch (mode) {
            case COVER:
                pageAnimation = new CoverPageAnim(getContext(), viewWidth, viewHeight);
                setPageAnimation(pageAnimation);
                break;
            case NONE:
            default:
                pageAnimation = new NonePageAnim(getContext(), viewWidth, viewHeight);
                setPageAnimation(pageAnimation);
                break;
        }
    }

    public void setPageAdapter(PageAdapter pageAdapter) {
        this.pageAdapter = pageAdapter;
    }
}
