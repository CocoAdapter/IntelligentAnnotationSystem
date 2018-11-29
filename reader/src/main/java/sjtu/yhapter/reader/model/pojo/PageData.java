package sjtu.yhapter.reader.model.pojo;

import java.util.List;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public class PageData {
    // annotation related
    public long bookId;
    public long chapterId;
    public long startIndex; // 当前页在章节中的起始点
    public long endIndex; // 当前页在章节中的结束点

    // UI related
    public String title;
    public int position; // 在上层传入的数据块中，按当前的分页方案，当前页对应的页码位置
    public List<LineData> lines;
}
