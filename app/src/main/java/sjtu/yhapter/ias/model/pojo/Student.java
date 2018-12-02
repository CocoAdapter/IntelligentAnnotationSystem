package sjtu.yhapter.ias.model.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import sjtu.yhapter.ias.model.dao.DaoSession;
import sjtu.yhapter.ias.model.dao.TeachClassDao;
import sjtu.yhapter.ias.model.dao.StudentDao;

@Entity
public class Student {

    @Id
    private Long id;
    private String studentId;
    private String password;
    private String name;

    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "studentId")
    })
    private List<TeachClass> teachClasses;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1943931642)
    private transient StudentDao myDao;

    @Generated(hash = 565415736)
    public Student(Long id, String studentId, String password, String name) {
        this.id = id;
        this.studentId = studentId;
        this.password = password;
        this.name = name;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1810570375)
    public List<TeachClass> getTeachClasses() {
        if (teachClasses == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeachClassDao targetDao = daoSession.getTeachClassDao();
            List<TeachClass> teachClassesNew = targetDao
                    ._queryStudent_TeachClasses(id);
            synchronized (this) {
                if (teachClasses == null) {
                    teachClasses = teachClassesNew;
                }
            }
        }
        return teachClasses;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1318383912)
    public synchronized void resetTeachClasses() {
        teachClasses = null;
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
    @Generated(hash = 1701634981)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentDao() : null;
    }
}
