package sjtu.yhapter.reader.model.pojo;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class ReadingRecord {

    private long bookId;
    private int chapterIndex;
    private int pagePos;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
