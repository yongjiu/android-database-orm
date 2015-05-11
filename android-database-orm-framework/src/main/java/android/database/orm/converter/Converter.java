package android.database.orm.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.Dao;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface Converter<T extends Dao> {
    Class<T> getTable();
    ContentValues toContentValues(T dao);
    T toDao(Cursor cursor, Class<T> table);
}