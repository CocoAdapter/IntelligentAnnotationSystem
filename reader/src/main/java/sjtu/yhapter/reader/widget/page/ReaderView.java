package sjtu.yhapter.reader.widget.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import sjtu.yhapter.reader.model.PageData;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.widget.animation.CoverPageAnim;
import sjtu.yhapter.reader.widget.animation.HorizontalPageAnim;
import sjtu.yhapter.reader.widget.animation.NonePageAnim;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public class ReaderView extends BaseReaderView {
    protected PageLoader pageLoader;
    protected PageElement pageElement;

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        pageLoader = new PageLoader();
        pageElement = new PageElement();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean hasPrePage() {
        PageData pageData = pageLoader.getPrePageData();
        if (pageData == null)
            return false;

        if (pageAnimation instanceof HorizontalPageAnim)
            ((HorizontalPageAnim) pageAnimation).changePage();

        pageElement.setCanvas(new Canvas(pageAnimation.getFrontBitmap()));
        pageElement.draw(pageData);
        return true;
    }

    @Override
    public boolean hasNextPage() {
        PageData pageData = pageLoader.getNextPageData();
        if (pageData == null)
            return false;

        if (pageAnimation instanceof HorizontalPageAnim)
            ((HorizontalPageAnim) pageAnimation).changePage();

        pageElement.setCanvas(new Canvas(pageAnimation.getFrontBitmap()));
        pageElement.draw(pageData);
        return true;
    }

    @Override
    public void cancelPage() {
        LogUtil.log(this, "cancelPage");
        pageLoader.cancelPrepare();
    }

    @Override
    public void prepareCurrPage() {
        PageData pageData = pageLoader.getCurrPageData();
        if (pageData != null) {
            pageElement.setCanvas(new Canvas(pageAnimation.getFrontBitmap()));
            pageElement.draw(pageData);
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
}
