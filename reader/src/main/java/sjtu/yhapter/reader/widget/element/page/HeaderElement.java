package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.model.PageData;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public class HeaderElement extends BasePageElement {
    protected TextPaint titlePaint;

    public HeaderElement() {
        titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.GRAY);
        titlePaint.setTextSize(ScreenUtil.spToPx(12));
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        PageData pageData = (PageData) object;
        Paint.FontMetrics metrics = titlePaint.getFontMetrics();

        int count = titlePaint.breakText(pageData.title, true, rectF.width(), null);
        // just roughly clip, the actual max length may be longer
        if (count < pageData.title.length())
            pageData.title = pageData.title.substring(0, pageData.title.length() - 3) + "...";

        int baseline = (int) (rectF.bottom + rectF.top - metrics.bottom - metrics.top) / 2;
        canvas.drawText(pageData.title, rectF.left, baseline, titlePaint);
    }
}
