package sjtu.yhapter.reader.model.pojo;

import android.graphics.Point;

/**
 * Created by CocoAdapter on 2018/11/20.
 */

public class PointChar {
    // annotation related
    public long chapterIndex;

    // UI related
    public char c;
    public Point topLeft;
    public Point topRight;
    public Point bottomLeft;
    public Point bottomRight;

    public int index; // 当前行中的index
    public float width;

    @Override
    public String toString() {
        return "PointChar{" +
                "chapterIndex=" + chapterIndex +
                ", c=" + c +
                ", index=" + index +
                ", width=" + width +
                '}';
    }
}
