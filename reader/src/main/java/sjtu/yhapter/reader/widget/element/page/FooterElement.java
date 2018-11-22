package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import sjtu.yhapter.reader.model.PageData;
import sjtu.yhapter.reader.util.ScreenUtil;

/**
 * Created by CocoAdapter on 2018/11/20.
 */

public class FooterElement extends BasePageElement {
    private TextPaint pageNumPainter;

    private int totalPageNum;

    public FooterElement() {
        pageNumPainter = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        pageNumPainter.setColor(Color.GRAY);
        pageNumPainter.setTextSize(ScreenUtil.spToPx(12));
    }

    public void setTotalPageNum(int num) {
        totalPageNum = num;
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        PageData pageData = (PageData) object;
        String str = (pageData.position + 1) + " / " + totalPageNum;

        Paint.FontMetrics metrics = pageNumPainter.getFontMetrics();

        int baseline = (int) (rectF.bottom + rectF.top - metrics.bottom - metrics.top) / 2;
        canvas.drawText(str, rectF.right - pageNumPainter.measureText(str), baseline, pageNumPainter);
    }
}
