package sjtu.yhapter.reader.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by CocoAdapter on 2018/11/24.
 */

@Entity
public class Annotation {

    // id
    @Id(autoincrement = true)
    private Long id;
    // book id
    private long bookId;
    // chapter id
    private long chapterId;
    // start index at chapter data
    private long startIndex;
    // end index at chapter data
    private long endIndex;
    // annotated content (now only consider annotation over text)
    private String content;
    // annotation type
    private String type;
    // timestamp
    private Date date;
    // user id
    private long userId;
    // additional note, nullable
    private String note;

    @Generated(hash = 794746748)
    public Annotation(Long id, long bookId, long chapterId, long startIndex,
            long endIndex, String content, String type, Date date, long userId,
            String note) {
        this.id = id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.content = content;
        this.type = type;
        this.date = date;
        this.userId = userId;
        this.note = note;
    }

    @Generated(hash = 1426594540)
    public Annotation() {
    }

    public Long getId() {
        return id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        return (int)(id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Annotation) {
            Annotation other = (Annotation) obj;
            return this.id.equals(other.id);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", chapterId=" + chapterId +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", note='" + note + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }
}
