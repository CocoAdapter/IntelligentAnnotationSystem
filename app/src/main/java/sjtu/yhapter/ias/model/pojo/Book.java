package sjtu.yhapter.ias.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import sjtu.yhapter.reader.model.pojo.TxtChapterData;
import org.greenrobot.greendao.DaoException;
import sjtu.yhapter.ias.model.dao.DaoSession;
import sjtu.yhapter.ias.model.dao.DownloadTaskDao;
import sjtu.yhapter.ias.model.dao.BookDao;

@Entity
public class Book implements sjtu.yhapter.reader.model.pojo.Book {

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", path='" + path + '\'' +
                ", shortIntro='" + shortIntro + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", studentId=" + studentId +
                ", teachCourseId=" + teachCourseId +
                '}';
    }

    @Id
    private Long id;
    private String title;
    private String author;
    private String link;
    private String path;
    private String shortIntro;
    private String coverPath;
    private Date createdTime;
    private Date updatedTime;
    private Date lastReadTime;
    private Boolean isFavorite;

    // download related
    private Long taskId;
    @ToOne(joinProperty = "taskId")
    private DownloadTask downloadTask;

    // teach class related
    private Long studentId;
    private Long teachCourseId;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(path);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getShortIntro() {
        return this.shortIntro;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return this.updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTeachCourseId() {
        return this.teachCourseId;
    }

    public void setTeachCourseId(Long teachCourseId) {
        this.teachCourseId = teachCourseId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 8821021)
    public DownloadTask getDownloadTask() {
        Long __key = this.taskId;
        if (downloadTask__resolvedKey == null
                || !downloadTask__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DownloadTaskDao targetDao = daoSession.getDownloadTaskDao();
            DownloadTask downloadTaskNew = targetDao.load(__key);
            synchronized (this) {
                downloadTask = downloadTaskNew;
                downloadTask__resolvedKey = __key;
            }
        }
        return downloadTask;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1738236992)
    public void setDownloadTask(DownloadTask downloadTask) {
        synchronized (this) {
            this.downloadTask = downloadTask;
            taskId = downloadTask == null ? null : downloadTask.getId();
            downloadTask__resolvedKey = taskId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1115456930)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookDao() : null;
    }

    public Date getLastReadTime() {
        return this.lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public Boolean getIsFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {

        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1097957864)
    private transient BookDao myDao;
    @Generated(hash = 450259512)
    private transient Long downloadTask__resolvedKey;

    private Book(Parcel in) {
        id = in.readLong();
        path = in.readString();
    }

    @Generated(hash = 1642284315)
    public Book(Long id, String title, String author, String link, String path,
            String shortIntro, String coverPath, Date createdTime, Date updatedTime,
            Date lastReadTime, Boolean isFavorite, Long taskId, Long studentId,
            Long teachCourseId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.link = link;
        this.path = path;
        this.shortIntro = shortIntro;
        this.coverPath = coverPath;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.lastReadTime = lastReadTime;
        this.isFavorite = isFavorite;
        this.taskId = taskId;
        this.studentId = studentId;
        this.teachCourseId = teachCourseId;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }
}
