package sjtu.yhapter.reader.widget.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import sjtu.yhapter.reader.model.PageData;

/**
 * Created by CocoAdapter on 2018/11/15.
 */

public class PageElement {
    protected Canvas canvas;

    protected TextPaint textPaint;

    public PageElement() {
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw(PageData pageData) {
        canvas.drawColor(pageData.position() % 2 == 0 ? Color.MAGENTA : Color.CYAN);

        String text = "index: " + pageData.position();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, (1080 - textWidth)/ 2, (1920 - textHeight) / 2, textPaint);
    }
}
