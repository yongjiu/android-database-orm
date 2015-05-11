package android.database.orm.sql;

import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Drop extends BooleanExecutable {
    public Drop(DbContext dbContext, Class<? extends Dao> table) {
        super(dbContext, table);
    }

    public Drop(DbMapper mapper, SQLiteDatabase db) {
        super(mapper, db);
    }

    @Override
    public Boolean onExec() {
        final String SQL = "DROP TABLE IF EXISTS %s;";
        String sql = String.format(SQL, this.mMapper.getName());
        this.mDb.execSQL(sql);
        return true;
    }
}