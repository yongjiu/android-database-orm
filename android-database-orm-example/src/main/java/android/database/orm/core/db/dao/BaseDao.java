package android.database.orm.core.db.dao;

import android.database.orm.Dao;
import android.database.orm.annotation.Column;

/**
 * Created by yongjiu on 15/5/1.
 */
public class BaseDao implements Dao {

    public static final String COLUMN_CREATED = "created";

    @Column
    public Long created;

}