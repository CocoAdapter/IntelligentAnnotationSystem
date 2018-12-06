package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeachClass {
    @Override
    public String toString() {
        return "TeachClass{" +
                "id=" + id +
                ", status=" + status +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Id
    private Long id;
    private int status; // 1 申请了 还没通过， 2 已加入, 3 已关闭
    private String studentId;

    // TODO 加入时间，有无新任务，用作排序
    private String name;

    @Generated(hash = 897858683)
    public TeachClass(Long id, int status, String studentId, String name) {
        this.id = id;
        this.status = status;
        this.studentId = studentId;
        this.name = name;
    }

    @Generated(hash = 1071052928)
    public TeachClass() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
