package sjtu.yhapter.reader.widget.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import sjtu.yhapter.reader.model.Constants;

/**
 * Created by CocoAdapter on 2018/11/13.
 */

public class CoverPageAnim extends HorizontalPageAnim {
    private Rect srcRect, destRect;
    private GradientDrawable shadowDrawable;

    public CoverPageAnim(Context context, int w, int h) {
        super(context, w, h);

        srcRect = new Rect(0, 0, contentWidth, contentHeight);
        destRect = new Rect(0, 0, contentWidth, contentHeight);

        int[] mBackShadowColors = new int[]{0x66000000, 0x00000000};
        shadowDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel) {
            nextBitmap.recycle();
            nextBitmap = currBitmap.copy(Constants.BITMAP_CONFIG, true);
            canvas.drawBitmap(currBitmap, 0, 0, null);

            nextSurfaceBitmap.recycle();
            nextSurfaceBitmap = currSurfaceBitmap.copy(Constants.BITMAP_CONFIG, true);
            canvas.drawBitmap(currSurfaceBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(nextBitmap, 0, 0, null);
            canvas.drawBitmap(nextSurfaceBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawMove(Canvas canvas) {
        switch (direction) {
            case NEXT:
                int dis = (int) (contentWidth - startX + touchX);
                if (dis > contentWidth) dis = contentWidth;

                srcRect.left = contentWidth - dis;
                destRect.right = dis;

                canvas.drawBitmap(nextBitmap, 0, 0, null);
                canvas.drawBitmap(nextSurfaceBitmap, 0, 0, null);
                canvas.drawBitmap(currBitmap, srcRect, destRect, null);
                canvas.drawBitmap(currSurfaceBitmap, 0, 0, null);
                addShadow(dis, canvas);
                break;
            case PRE:
                srcRect.left = (int) (contentWidth - touchX);
                destRect.right = (int) touchX;

                canvas.drawBitmap(currBitmap, 0, 0, null);
                canvas.drawBitmap(currSurfaceBitmap, 0, 0, null);
                canvas.drawBitmap(nextBitmap, srcRect, destRect, null);
                canvas.drawBitmap(nextSurfaceBitmap, srcRect, destRect, null);
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
                    int dis = (int) ((contentWidth - startX) + touchX);
                    if (dis > contentWidth) dis = contentWidth;

                    dx = contentWidth - dis;
                } else
                    dx = (int) -(touchX + (contentWidth - startX));
                break;
            case PRE:
                dx = isCancel ? (int) -touchX : (int) (contentWidth - touchX);
                break;
        }

        int duration = (400 * Math.abs(dx)) / contentWidth;
        scroller.startScroll((int) touchX, 0, dx, 0, duration);
    }

    private void addShadow(int left, Canvas canvas) {
        shadowDrawable.setBounds(left, 0, left + 30, viewHeight);
        shadowDrawable.draw(canvas);
    }
}
