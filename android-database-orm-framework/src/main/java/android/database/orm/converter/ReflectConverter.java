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

    private final DbMapping mDbMapping;
    public ReflectConverter(DbMapping mapping) {
        this.mDbMapping = mapping;
    }

    @Override
    public Class<T> getTable() {
        return null;
    }

    @Override
    public ContentValues toContentValues(T dao) {
        DbMapper mapper = this.mDbMapping.getMapper(dao.getClass(), true);
        return DbUtils.toContentValues(mapper, dao);
    }

    @Override
    public T toDao(Cursor cursor, Class<T> table) {
        return DbUtils.toObject(this.mDbMapping, cursor, table);
    }

}