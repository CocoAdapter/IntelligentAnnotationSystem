package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Book {

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
    private boolean isDownloaded;
    private String path;
    private String shortIntro;
    private String coverPath;
    private Date createdTime;
    private Date updatedTime;

    // teach class related
    private Long studentId;
    private Long teachCourseId;
    @Generated(hash = 528710385)
    public Book(Long id, String title, String author, String link,
            boolean isDownloaded, String path, String shortIntro, String coverPath,
            Date createdTime, Date updatedTime, Long studentId,
            Long teachCourseId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.link = link;
        this.isDownloaded = isDownloaded;
        this.path = path;
        this.shortIntro = shortIntro;
        this.coverPath = coverPath;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.studentId = studentId;
        this.teachCourseId = teachCourseId;
    }
    @Generated(hash = 1839243756)
    public Book() {
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
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public boolean getIsDownloaded() {
        return this.isDownloaded;
    }
    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }
}
