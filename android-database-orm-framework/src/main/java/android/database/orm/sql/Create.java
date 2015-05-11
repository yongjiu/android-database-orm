package android.database.orm.sql;

import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Create extends BooleanExecutable {

    public Create(DbContext dbContext, Class<? extends Dao> table) {
        super(dbContext, table);
    }

    public Create(DbMapper mapper, SQLiteDatabase db) {
        super(mapper, db);
    }

    @Override
    public Boolean onExec() {
        final String SQL = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String sql = this.mMapper.toString(SQL);
        this.mDb.execSQL(sql);
        return true;
    }

}