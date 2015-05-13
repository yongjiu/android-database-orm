package android.database.orm.test.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table(MessageDao.TABLE_NAME)
public class MessageDao extends BaseDao {

    public static final String TABLE_NAME       = "tbl_message";
    public static final String COLUMN_ID        = "message_id";
    public static final String COLUMN_SESSION   = "session_id";
    public static final String COLUMN_USER      = "user_id";
    public static final String COLUMN_CONTENT   = "content";
    public static final String COLUMN_TYPE      = "type";

    @Column(COLUMN_ID)
    public Long id;

    @Column(COLUMN_SESSION)
    public Long session;

    @Column(COLUMN_USER)
    public Long user;

    @Column
    public String content;

    @Column
    public Integer type;

}