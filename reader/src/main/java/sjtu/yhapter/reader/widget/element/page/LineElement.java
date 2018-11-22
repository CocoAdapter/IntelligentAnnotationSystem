package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;

import sjtu.yhapter.reader.model.LineData;
import sjtu.yhapter.reader.model.PointChar;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.model.PageData;

/**
 * Created by CocoAdapter on 2018/11/17.
 */

public class LineElement extends BasePageElement {
    protected TextPaint linePaint;

    public LineElement() {
        linePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setTextSize(ScreenUtil.spToPx(18));
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        PageData pageData = (PageData) object;
        Paint.FontMetrics metrics = linePaint.getFontMetrics();
        float baseline = (int) rectF.top - metrics.top;
        String str;
        for (int i = 0; i < pageData.lines.size(); i++) {
            LineData lineData = pageData.lines.get(i);
            str = lineData.content();
            canvas.drawText(str, rectF.left, baseline, linePaint);

            // cal char pos
            float topPos = baseline + metrics.ascent;
            float bottomPos = baseline + metrics.descent;
            float leftPos = rectF.left;
            float rightPos = leftPos;
            for (PointChar pc : lineData.getChars()) {
                rightPos += pc.width;
                pc.topLeft = new Point((int) leftPos, (int) topPos);
                pc.bottomLeft = new Point((int) leftPos, (int) bottomPos);
                pc.topRight = new Point((int) rightPos, (int) topPos);
                pc.bottomRight = new Point((int) rightPos, (int) bottomPos);
                leftPos += pc.width;
            }

            baseline += getLineHeight();
        }
    }

    public int getLineHeight() {
        return (int) linePaint.getTextSize();
    }

    public int getWordCountOfLine(String str) {
        return linePaint.breakText(str, true, rectF.width(), null);
    }

    public LineData measureLineData(String str) {
        int count = getWordCountOfLine(str);

        LineData lineData = new LineData();
        char[] tem = new char[1];
        for (int i = 0; i < count; i++) {
            PointChar pc = new PointChar();
            pc.c = str.charAt(i);
            pc.index = i;
            tem[0] = pc.c;
            pc.width = linePaint.measureText(tem, 0, 1);
            lineData.append(pc);
        }
        return lineData;
    }
}
