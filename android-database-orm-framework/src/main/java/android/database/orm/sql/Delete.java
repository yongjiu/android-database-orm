package android.database.orm.sql;

import android.database.orm.DbContext;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Delete extends ResultExecutable {

    private final From mFrom;
    public Delete(From from) {
        this.mFrom = from;
    }

    /******************************************************************************************************************/

    public Where<Result> where(String clause, String... args) {
        final Delete delete = this;
        return new Where<Result>(clause, args) {
            @Override
            public Result exec() {
                return delete.exec(this);
            }
        };
    }

    /******************************************************************************************************************/

    private Result exec(Where where) {
        return this.exec(where.getClause(), where.getArgs());
    }

    @Override
    public Result exec() {
        return this.exec(Where.All);
    }

    public Result exec(String whereClause, String... whereArgs) {
        From from               = this.mFrom;
        DbContext context       = from.getDbContext();
        SQLiteOpenHelper helper = context.getDbHelper();
        SQLiteDatabase db       = helper.getWritableDatabase();
        DbMapper mapper         = from.getMapper();
        try {
            int deleted = db.delete(mapper.getName(), whereClause, whereArgs);
            return Result.success(deleted);
        } catch (Exception e) {
            return Result.failed(e);
        } finally {
            db.close();
        }
    }

}