package android.database.orm.test.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table("tbl_session")
public class SessionDao extends TestDao {

    @Column("Name")
    public String name;

    public java.util.List<MessageDao> messages;

}