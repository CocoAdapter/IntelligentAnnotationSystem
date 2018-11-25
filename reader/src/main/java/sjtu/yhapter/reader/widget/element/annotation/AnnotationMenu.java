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

/**
 * Created by CocoAdapter on 2018/11/21.
 */

public class AnnotationMenu extends PopupWindow {
    private View contentView;
    private Button btn;

    private long bookId;
    private long chapterId;
    private String content;
    private long startIndex;
    private long endIndex;

    private Annotation annotation;

    public AnnotationMenu(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.widget_annotation_menu, null);
        setContentView(contentView);

        btn = contentView.findViewById(R.id.btn);

        setHeight((int) context.getResources().getDimension(R.dimen.annotation_menu_height));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        initListener();

        this.content = content;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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
            if (i == R.id.btn) {
                annotation = new Annotation();
                annotation.setBookId(bookId);
                annotation.setChapterId(chapterId);
                annotation.setContent(content);
                annotation.setStartIndex(startIndex);
                annotation.setEndIndex(endIndex);
                // TODO
                LogUtil.log(this, bookId + ", " + chapterId + ", " + content + ", " + startIndex + ", " + endIndex);
                dismiss();
            }
        };
        btn.setOnClickListener(ocl);
    }
}
