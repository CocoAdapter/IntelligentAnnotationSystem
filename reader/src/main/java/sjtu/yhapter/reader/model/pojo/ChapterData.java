package sjtu.yhapter.reader.model.pojo;

import android.os.Parcelable;

import java.io.BufferedReader;

/**
 * Created by Yhapter on 2018/11/28.
 */
public abstract class ChapterData implements Parcelable {
    protected long id;
    protected long bookId;
    protected String title;

    // TODO 只有分页后才知道
    protected int pageStartIndex;
    protected int pageEndIndex;

    public long getId() {
        return id;
    }

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract int getLength();
}
