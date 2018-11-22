package sjtu.yhapter.reader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里需要存储 在当前分页中的位置， 还要存储在原文中的位置，以便进行批注统计
 * Created by CocoAdapter on 2018/11/20.
 */

public class LineData {
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

    @Override
    public String toString() {
        return "LineData{" +
                "chars=" + chars +
                '}';
    }
}
