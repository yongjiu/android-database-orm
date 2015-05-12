package android.database.orm.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbMapper;
import android.database.orm.DbMapping;
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
    public ContentValues toContentValues(DbMapping mapping, T dao) {
        DbMapper mapper = mapping.getMapper(dao.getClass(), true);
        return DbUtils.toContentValues(mapper, dao);
    }

    @Override
    public T toDao(Cursor cursor, DbMapping mapping, Class<T> table) {
        return DbUtils.toObject(mapping, cursor, table);
    }

}