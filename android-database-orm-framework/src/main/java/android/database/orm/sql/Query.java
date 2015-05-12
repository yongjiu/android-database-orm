package android.database.orm.sql;

import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbMapping;
import android.database.orm.converter.Converter;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.util.ArrayList;
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

    /******************************************************************************************************************/

    public <T extends Dao> List<T> list(Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            while (this.mCursor.moveToNext()) {
                T t = this.toObject(this.mCursor, cls, false);
                list.add(t);
            }
        } finally {
            this.close();
        }
        return list;
    }

    /******************************************************************************************************************/

    public <T extends Dao> T singleOrNull(Class<T> cls, int index) {
        return this.singleOrDefault(cls, index, null);
    }

    public <T extends Dao> T singleOrDefault(Class<T> cls, int index, T defaultValue) {
        if(this.mCursor.move(index)) {
            return this.toObject(cls);
        }
        return defaultValue;
    }

    public <T extends Dao> T single(Class<T> cls, int index) {
        this.mCursor.move(index);
        return this.toObject(cls);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> T singleOrNull(Class<T> cls) {
        return this.firstOrNull(cls);
    }

    public <T extends Dao> T singleOrDefault(Class<T> cls, T defaultValue) {
        return this.firstOrDefault(cls, defaultValue);
    }

    public <T extends Dao> T single(Class<T> cls) {
        return this.first(cls);
    }

    /******************************************************************************************************************/

    public <T extends Dao> T firstOrNull(Class<T> cls) {
        return this.firstOrDefault(cls, null);
    }

    public <T extends Dao> T firstOrDefault(Class<T> cls, T defaultValue) {
        if(this.mCursor.moveToFirst()) {
            return this.toObject(cls);
        }
        return defaultValue;
    }

    public <T extends Dao> T first(Class<T> cls) {
        this.mCursor.moveToFirst();
        return this.toObject(cls);
    }

    /******************************************************************************************************************/

    public <T extends Dao> T lastOrNull(Class<T> cls) {
        return this.lastOrDefault(cls, null);
    }

    public <T extends Dao> T lastOrDefault(Class<T> cls, T defaultValue) {
        if(this.mCursor.moveToLast()) {
            return this.toObject(cls);
        }
        return defaultValue;
    }

    public <T extends Dao> T last(Class<T> cls) {
        this.mCursor.moveToLast();
        return this.toObject(cls);
    }

    /******************************************************************************************************************/

    private <T extends Dao> T toObject(Class<T> cls) {
        return this.toObject(this.mCursor, cls, true);
    }

    private <T extends Dao> T toObject(Cursor cursor, Class<T> cls, boolean close) {
        try {
            Converter<T> converter = this.mDbMapping.getConverter(cls);
            T dao = converter.toDao(cursor, this.mDbMapping, cls);
            return dao;
        } finally {
            if(close) this.close();
        }
    }

}