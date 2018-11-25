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
    private View parentView;
    private Button btn;

    public AnnotationMenu(Context context, View parentView) {
        this.parentView = parentView;
        contentView = LayoutInflater.from(context).inflate(R.layout.widget_annotation_menu, null);
        setContentView(contentView);

        btn = contentView.findViewById(R.id.btn);

        setHeight((int) context.getResources().getDimension(R.dimen.annotation_menu_height));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setOutsideTouchable(true);
//        setTouchable(true);

        initListener();
    }

    public void setAnnotation(PageData page, String content, long startIndex, long endIndex) {
        LogUtil.log(this, page.bookId + ", " + page.chapterId + ", " + content + ", " + startIndex + ", " + endIndex);
    }

    private void initListener() {
        View.OnClickListener ocl = v -> {
            int i = v.getId();
            if (i == R.id.btn) {
                // 确定要画线，不要删除
                // TODO
                dismiss();
            }
        };
        btn.setOnClickListener(ocl);
    }
}
