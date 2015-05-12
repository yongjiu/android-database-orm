package android.database.orm.core.repository;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface Repository<T> {

    boolean isEmpty();
    int getCount();
    T getItem(int index);

}