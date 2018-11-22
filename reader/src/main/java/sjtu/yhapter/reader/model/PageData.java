package sjtu.yhapter.reader.model;

import java.util.List;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public class PageData {
    public String title;
    public int position; // 在上层传入的数据块中，按当前的分页方案，当前页对应的页码位置
    public List<LineData> lines;
}
