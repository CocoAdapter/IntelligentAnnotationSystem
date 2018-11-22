package sjtu.yhapter.reader.model;

import android.graphics.Point;

/**
 * Created by CocoAdapter on 2018/11/20.
 */

public class PointChar {
    public char c;
    public Point topLeft;
    public Point topRight;
    public Point bottomLeft;
    public Point bottomRight;

    public int index;
    public float width;

    @Override
    public String toString() {
        return "PointChar{" +
                "c=" + c +
                ", topLeft=" + topLeft +
                ", topRight=" + topRight +
                ", bottomLeft=" + bottomLeft +
                ", bottomRight=" + bottomRight +
                ", index=" + index +
                ", width=" + width +
                '}';
    }
}
