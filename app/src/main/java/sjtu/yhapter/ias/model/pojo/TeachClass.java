package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeachClass {
    @Id
    private Long id;

    private int status; // 0 没有这个人， 1 申请了 还没通过， 2 已加入
    private Long studentId;

    // TODO 加入时间，有无新任务，用作排序
    @Transient
    private String otherInfo;

    @Generated(hash = 682796218)
    public TeachClass(Long id, int status, Long studentId) {
        this.id = id;
        this.status = status;
        this.studentId = studentId;
    }

    @Generated(hash = 1071052928)
    public TeachClass() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
