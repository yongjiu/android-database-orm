package android.database.orm.sql;

import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbMapping;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Query implements Closeable {

    private final DbMapping         mDbMapping;
    private final SQLiteDatabase    mDb;
    private final Cursor            mCursor;
    private boolean                 mClosed;

    Query(DbMapping mapping, SQLiteDatabase db, Cursor cursor) {
        this.mDbMapping = mapping;
        this.mDb        = db;
        this.mCursor    = cursor;
        this.mClosed    = false;
    }

    @Override
    public void close() {
        if(this.mClosed) return;
        this.mCursor.close();
        this.mDb.close();
        this.mClosed = true;
    }

    public int getCount() {
        return this.mCursor.getCount();
    }

    public <T extends Dao> List<T> list(Class<T> cls) {
        return null;
    }

    public <T extends Dao> T single(Class<T> cls, int index) {
        return null;
    }

}