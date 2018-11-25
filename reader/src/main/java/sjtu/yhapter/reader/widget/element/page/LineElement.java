package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sjtu.yhapter.reader.model.Annotation;
import sjtu.yhapter.reader.model.LineData;
import sjtu.yhapter.reader.model.PointChar;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.model.PageData;

/**
 * Created by CocoAdapter on 2018/11/17.
 */

public class LineElement extends BasePageElement {
    protected TextPaint linePaint;
    // 这个从上层传过来，分页的时候的读入
    protected List<Annotation> annotations; // needs to be sorted by startIndex

    protected Paint annotationPaint;

    private long chapterIndex;

    private int lineHeight;
    private float baseline;

    public LineElement() {
        linePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setTypeface(Typeface.MONOSPACE);
        linePaint.setTextSize(ScreenUtil.spToPx(18));
        Paint.FontMetrics metrics = linePaint.getFontMetrics();
        lineHeight = (int) (metrics.bottom - metrics.top);

        annotationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        annotationPaint.setColor(Color.MAGENTA);
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        PageData pageData = (PageData) object;
        // draw text
        for (int i = 0; i < pageData.lines.size(); i++) {
            LineData lineData = pageData.lines.get(i);
            String str = lineData.content();
            canvas.drawText(str, rectF.left, lineData.getBaseline(), linePaint);
        }
        // draw annotation
        drawAnnotations(canvas, pageData);
    }

    public int getLineHeight() {
        return lineHeight;
    }

    @Override
    public void setRectF(RectF rectF) {
        super.setRectF(rectF);
        resetBaseLine();
    }

    public void resetBaseLine() {
        Paint.FontMetrics metrics = linePaint.getFontMetrics();
        baseline = (int) rectF.top - metrics.top;
    }

    public int getWordCountOfLine(String str) {
        return linePaint.breakText(str, true, rectF.width(), null);
    }

    public void resetParse() {
        chapterIndex = 0;
    }

    public LineData measureLineData(String str) {
        int count = getWordCountOfLine(str);

        LineData lineData = new LineData();
        lineData.setBaseline(baseline);

        // cal char pos
        Paint.FontMetrics metrics = linePaint.getFontMetrics();
        float topPos = baseline + metrics.ascent;
        float bottomPos = baseline + metrics.descent;
        float leftPos = rectF.left;
        float rightPos = leftPos;

        char[] tem = new char[1];
        for (int i = 0; i < count; i++) {
            PointChar pc = new PointChar();
            pc.chapterIndex = chapterIndex++;
            pc.c = str.charAt(i);
            pc.index = i;
            tem[0] = pc.c;
            pc.width = linePaint.measureText(tem, 0, 1);

            rightPos += pc.width;
            pc.topLeft = new Point((int) leftPos, (int) topPos);
            pc.bottomLeft = new Point((int) leftPos, (int) bottomPos);
            pc.topRight = new Point((int) rightPos, (int) topPos);
            pc.bottomRight = new Point((int) rightPos, (int) bottomPos);
            leftPos += pc.width;

            lineData.append(pc);
        }

        baseline += getLineHeight();
        return lineData;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    private void drawAnnotations(Canvas canvas, PageData pageData) {
        if (annotations == null || annotations.isEmpty())
            return;

        // getSelectLines
        long charStart = pageData.lines.get(0).getChars().get(0).chapterIndex;
        List<PointChar> tem = pageData.lines.get(pageData.lines.size() - 1).getChars();
        long charEnd = tem.get(tem.size() - 1).chapterIndex;

        Collections.sort(annotations, (o1, o2) -> (int) (o1.getStartIndex() - o2.getStartIndex()));

        // search
        for (Annotation annotation : annotations) {
            if (annotation.getStartIndex() < charStart)
                continue;
            if (annotation.getStartIndex() <= charEnd) {
                // draw on curr page, may be partially
                boolean isForStart = true;
                for (LineData ld : pageData.lines) {
                    long start = ld.getChars().get(0).chapterIndex;
                    long end = ld.getChars().get(ld.getChars().size() - 1).chapterIndex;
                    boolean isInCurrLine = annotation.getStartIndex() >= start && annotation.getStartIndex() <= end;
                    if (isForStart && isInCurrLine) {
                        int startOffset = (int) (annotation.getStartIndex() - start);
                        PointChar pcStart = ld.getChars().get(startOffset);
                        isForStart = false;

                        if (annotation.getEndIndex() <= end) {
                            // only one line
                            int endOffset = (int) (annotation.getEndIndex() - start);
                            PointChar pcEnd = ld.getChars().get(endOffset);
                            canvas.drawLine(pcStart.bottomLeft.x, pcStart.bottomLeft.y,
                                    pcEnd.bottomRight.x, pcEnd.bottomRight.y, annotationPaint);
                            break;
                        } else {
                            PointChar pcEnd = ld.getChars().get(ld.getChars().size() - 1);
                            canvas.drawLine(pcStart.bottomLeft.x, pcStart.bottomLeft.y,
                                    pcEnd.bottomRight.x, pcEnd.bottomRight.y, annotationPaint);
                        }
                    } else if (!isForStart){
                        boolean isEnded = annotation.getEndIndex() <= end;
                        PointChar pcStart = ld.getChars().get(0);
                        PointChar pcEnd = isEnded ? ld.getChars().get((int) (annotation.getEndIndex() - start))
                                : ld.getChars().get(ld.getChars().size() - 1);

                        canvas.drawLine(pcStart.bottomLeft.x, pcStart.bottomLeft.y,
                                pcEnd.bottomRight.x, pcEnd.bottomRight.y, annotationPaint);

                        if (isEnded)
                            break;
                    }
                }
            } else
                break;
        }
    }
}
