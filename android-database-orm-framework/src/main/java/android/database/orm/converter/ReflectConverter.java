package android.database.orm.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbMapper;
import android.database.orm.DbUtils;

/**
 * Created by yongjiu on 15/5/1.
 */
public class ReflectConverter<T extends Dao> implements Converter<T> {

    @Override
    public Class<T> getTable() {
        return null;
    }

    @Override
    public ContentValues toContentValues(DbMapper mapper, T dao) {
        return DbUtils.toContentValues(mapper, dao);
    }

    @Override
    public T toDao(DbMapper mapper, Cursor cursor) {
        return DbUtils.toObject(cursor, mapper);
    }

}