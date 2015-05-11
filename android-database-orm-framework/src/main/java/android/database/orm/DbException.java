package android.database.orm;

import java.util.Collection;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbException extends Exception {

    public static void checkEmpty(String text, String msg) {
        if(!DbUtils.isEmpty(text)) return;
        throw new IllegalArgumentException(msg);
    }

    public static void checkEmpty(Collection list, String msg) {
        if(!DbUtils.isEmpty(list)) return;
        throw new IllegalArgumentException(msg);
    }

    public static void checkNull(Object nullable, String msg) {
        if(nullable != null) return;
        throw new IllegalArgumentException(msg);
    }

    public static void checkMapper(DbMapper mapper) {
        checkNull(mapper, "mapper");
    }
}