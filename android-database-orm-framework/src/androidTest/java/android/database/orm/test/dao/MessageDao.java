package android.database.orm.test.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table("tbl_message")
public class MessageDao extends TestDao {

    @Column("Session")
    public Long session;

}