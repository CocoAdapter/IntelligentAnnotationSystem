package sjtu.yhapter.reader.page.annotation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Date;

import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.widget.ImageTextView;

/**
 * Created by CocoAdapter on 2018/11/21.
 */

public class AnnotationMenu extends PopupWindow {
    private final static int X_OFFSET = 10; // in dp

    private View parentView;
    private Point anchorPoint;
    private ImageTextView btnCopy, btnDrawLine, btnWriteIdea, btnQuery, btnShare;
    private LineTypeMenu lineTypeMenu;
    private WriteIdeaWindow writeIdeaWindow;

    private long bookId;
    private long chapterId;
    private String content;
    private long startIndex;
    private long endIndex;

    private Annotation annotation;
    private AnnotationListener annotationListener;

    public AnnotationMenu(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.widget_anno_menu, null);
        setContentView(contentView);

        lineTypeMenu = new LineTypeMenu(context);
        writeIdeaWindow = new WriteIdeaWindow(context);

        btnCopy = contentView.findViewById(R.id.btn_copy);
        btnDrawLine = contentView.findViewById(R.id.btn_drawline);
        btnWriteIdea = contentView.findViewById(R.id.btn_writeidea);
        btnQuery = contentView.findViewById(R.id.btn_query);
        btnShare = contentView.findViewById(R.id.btn_share);

        setHeight((int) context.getResources().getDimension(R.dimen.annotation_menu_height));
        setWidth((int) context.getResources().getDimension(R.dimen.annotation_menu_width));

        initListener();

        setAnimationStyle(R.style.AnnotationMenuAnim);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // reset
        btnDrawLine.setTag(annotation == null);
        btnDrawLine.setText(App.getInstance().getText(annotation == null ?
                R.string.annotation_drawline : R.string.annotation_delline));
        if (annotation == null)
            lineTypeMenu.setSelectIndex(0);
        else {
            switch (AnnotationType.valueOf(annotation.getType())) {
                case NORMAL:
                    lineTypeMenu.setSelectIndex(1);
                    break;
                case WAVE:
                    lineTypeMenu.setSelectIndex(2);
                    break;
                case FILL:
                default:
                    lineTypeMenu.setSelectIndex(0);
                    break;
            }
        }

        int xPadding = ScreenUtil.dpToPx(X_OFFSET);
        int totalX = x + getWidth() + xPadding;
        int parentWidth = parent.getWidth();
        if (totalX >= parentWidth) {
            x = parentWidth - xPadding - getWidth();
        }
        parentView = parent;
        anchorPoint = new Point(x, y);
        // the y pos for showAtLocation is based on whole screen
        super.showAtLocation(parent, gravity, x, y + ScreenUtil.getStatusBarHeight());
    }

    @Override
    public void dismiss() {
        annotation = null; // release
        super.dismiss();
    }

    public AnnotationMenu setBookId(long bookId) {
        this.bookId = bookId;
        return this;
    }

    public AnnotationMenu setChapterId(long chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public AnnotationMenu setContent(String content) {
        this.content = content;
        return this;
    }

    public AnnotationMenu setStartIndex(long startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public AnnotationMenu setEndIndex(long endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public void setAnnotationListener(AnnotationListener annotationListener) {
        this.annotationListener = annotationListener;
    }

    private void initListener() {
        View.OnClickListener ocl = v -> {
            int i = v.getId();
            if (i == R.id.btn_drawline) {
                Boolean isDrawLine = (Boolean) btnDrawLine.getTag();
                isDrawLine = isDrawLine == null ? true : isDrawLine;
                if (isDrawLine) {
                    annotation = new Annotation();
                    annotation.setBookId(bookId);
                    annotation.setChapterId(chapterId);
                    annotation.setContent(content);
                    annotation.setStartIndex(startIndex);
                    annotation.setEndIndex(endIndex);
                    annotation.setDate(new Date());
                    annotation.setUserId(App.USER_ID);
                    annotation.setNote(null);
                    annotation.setType(AnnotationType.FILL.name());
                    // draw Annotation
                    if (annotationListener != null)
                        annotationListener.onAnnotationDraw(annotation);

                    if (parentView != null) {
                        int x = anchorPoint.x + v.getLeft() + v.getWidth() / 2;
                        int y = anchorPoint.y + v.getTop();
                        lineTypeMenu.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y);

                        btnDrawLine.setText(App.getInstance().getText(R.string.annotation_delline));
                        btnDrawLine.setTag(false);
                    }
                } else {
                    if (annotationListener != null && annotation != null) {
                        annotationListener.onAnnotationDel(annotation);
                        dismiss();
                    }
                }
            } else if (i == R.id.btn_writeidea) {
                dismiss();
                // pop another window
                writeIdeaWindow.setReference(content);
                writeIdeaWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
            }
        };
        btnDrawLine.setOnClickListener(ocl);
        btnWriteIdea.setOnClickListener(ocl);
    }

    private class LineTypeMenu extends PopupWindow implements View.OnClickListener{
        private int width;
        private int height;

        private ImageView btnFill, btnNormal, btnWave;

        LineTypeMenu(Context context) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.widget_anno_menu_linetype, null);
            setContentView(contentView);
            btnFill = contentView.findViewById(R.id.btn_fill);
            btnNormal = contentView.findViewById(R.id.btn_normalline);
            btnWave = contentView.findViewById(R.id.btn_waveline);
            initListener();

            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            width = contentView.getMeasuredWidth();
            height = contentView.getMeasuredHeight();

            setTouchable(true);
            setOutsideTouchable(true);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            setAnimationStyle(R.style.AnnotationMenuAnim);
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            x = x - width / 2;
            int margin = (int) App.getInstance().getResources().getDimension(R.dimen.anno_menu_line_margin);
            int statusBarHeight = ScreenUtil.getStatusBarHeight();
            int yOffset = y - height - margin;
            if (yOffset < statusBarHeight) {
                y = y + AnnotationMenu.this.getHeight() + margin;
            } else
                y = yOffset;

            super.showAtLocation(parent, gravity, x, y + statusBarHeight);
        }

        @Override
        public void onClick(View v) {
            v.setSelected(true);
            if (v.getId() == btnFill.getId()) {
                btnNormal.setSelected(false);
                btnWave.setSelected(false);

                if (annotationListener != null && annotation != null) {
                    annotation.setType(AnnotationType.FILL.name());
                    annotationListener.onAnnotationDraw(annotation);
                    LogUtil.log(this, annotation.toString());
                }
            } else if (v.getId() == btnNormal.getId()) {
                btnFill.setSelected(false);
                btnWave.setSelected(false);

                if (annotationListener != null && annotation != null) {
                    annotation.setType(AnnotationType.NORMAL.name());
                    annotationListener.onAnnotationDraw(annotation);
                }
            } else if (v.getId() == btnWave.getId()) {
                btnFill.setSelected(false);
                btnNormal.setSelected(false);

                if (annotationListener != null && annotation != null) {
                    annotation.setType(AnnotationType.WAVE.name());
                    annotationListener.onAnnotationDraw(annotation);
                }
            }
        }

        public void setSelectIndex(int index) {
            View[] tem = new View[]{btnFill, btnNormal, btnWave};
            for (int i = 0; i < tem.length; i++){
                tem[i].setSelected(index == i);
            }
        }

        private void initListener() {
            btnFill.setOnClickListener(this);
            btnNormal.setOnClickListener(this);
            btnWave.setOnClickListener(this);
        }
    }

    private class WriteIdeaWindow extends PopupWindow {
        private EditText et_idea;
        private TextView tv_reference;

        WriteIdeaWindow(Context context) {
            this(context, "");
        }

        WriteIdeaWindow(Context context, String reference) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.widget_anno_menu_writeidea, null);
            setContentView(contentView);

            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

            setTouchable(true);
            setOutsideTouchable(true);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            et_idea = contentView.findViewById(R.id.et_idea);
            tv_reference = contentView.findViewById(R.id.tv_reference);

            et_idea.setFocusable(true);
            et_idea.setFocusableInTouchMode(true);
            et_idea.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
                    // TODO 是否考虑过滤空值
                    dismiss();

                    Annotation annotation = new Annotation();
                    annotation.setBookId(bookId);
                    annotation.setChapterId(chapterId);
                    annotation.setContent(content);
                    annotation.setStartIndex(startIndex);
                    annotation.setEndIndex(endIndex);
                    annotation.setDate(new Date());
                    annotation.setUserId(App.USER_ID);
                    annotation.setNote(et_idea.getText().toString());
                    annotation.setType(AnnotationType.IDEA.name());
                    // draw Annotation
                    if (annotationListener != null)
                        annotationListener.onAnnotationDraw(annotation);
                    return true;
                }
                return false;
            });

            tv_reference.setText(reference);
        }

        void setReference(String reference) {
            tv_reference.setText(reference);
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            Activity activity = (Activity) et_idea.getContext();
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.5f;
            window.setAttributes(lp);

            parent.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) et_idea.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }, 100);

            super.showAtLocation(parent, gravity, x, y);
        }

        @Override
        public void dismiss() {
            Activity activity = (Activity) et_idea.getContext();
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 1.0f;
            window.setAttributes(lp);

            super.dismiss();
        }
    }

    public interface AnnotationListener {
        void onAnnotationDraw(Annotation annotation);

        void onAnnotationDel(Annotation annotation);
    }
}
