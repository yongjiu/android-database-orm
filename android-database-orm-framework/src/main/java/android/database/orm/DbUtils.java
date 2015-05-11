package android.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbUtils {
    public static <T extends Dao> ContentValues toContentValues(DbMapper mapper, T dao) {
        return null;
    }

    public static <T extends Dao> T toObject(DbMapping dbMapping, Cursor cursor, Class<T> table) {
        return null;
    }
}