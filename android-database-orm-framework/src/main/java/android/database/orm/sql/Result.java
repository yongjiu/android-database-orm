package android.database.orm.sql;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Result {

    private final long      mRows;
    private final Exception mError;

    private Result(long inserted, Exception e) {
        this.mRows  = inserted;
        this.mError = e;
    }

    public boolean success() {
        return this.mError == null || this.mRows >= 0;
    }

    public long getAffectedRows() {
        return this.mRows;
    }

    /******************************************************************************************************************/

    public static Result success(long affected) {
        return new Result(affected, null);
    }

    public static Result failed(Exception e) {
        return new Result(-1, e);
    }

}