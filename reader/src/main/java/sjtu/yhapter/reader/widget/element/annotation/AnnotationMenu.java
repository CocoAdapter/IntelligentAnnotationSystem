package sjtu.yhapter.reader.widget.element.annotation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.model.Annotation;
import sjtu.yhapter.reader.model.PageData;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;

/**
 * Created by CocoAdapter on 2018/11/21.
 */

public class AnnotationMenu extends PopupWindow {
    private final static int X_OFFSET = 10; // in dp

    private View contentView;
    private View btn;

    private long bookId;
    private long chapterId;
    private String content;
    private long startIndex;
    private long endIndex;

    private Annotation annotation;

    public AnnotationMenu(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.widget_annotation_menu, null);
        setContentView(contentView);

        btn = contentView.findViewById(R.id.btn_drawline);

        setHeight((int) context.getResources().getDimension(R.dimen.annotation_menu_height));
        setWidth((int) context.getResources().getDimension(R.dimen.annotation_menu_width));

        initListener();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        int xPadding = ScreenUtil.dpToPx(X_OFFSET);
        int total = x + getWidth() + xPadding;
        int parentWidth = parent.getWidth();
        if (total >= parentWidth) {
            x = parentWidth - xPadding - getWidth();
        }
        super.showAtLocation(parent, gravity, x, y);
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

    public Annotation getAnnotation() {
        return annotation;
    }

    private void initListener() {
        View.OnClickListener ocl = v -> {
            int i = v.getId();
            if (i == R.id.btn_drawline) {
                annotation = new Annotation();
                annotation.setBookId(bookId);
                annotation.setChapterId(chapterId);
                annotation.setContent(content);
                annotation.setStartIndex(startIndex);
                annotation.setEndIndex(endIndex);
                // TODO
                LogUtil.log(this, bookId + ", " + chapterId + ", " + content + ", " + startIndex + ", " + endIndex);

                // TODO 弹出二级菜单, 不能dismiss
                dismiss();
            }
        };
        btn.setOnClickListener(ocl);
    }
}
