package android.database.orm.test.dao;

import android.database.orm.Dao;
import android.database.orm.annotation.Column;

/**
 * Created by yongjiu on 15/5/1.
 */
public abstract class TestDao implements Dao {

    @Column("ID")
    public Long id;

}