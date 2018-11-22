package sjtu.yhapter.reader.widget.element.annotation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import sjtu.yhapter.reader.model.LineData;
import sjtu.yhapter.reader.model.PageData;
import sjtu.yhapter.reader.model.PointChar;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public class TextSelectorElement {
    protected Context context;

    protected PageData currPage;


    protected Paint erasePaint;
    protected Paint endPointPaint;
    protected Paint selectTextPaint;
    protected Path path;

    protected boolean isMovingLeftUp;
    protected PointChar lastChar;
    protected PointChar currChar;
    protected List<LineData> selectLines;

    public TextSelectorElement(Context context) {
        this.context = context;

        erasePaint = new Paint();
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        endPointPaint = new Paint();
        endPointPaint.setAntiAlias(true);
//        endPointPaint.setTextSize(ScreenUtil.dpToPx(15));
        endPointPaint.setColor(Color.RED);

        selectTextPaint = new Paint();
        selectTextPaint.setAntiAlias(true);
        selectTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//        selectTextPaint.setTextSize(ScreenUtil.dpToPx(15));
        selectTextPaint.setColor(Color.parseColor("#77FADB08"));

        path = new Path();
    }

    public void setCurrPage(PageData currPage) {
        this.currPage = currPage;

        clear(null);
    }

    public boolean onLongClickEnter(int x, int y) {
        isMovingLeftUp = false;

        PointChar pc = getSelectedChar(x, y);
        if (pc != null) {
            lastChar = pc;
            currChar = pc;
            if (selectLines == null)
                selectLines = new ArrayList<>();
            return true;
        } else
            return false;
    }

    public void onLongClickMove(int x, int y) {
        // TODO 如果反向，需要记录下来并 clear掉已有的selectLine
        // TODO 这个时候，选中的char已经减少
        PointChar pc = getSelectedChar(x, y);
        if (pc != null) {
            currChar = pc;

            boolean lastMovingLeftUp = isMovingLeftUp;
            isMovingLeftUp = pc.topLeft.x < lastChar.topLeft.x || pc.topLeft.y < lastChar.topLeft.y;
            if (lastMovingLeftUp != isMovingLeftUp && selectLines != null)
                selectLines.clear();
        }

//        LogUtil.log(this, "curr: " + currChar.c + "last: " + lastChar.c);
    }

    public String onLongClickUp(int x, int y) {
        return null;
    }

    public void draw(Canvas canvas) {
        if (currChar == null || lastChar == null)
            return;

        drawSelectLines(canvas);
//        if (lastChar != null) {
//            path.reset();
//            path.moveTo(lastChar.topLeft.x, lastChar.topLeft.y);
//            path.lineTo(lastChar.topRight.x , lastChar.topRight.y);
//            path.lineTo(lastChar.bottomRight.x , lastChar.bottomRight.y);
//            path.lineTo(lastChar.bottomLeft.x , lastChar.bottomLeft.y);
//            canvas.drawPath(path, selectTextPaint);
//        }
    }

    public void clear(Canvas canvas) {
        if (selectLines != null)
            selectLines.clear();
        lastChar = null;
        currChar = null;

        if (canvas != null)
            canvas.drawRect(canvas.getClipBounds(), erasePaint);
    }

    private PointChar getSelectedChar(int x, int y) {
        for (LineData lineData : currPage.lines) {
            for (PointChar pc : lineData.getChars()) {
                if (y > pc.bottomLeft.y)
                    break; // next lines
                if (x >= pc.bottomLeft.x && x <= pc.bottomRight.x)
                    return pc;
            }
        }
        return null;
    }

    private void getSelectLines() {
        boolean isForStartChar = true;
        boolean isEnded = false;

        PointChar startChar, endChar;
        if (isMovingLeftUp) {
            startChar = currChar;
            endChar = lastChar;
        } else {
            startChar = lastChar;
            endChar = currChar;
        }

        for (LineData ld : currPage.lines) {
            LineData selectLine = new LineData();
            for (PointChar pc : ld.getChars()) {
                if (isForStartChar && pc == startChar) {
                    isForStartChar = false;
                    selectLine.append(pc);
                    if (pc == endChar) {
//                        selectLines.add(selectLine);
                        isEnded = true;
                        break;
                    }
                } else if (!isForStartChar) {
                    selectLine.append(pc);
                    if (pc == endChar) {
//                        selectLines.add(selectLine);
                        isEnded = true;
                        break;
                    }
                }
            }
            if (!selectLine.getChars().isEmpty())
                selectLines.add(selectLine);
            if (isEnded)
                return;
        }
    }

    private void drawSelectLines(Canvas canvas) {
        getSelectLines();
        if (selectLines.isEmpty())
            LogUtil.log(this, "empty");
        for (LineData ld : selectLines) {
            LogUtil.log(this, "ld: " + ld.content());
            PointChar pcStart = ld.getChars().get(0);
            PointChar pcEnd = ld.getChars().get(ld.getChars().size() - 1);

            RectF rect = new RectF(pcStart.topLeft.x, pcStart.topLeft.y,
                    pcEnd.bottomRight.x, pcEnd.bottomRight.y);
            canvas.drawRect(rect, selectTextPaint);
        }
    }

//    private Region getSelectRegion(Path path) {
//        Region region = new Region();
//        RectF rectF = new RectF();
//        path.computeBounds(rectF, true);
//        region.setPath(path, )
//        return region;
//    }
}
