package android.database.orm.core.db.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table(UserDao.TABLE_NAME)
public class UserDao extends BaseDao {

    public static final String TABLE_NAME       = "tbl_user";
    public static final String COLUMN_ID        = "user_id";
    public static final String COLUMN_NAME      = "name";
    public static final String COLUMN_AVATAR    = "avatar";
    public static final String COLUMN_GENDER    = "gender";
    public static final String COLUMN_AGE       = "age";

    @Column(COLUMN_ID)
    public Long id;

    @Column
    public String name;

    @Column
    public String avatar;

    @Column
    public Boolean gender;

    @Column
    public Integer age;

    private final List<SessionDao> sessions;

    public UserDao() {
        this.sessions = new ArrayList<SessionDao>();
    }

    public List<SessionDao> getSessions() {
        if(this.sessions.isEmpty())
            ; // TODO
        return this.sessions;
    }

}