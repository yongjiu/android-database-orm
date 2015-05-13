package android.database.orm.test.dao;

import android.database.orm.Dao;
import android.database.orm.annotation.Column;

/**
 * Created by yongjiu on 15/5/1.
 */
public abstract class BaseDao implements Dao {

    public static final String COLUMN_CREATED = "created";

    @Column
    public Long created;

}