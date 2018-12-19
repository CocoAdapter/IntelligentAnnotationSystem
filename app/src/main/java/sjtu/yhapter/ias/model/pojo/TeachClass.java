package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeachClass {
    @Id
    private Long classid;
    private String classname;
    private String desc;

    private int status; // 0 申请了 还没通过， 1 已加入

    @Generated(hash = 899354365)
    public TeachClass(Long classid, String classname, String desc, int status) {
        this.classid = classid;
        this.classname = classname;
        this.desc = desc;
        this.status = status;
    }

    @Generated(hash = 1071052928)
    public TeachClass() {
    }

    public Long getClassid() {
        return this.classid;
    }

    public void setClassid(Long classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
