package sjtu.yhapter.reader.model.pojo;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class TxtChapterData extends ChapterData {
    // index in byte, included
    protected long startIndex;
    // index in byte, excluded
    protected long endIndex;

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    @Override
    public int getLength() {
        return (int) (endIndex - startIndex);
    }

    @Override
    public String toString() {
        return "TxtChapterData{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", id=" + id +
                ", bookId=" + bookId +
                ", title='" + title + '\'' +
                '}';
    }
}
