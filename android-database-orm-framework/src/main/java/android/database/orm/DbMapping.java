package android.database.orm;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface DbMapping {

    <T extends Dao> java.util.Set<Class<T>> getTables();
    <T extends Dao> DbMapper getMapper(Class<T> table, boolean check);

}