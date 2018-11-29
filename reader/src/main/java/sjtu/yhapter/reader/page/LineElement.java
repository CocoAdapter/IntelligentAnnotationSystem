package sjtu.yhapter.reader.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.model.pojo.LineData;
import sjtu.yhapter.reader.model.pojo.PageData;
import sjtu.yhapter.reader.model.pojo.PointChar;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.page.annotation.AnnotationType;

/**
 * Created by CocoAdapter on 2018/11/17.
 */

public class LineElement extends BasePageElement {
    protected final static float WAVE_HEIGHT = ScreenUtil.dpToPx(5);
    protected final static float WAVE_WIDTH = ScreenUtil.dpToPx(5);

    protected TextPaint linePaint;
    // 这个从上层传过来，分页的时候的读入
    protected List<Annotation> annotations; // needs to be sorted by startIndex

    protected Paint annotationPaint;

    private long chapterIndex;

    private int lineHeight;
    private float baseline;

    private Path path;

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

    public void setAnnotations(Collection<Annotation> annotations) {
        if (annotations != null)
            this.annotations = new ArrayList<>(annotations);
    }

    /**
     * check if a annotation exists at (x, y), and return it if it does.
     * WARNING: the test may not be sufficient
     *
     * @param x x of MotionEvent
     * @param y y of MotionEvent
     * @param pageData pageData with annotations on
     * @return the responding annotation representation if it is, null otherwise
     */
    protected Annotation checkIfAnnotation(int x, int y, PageData pageData) {
        if (annotations == null || annotations.isEmpty() || pageData == null)
            return null;

        for (Annotation annotation : annotations) {
            Point top = null, bottom = null;
            boolean isMulti = false;
            boolean isForStart = true;
            for (LineData ld : pageData.lines) {
                long start = ld.getChars().get(0).chapterIndex;
                long end = ld.getChars().get(ld.getChars().size() - 1).chapterIndex;
                boolean isInCurrLine = annotation.getStartIndex() >= start && annotation.getStartIndex() <= end;
                if (isForStart && isInCurrLine) {
                    int startOffset = (int) (annotation.getStartIndex() - start);
                    PointChar pcStart = ld.getChars().get(startOffset);
                    top = new Point(pcStart.topLeft.x, pcStart.topLeft.y);
                    isForStart = false;

                    if (annotation.getEndIndex() <= end) {
                        // only one line
                        int endOffset = (int) (annotation.getEndIndex() - start);
                        PointChar pcEnd = ld.getChars().get(endOffset);
                        bottom = new Point(pcEnd.bottomRight.x, pcEnd.bottomRight.y);
                        break;
                    } else
                        isMulti = true;
                } else if (!isForStart){
                    boolean isEnded = annotation.getEndIndex() <= end;
                    if (isEnded) {
                        PointChar pcEnd = ld.getChars().get((int) (annotation.getEndIndex() - start));
                        bottom = new Point(pcEnd.bottomRight.x, pcEnd.bottomRight.y);
                        break;
                    }
                }
            }

            if (top != null) {
                // found an annotation, safe-coding here cause it's certain
                if (isMulti) {
                    // bottom should not be null
                    assert bottom != null;
                    if (y < top.y || y > bottom.y)
                        continue;
                    if (y < top.y + getLineHeight() && x < top.x)
                        continue;
                    if (y > bottom.y - getLineHeight() && x > bottom.x)
                        continue;

                    return annotation;
                } else {
                    // only one line
                    if (x >= top.x && x <= bottom.x && y >= top.y && y <= bottom.y)
                        return annotation;
                }
            }
        }
        // no match
        return null;
    }

    private void drawAnnotations(Canvas canvas, PageData pageData) {
        if (annotations == null || annotations.isEmpty())
            return;

        for (Annotation annotation : annotations) {
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
                        drawAnnotation(canvas, pcStart, pcEnd, AnnotationType.valueOf(annotation.getType()), annotationPaint);
                        break;
                    } else {
                        PointChar pcEnd = ld.getChars().get(ld.getChars().size() - 1);
                        drawAnnotation(canvas, pcStart, pcEnd, AnnotationType.valueOf(annotation.getType()), annotationPaint);
                    }
                } else if (!isForStart){
                    boolean isEnded = annotation.getEndIndex() <= end;
                    PointChar pcStart = ld.getChars().get(0);
                    PointChar pcEnd = isEnded ? ld.getChars().get((int) (annotation.getEndIndex() - start))
                            : ld.getChars().get(ld.getChars().size() - 1);
                    drawAnnotation(canvas, pcStart, pcEnd, AnnotationType.valueOf(annotation.getType()), annotationPaint);
                    if (isEnded)
                        break;
                }
            }
        }
    }

    private void drawAnnotation(Canvas canvas, PointChar start, PointChar end, AnnotationType type, Paint paint) {
        switch (type) {
            case FILL:
                annotationPaint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#77FADB08"));
                canvas.drawRect(start.topLeft.x, start.topLeft.y, end.bottomRight.x, end.bottomRight.y, paint);
                break;
            case NORMAL:
                annotationPaint.setStyle(Paint.Style.STROKE);
                annotationPaint.setStrokeWidth(ScreenUtil.dpToPx(1));
                paint.setColor(Color.RED);
                canvas.drawLine(start.bottomLeft.x, start.bottomLeft.y,
                        end.bottomRight.x, end.bottomRight.y, annotationPaint);
                break;
            case WAVE:
                annotationPaint.setStyle(Paint.Style.STROKE);
                annotationPaint.setStrokeWidth(ScreenUtil.dpToPx(1));
                paint.setColor(Color.BLUE);
                Path path = getWaveLine(start, end);
                canvas.drawPath(path, paint);
                break;
        }
    }

    private Path getWaveLine(PointChar start, PointChar end) {
        if (path == null)
            path = new Path();
        path.reset();

        int quadrant = 0;
        float y = start.bottomLeft.y + WAVE_HEIGHT / 2;
        float yc = y - WAVE_HEIGHT / 2;

        float xl = start.bottomLeft.x;
        float xc = xl + WAVE_WIDTH / 2;
        float xr = xl + WAVE_WIDTH;
        while (xr <= end.bottomRight.x) {
            path.moveTo(xl, y);
            path.quadTo(xc, yc,xr, y);

            xl += WAVE_WIDTH;
            xc += WAVE_WIDTH;
            xr += WAVE_WIDTH;

            quadrant++;
            if (quadrant % 2 != 0)
                yc = y + WAVE_HEIGHT / 2;
            else
                yc = y - WAVE_HEIGHT / 2;
        }
        return path;
    }
}
