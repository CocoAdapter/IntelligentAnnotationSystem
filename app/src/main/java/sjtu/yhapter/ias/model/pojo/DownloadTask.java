package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import sjtu.yhapter.ias.model.remote.download.DownloadListener;
import sjtu.yhapter.ias.service.DownloadService;

@Entity
public class DownloadTask {
    @Override
    public String toString() {
        return "DownloadTask{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", localPath='" + localPath + '\'' +
                ", taskName='" + taskName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", size=" + size +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }

    @Id
    private Long id;
    private String link;
    private String localPath;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private long size;
    private long progress;

    @Transient
    private int status = DownloadService.STATUS_WAIT;
    @Transient
    private DownloadListener downloadListener;

    @Generated(hash = 1409303561)
    public DownloadTask(Long id, String link, String localPath, String taskName,
            Date startTime, Date endTime, long size, long progress) {
        this.id = id;
        this.link = link;
        this.localPath = localPath;
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.size = size;
        this.progress = progress;
    }

    @Generated(hash = 1999398913)
    public DownloadTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
}
