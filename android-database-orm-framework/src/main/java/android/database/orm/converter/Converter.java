package android.database.orm.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbMapper;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface Converter<T extends Dao> {
    Class<T> getTable();
    ContentValues toContentValues(DbMapper mapper, T dao);
    T toDao(DbMapper mapper, Cursor cursor);
}