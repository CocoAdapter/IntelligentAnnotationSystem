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
    protected List<Annotation> annotations; // needs to be sorted by startIndex

    protected Paint annotationPaint;

    private long chapterIndex;

    private int lineHeight;
    private float baseline;

    private Annotation annotation;

    public LineElement() {
        linePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setTypeface(Typeface.MONOSPACE);
        linePaint.setTextSize(ScreenUtil.spToPx(18));
        Paint.FontMetrics metrics = linePaint.getFontMetrics();
        lineHeight = (int) (metrics.bottom - metrics.top);

        annotationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        annotationPaint.setColor(Color.MAGENTA);

        annotations = new ArrayList<>();
        // TODO test
        annotation = new Annotation();
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotation.setContent("人的行为可能建立在坚固的岩石上面，也可能建立在潮湿的沼泽之中，但是一过某种程度，我就不管它是建立在什么上面的了。去年秋天我从");
        annotation.setStartIndex(537);
        annotation.setEndIndex(598);
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setContent("身上出现的时候，心理不正常的人很快就会察觉并区抓住不放。由于这个缘故，我上大学的时候就被不公正地指责为小政客，因为我与闻一");
        annotation.setStartIndex(216);
        annotation.setEndIndex(276);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setContent("系的实际创始人却是我祖父的哥哥。他在一八五一年来到这里，买了个替身去参加南北战争，开始做起五金批发生意，也就是我父东今天");
        annotation.setStartIndex(1043);
        annotation.setEndIndex(1102);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setContent("的－－每逢我根据某种明白无误的迹象看出又有一次倾诉衷情在地平线上喷薄欲出的时候，我往往假装睡觉，假装心不在焉，或者装出不怀好");
        annotation.setStartIndex(309);
        annotation.setEndIndex(370);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotations.add(annotation);

        Collections.sort(annotations, (o1, o2) -> (int) (o1.getStartIndex() - o2.getStartIndex()));
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        PageData pageData = (PageData) object;

//        Paint.FontMetrics metrics = linePaint.getFontMetrics();
//        float baseline = (int) rectF.top - metrics.top;

        for (int i = 0; i < pageData.lines.size(); i++) {
            LineData lineData = pageData.lines.get(i);
            String str = lineData.content();
//            canvas.drawText(str, rectF.left, baseline, linePaint);
            canvas.drawText(str, rectF.left, lineData.getBaseline(), linePaint);
        }

        drawAnnotations(canvas, pageData);
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public float getBaseline() {
        return baseline;
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

    private void drawAnnotations(Canvas canvas, PageData pageData) {
        // getSelectLines
        long charStart = pageData.lines.get(0).getChars().get(0).chapterIndex;
        List<PointChar> tem = pageData.lines.get(pageData.lines.size() - 1).getChars();
        long charEnd = tem.get(tem.size() - 1).chapterIndex;

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
