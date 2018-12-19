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
import sjtu.yhapter.ias.model.dao.TeachClassDao;

@Entity
public class Book implements sjtu.yhapter.reader.model.pojo.Book {
    @Id
    private Long id;
    private String name;
    private String author;
    private String url;
    private String path;
    private String brief;
    private String picture;
    private Date create_time;
    private Date update_time;

    private Date lastReadTime;
    private Boolean isFavorite;

    // download related
    private Long taskId;
    @ToOne(joinProperty = "taskId")
    private DownloadTask downloadTask;

    private Long teachClassId;
    @ToOne(joinProperty = "teachClassId")
    private TeachClass teachClass;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBrief() {
        return this.brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
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

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Long getTeachClassId() {
        return this.teachClassId;
    }

    public void setTeachClassId(Long teachClassId) {
        this.teachClassId = teachClassId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1624042324)
    public TeachClass getTeachClass() {
        Long __key = this.teachClassId;
        if (teachClass__resolvedKey == null || !teachClass__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeachClassDao targetDao = daoSession.getTeachClassDao();
            TeachClass teachClassNew = targetDao.load(__key);
            synchronized (this) {
                teachClass = teachClassNew;
                teachClass__resolvedKey = __key;
            }
        }
        return teachClass;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 666447524)
    public void setTeachClass(TeachClass teachClass) {
        synchronized (this) {
            this.teachClass = teachClass;
            teachClassId = teachClass == null ? null : teachClass.getClassid();
            teachClass__resolvedKey = teachClassId;
        }
    }

    private Book(Parcel in) {
        id = in.readLong();
        path = in.readString();
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
    @Generated(hash = 2024068698)
    private transient Long teachClass__resolvedKey;

    public Book() {
    }

    @Generated(hash = 1646449777)
    public Book(Long id, String name, String author, String url, String path, String brief,
            String picture, Date create_time, Date update_time, Date lastReadTime,
            Boolean isFavorite, Long taskId, Long teachClassId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.url = url;
        this.path = path;
        this.brief = brief;
        this.picture = picture;
        this.create_time = create_time;
        this.update_time = update_time;
        this.lastReadTime = lastReadTime;
        this.isFavorite = isFavorite;
        this.taskId = taskId;
        this.teachClassId = teachClassId;
    }
}
