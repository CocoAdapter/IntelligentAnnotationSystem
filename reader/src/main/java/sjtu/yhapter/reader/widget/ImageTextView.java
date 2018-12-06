package sjtu.yhapter.reader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import sjtu.yhapter.reader.R;

/**
 * Only supports ONE drawable at left, top, right, or bottom. The second one will be simply ignored.
 *
 * Created by CocoAdapter on 2018/2/10.
 */

public class ImageTextView extends android.support.v7.widget.AppCompatTextView {
    protected int drawableWidth;
    protected int drawableHeight;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        drawableWidth = array.getDimensionPixelSize(R.styleable.ImageTextView_drawableWidth, 50);
        drawableHeight = array.getDimensionPixelSize(R.styleable.ImageTextView_drawableHeight, 50);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas = getTopCanvas(canvas);
        super.onDraw(canvas);
    }

    private Canvas getTopCanvas(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = null;
        int directionFlag = -1;
        for (int i = 0; i < 4; i++){
            if (drawables[i] != null){
                drawable = drawables[i];
                int xOffset = (drawable.getIntrinsicWidth() - drawableWidth) / 2;
                int yOffset = (drawable.getIntrinsicHeight() - drawableHeight) / 2;
                drawable.setBounds(xOffset, yOffset, drawableWidth + xOffset, drawableHeight + yOffset);
                directionFlag = i;
                break;
            }
        }

        if (directionFlag == 0 || directionFlag == 2){ // left or right
            float textWidth = getPaint().measureText(getText().toString());
            int drawWidth = drawable.getIntrinsicWidth();
            int drawPadding = getCompoundDrawablePadding();
            float contentWidth = textWidth + drawWidth + drawPadding;

            float dx = (getWidth() - contentWidth) / 2;
            if (directionFlag == 2)
                dx = -dx;
            canvas.translate(dx, 0);
        } else if (directionFlag == 1 || directionFlag == 3) { // top or bottom
            float textHeight = getPaint().getTextSize();
            int drawHeight = drawable.getIntrinsicHeight();
            int drawPadding = getCompoundDrawablePadding();
            float contentHeight = textHeight * 2 + drawHeight + drawPadding;
            int topPadding = (int) (getHeight() - contentHeight);
            setPadding(0, topPadding , 0, 0);
            float dy = (contentHeight - getHeight())/2;
            if (directionFlag == 3)
                dy = -dy;

            canvas.translate(0, dy);
        }

        return canvas;
    }
}
