package android.database.orm.sql;

import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Truncate extends BooleanExecutable {
    public Truncate(DbContext dbContext, Class<? extends Dao> table) {
        super(dbContext, table);
    }
    public Truncate(DbMapper mapper, SQLiteDatabase db) {
        super(mapper, db);
    }
    @Override
    protected Boolean onExec() {
        String table = this.mMapper.getName();
        Boolean error = false;
        error |= this.mDb.delete(table, "1=1", null) < 0;
        error |= this.mDb.delete("sqlite_sequence", "name=?", new String[]{ table }) < 0;
        return !error;
    }
}