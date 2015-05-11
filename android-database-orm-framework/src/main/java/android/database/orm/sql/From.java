package android.database.orm.sql;

import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbMapper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class From {

    private final DbContext                         mDbContext;
    private final Class<? extends Dao>              mTable;

    public From(DbContext context, Class<? extends Dao> table) {
        this.mDbContext = context;
        this.mTable     = table;
    }

    /******************************************************************************************************************/

    public DbContext getDbContext() {
        return this.mDbContext;
    }

    public Class<? extends Dao> getTable() {
        return this.mTable;
    }

    public DbMapper getMapper() {
        return this.getDbContext().getMapper(this.getTable(), true);
    }

    /******************************************************************************************************************/

    public Select select(String... columns) {
        return new Select(this, columns);
    }

    public Delete delete() {
        return new Delete(this);
    }

}