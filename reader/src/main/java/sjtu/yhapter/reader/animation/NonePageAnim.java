package sjtu.yhapter.reader.animation;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by CocoAdapter on 2018/11/15.
 */

public class NonePageAnim extends HorizontalPageAnim {
    public NonePageAnim(Context context, int w, int h) {
        this(context, w, h, 0, 0);
    }

    public NonePageAnim(Context context, int w, int h, int marginWidth, int marginHeight) {
        super(context, w, h, marginWidth, marginHeight);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel)
            canvas.drawBitmap(currBitmap, 0, 0, null);
        else
            canvas.drawBitmap(nextBitmap, 0, 0, null);
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (isCancel)
            canvas.drawBitmap(currBitmap, 0, 0, null);
        else
            canvas.drawBitmap(nextBitmap, 0, 0, null);
    }

    @Override
    public void startAnimation(){
        // no animation
    }
}
