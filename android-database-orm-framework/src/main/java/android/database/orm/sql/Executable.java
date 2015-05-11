package android.database.orm.sql;

import android.database.SQLException;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbException;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface Executable<T> {
    T exec();
}

abstract class QueryExecutable implements Executable<Query> {

}

abstract class ResultExecutable implements Executable<Result> {

}

abstract class BooleanExecutable implements Executable<Boolean> {

    protected final DbMapper        mMapper;
    protected final SQLiteDatabase  mDb;
    protected final boolean         mClose;

    private BooleanExecutable(DbMapper mapper, SQLiteDatabase db, boolean close) {
        DbException.checkMapper(mapper);
        DbException.checkNull(db, "db");

        this.mMapper    = mapper;
        this.mDb        = db;
        this.mClose     = close;
    }

    protected BooleanExecutable(DbContext dbContext, Class<? extends Dao> table) {
        this(dbContext.getMapper(table, false), dbContext.getDbHelper().getWritableDatabase(), true);
    }

    protected BooleanExecutable(DbMapper mapper, SQLiteDatabase db) {
        this(mapper, db, false);
    }

    protected abstract Boolean onExec();

    @Override
    public Boolean exec() {
        try {
            boolean success = this.onExec();
            if(this.mClose) this.mDb.close();
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}