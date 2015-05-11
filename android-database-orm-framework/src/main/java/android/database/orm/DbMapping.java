package android.database.orm;

import android.database.orm.converter.Converter;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface DbMapping {

    <T extends Dao> java.util.Set<Class<T>> getTables();
    <T extends Dao> DbMapper getMapper(Class<T> table, boolean check);
    <T extends Dao> Converter<T> getConverter(Class<T> table);

}