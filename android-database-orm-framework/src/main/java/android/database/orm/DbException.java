package android.database.orm;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbException extends Exception {

    public static void checkNull(Object nullable, String msg) {
        if(nullable != null) return;
        throw new IllegalArgumentException(msg);
    }

    public static void checkMapper(DbMapper mapper) {
        checkNull(mapper, "mapper");
    }

}