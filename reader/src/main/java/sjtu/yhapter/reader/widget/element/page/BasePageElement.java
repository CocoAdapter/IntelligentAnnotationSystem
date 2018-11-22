package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.RectF;

import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public abstract class BasePageElement {
    protected RectF rectF;

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public int getWidth() {
        return (int) rectF.width();
    }

    public int getHeight() {
        return (int) rectF.height();
    }

    public abstract void draw(Canvas canvas, Object object);
}
