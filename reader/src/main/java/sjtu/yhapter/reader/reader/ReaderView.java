package sjtu.yhapter.reader.reader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.animation.PageAnimationMode;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.page.annotation.AnnotationType;
import sjtu.yhapter.reader.page.annotation.IdeaDialog;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.animation.CoverPageAnim;
import sjtu.yhapter.reader.animation.HorizontalPageAnim;
import sjtu.yhapter.reader.animation.NonePageAnim;
import sjtu.yhapter.reader.page.annotation.AnnotationMenu;
import sjtu.yhapter.reader.page.annotation.TextSelectorElement;
import sjtu.yhapter.reader.page.PageElement;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public class ReaderView extends BaseReaderView implements BaseReaderView.OnTouchListener, AnnotationMenu.AnnotationListener {
    protected TextSelectorElement textSelector;
    protected PageElement pageElement;
    protected AnnotationMenu annotationMenu;
    protected IdeaDialog ideaDialog;

    protected OnClickListener onClickListener;

    private RectF centerRect;

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        pageElement = new PageElement(this);
        textSelector = new TextSelectorElement(getContext());
        annotationMenu = new AnnotationMenu(getContext());
        annotationMenu.setAnnotationListener(this);
        annotationMenu.setOnDismissListener(() -> {
            annotationMenu.setAnnotation(null);
            Canvas canvas = new Canvas(pageAnimation.getSurfaceBitmap());
            textSelector.clear(canvas);
            postInvalidate();
        });
        ideaDialog = new IdeaDialog(context, (Activity) context);
        canTouch = true;
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        pageElement.onSizeChanged(w, h , ScreenUtil.dpToPx(12), ScreenUtil.dpToPx(18));
        // from portrait to landscape will trigger refreshing curr page, auto
        prepareCurrPage();
    }

    @Override
    public boolean hasPrePage() {
        if (pageElement == null || !pageElement.hasPrePage())
            return false;

        if (pageAnimation instanceof HorizontalPageAnim)
            ((HorizontalPageAnim) pageAnimation).changePage();
        pageElement.drawPrePage(new Canvas(pageAnimation.getFrontBitmap()));

        textSelector.setCurrPage(pageElement.getCurrPage());
        textSelector.draw(new Canvas(pageAnimation.getSurfaceBitmap()));
        return true;
    }

    @Override
    public boolean hasNextPage() {
        if (pageElement == null || !pageElement.hasNextPage())
            return false;

        if (pageAnimation instanceof HorizontalPageAnim)
            ((HorizontalPageAnim) pageAnimation).changePage();

        pageElement.drawNextPage(new Canvas(pageAnimation.getFrontBitmap()));

        textSelector.setCurrPage(pageElement.getCurrPage());
        textSelector.draw(new Canvas(pageAnimation.getSurfaceBitmap()));
        return true;
    }

    @Override
    public void cancelPage() {
        pageElement.cancelPage();
        textSelector.setCurrPage(pageElement.getCurrPage());
    }

    @Override
    public void prepareCurrPage() {
        if (pageElement == null)
            return;

        textSelector.setCurrPage(pageElement.getCurrPage());
        pageElement.drawCurrPage(new Canvas(pageAnimation.getFrontBitmap()));
        textSelector.draw(new Canvas(pageAnimation.getSurfaceBitmap()));
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

    public PageElement getPageElement() {
        return pageElement;
    }

    public void setUserId(long uid) {
        App.USER_ID = uid;
    }

    @Override
    public boolean canTouch() {
        // TODO 按理说这里应该放行，让baseReaderView去处理。但现阶段发现放行会偶尔出现page显示错页，肯定哪里没写安全
        // TODO 现阶段先按这个跑起来，毕竟作业要紧
//        if (pageAnimation.isRunning())
//            return false;
        if (onClickListener != null && !onClickListener.canTouch())
            return false;

        if (annotationMenu != null && annotationMenu.isShowing()) {
            annotationMenu.dismiss();
            return false;
        }
        return true;
    }

    @Override
    public boolean onClick(int x, int y) {
        Annotation annotation = pageElement.checkIfAnnotation(x, y);
        if (annotation != null) {
            if (AnnotationType.valueOf(annotation.getType()) == AnnotationType.IDEA) {
                ideaDialog.setAnnotation(annotation);
                ideaDialog.setFeedback(pageElement.getFeedback(annotation.getId()));
                ideaDialog.show();
            } else {
                annotationMenu.setAnnotation(annotation);
                annotationMenu.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
            }

            return true;
        }

        if (onClickListener != null) {
            if (centerRect == null)
                centerRect = new RectF(viewWidth * 0.2f, viewHeight * 0.333f, viewWidth * 0.8f, viewHeight * 0.666f);
            if (centerRect.contains(x, y)) {
                onClickListener.onCenterClick();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onLongClickDown(int x, int y) {
        Annotation annotation = pageElement.checkIfAnnotation(x, y);
        if (annotation != null) {
            annotationMenu.setAnnotation(annotation);
            annotationMenu.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
            return true;
        }

        if (textSelector.onLongClickEnter(x, y)) {
            Canvas canvas = new Canvas(pageAnimation.getSurfaceBitmap());
            textSelector.draw(canvas);
            postInvalidate();
            return true;
        }

        return false;
    }

    @Override
    public void onLongClickMove(int x, int y) {
        textSelector.onLongClickMove(x, y);
        Canvas canvas = new Canvas(pageAnimation.getSurfaceBitmap());
        textSelector.draw(canvas);
        postInvalidate();
    }

    @Override
    public void onLongClickUp(int x, int y) {
        String selectContent = textSelector.onLongClickUp(x, y);
        final long startIndex = textSelector.getStartCharIndex();
        final long endIndex = textSelector.getEndCharIndex();

        annotationMenu.setBookId(pageElement.getCurrPage().bookId)
                .setChapterId(pageElement.getCurrPage().chapterId)
                .setContent(selectContent)
                .setStartIndex(startIndex)
                .setEndIndex(endIndex);
        annotationMenu.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
    }

    @Override
    public void onAnnotationDraw(Annotation annotation) {
        // erase the selection
        Canvas canvas = new Canvas(pageAnimation.getSurfaceBitmap());
        textSelector.clear(canvas);
        // draw annotation
        canvas = new Canvas(pageAnimation.getFrontBitmap());
        if (annotation != null) {
            LogUtil.log(annotation.toString());
            pageElement.addAnnotation(annotation);
            pageElement.drawCurrPage(canvas); // draw line
        }

        postInvalidate();
    }

    @Override
    public void onAnnotationDel(Annotation annotation) {
        // erase the selection
        Canvas canvas = new Canvas(pageAnimation.getSurfaceBitmap());
        textSelector.clear(canvas);
        // del the annotation
        canvas = new Canvas(pageAnimation.getFrontBitmap());
        if (annotation != null) {
            pageElement.delAnnotation(annotation);
            pageElement.drawCurrPage(canvas); // refresh
        }

        postInvalidate();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        boolean canTouch();

        void onCenterClick();
    }
}
