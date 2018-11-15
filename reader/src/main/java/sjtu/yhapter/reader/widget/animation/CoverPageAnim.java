package sjtu.yhapter.reader.widget.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public class CoverPageAnim extends HorizontalPageAnim {
    private Rect srcRect, destRect;
    private GradientDrawable shadowDrawable;

    public CoverPageAnim(Context context, int w, int h) {
        super(context, w, h);

        srcRect = new Rect(0, 0, viewWidth, viewHeight);
        destRect = new Rect(0, 0, viewWidth, viewHeight);

        int[] mBackShadowColors = new int[]{0x66000000, 0x00000000};
        shadowDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel) {
            nextBitmap = currBitmap.copy(Bitmap.Config.RGB_565, true);
            canvas.drawBitmap(currBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(nextBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawMove(Canvas canvas) {
        switch (direction) {
            case NEXT:
                int dis = (int) (viewWidth - startX + touchX);
                if (dis > viewWidth) dis = viewWidth;

                srcRect.left = viewWidth - dis;
                destRect.right = dis;

                canvas.drawBitmap(nextBitmap, 0, 0, null);
                canvas.drawBitmap(currBitmap, srcRect, destRect, null);
                addShadow(dis, canvas);
                break;
            case PRE:
                srcRect.left = (int) (viewWidth - touchX);
                destRect.right = (int) touchX;

                canvas.drawBitmap(currBitmap, 0, 0, null);
                canvas.drawBitmap(nextBitmap, srcRect, destRect, null);
                addShadow((int) touchX, canvas);
                break;
        }
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
        int dx = 0;
        switch (direction) {
            case NEXT:
                if (isCancel) {
                    int dis = (int) ((viewWidth - startX) + touchX);
                    if (dis > viewWidth) dis = viewWidth;

                    dx = viewWidth - dis;
                } else
                    dx = (int) -(touchX + (viewWidth - startX));
                break;
            case PRE:
                dx = isCancel ? (int) -touchX : (int) (viewWidth - touchX);
                break;
        }

        int duration = (400 * Math.abs(dx)) / viewWidth;
        scroller.startScroll((int) touchX, 0, dx, 0, duration);
    }

    private void addShadow(int left, Canvas canvas) {
        shadowDrawable.setBounds(left, 0, left + 30, screenHeight);
        shadowDrawable.draw(canvas);
    }
}
