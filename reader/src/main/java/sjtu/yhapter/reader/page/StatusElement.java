package sjtu.yhapter.reader.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import sjtu.yhapter.reader.model.pojo.PageData;
import sjtu.yhapter.reader.util.ScreenUtil;

public class StatusElement extends BasePageElement {
    private TextPaint textPaint;

    public StatusElement() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(ScreenUtil.spToPx(22));
    }

    @Override
    public void draw(Canvas canvas, Object object) {
        String msg = "加载中...";
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        int baseline = (int) (rectF.bottom + rectF.top - metrics.bottom - metrics.top) / 2;
        float left = (rectF.width() - textPaint.measureText(msg)) / 2;
        canvas.drawText("加载中...", left, baseline, textPaint);
    }
}
