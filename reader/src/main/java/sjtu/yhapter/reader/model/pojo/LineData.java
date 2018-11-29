package sjtu.yhapter.reader.model.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CocoAdapter on 2018/11/20.
 */

public class LineData {
    private float baseline;
    private List<PointChar> chars;

    public LineData() {
        chars = new ArrayList<>();
    }

    public void append(PointChar pointChar) {
        chars.add(pointChar);
    }

    public List<PointChar> getChars() {
        return chars;
    }

    public String content() {
        StringBuilder sb = new StringBuilder(chars.size());
        for (PointChar p : chars) {
            sb.append(p.c);
        }
        return sb.toString();
    }

    public float getBaseline() {
        return baseline;
    }

    public void setBaseline(float baseline) {
        this.baseline = baseline;
    }

    @Override
    public String toString() {
        return "LineData{" +
                "chars=" + chars +
                '}';
    }
}
