package android.database.orm.core.db.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table(SessionDao.TABLE_NAME)
public class SessionDao extends BaseDao {

    public static final String TABLE_NAME       = "tbl_session";
    public static final String COLUMN_ID        = "session_id";
    public static final String COLUMN_NAME      = "name";

    @Column(COLUMN_ID)
    public Long id;

    @Column
    public String name;

    private final List<UserDao> users;
    private final List<MessageDao> messages;

    public SessionDao() {
        this.users = new ArrayList<UserDao>();
        this.messages = new ArrayList<MessageDao>();
    }

    public List<UserDao> getUsers() {
        if(this.users.isEmpty())
            ; // TODO
        return this.users;
    }

    public List<MessageDao> getMessages() {
        if(this.messages.isEmpty())
            ; // TODO
        return this.messages;
    }

}