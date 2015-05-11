package android.database.orm.sql;

/**
 * Created by yongjiu on 15/5/1.
 */
abstract class Setter implements Executable<Result> {

    private final android.content.ContentValues mContentValues;
    private final Executable<Result> mExecutor;

    protected Setter(Executable<Result> executor) {
        this.mContentValues = new android.content.ContentValues();
        this.mExecutor = executor;
    }

    protected android.content.ContentValues getContentValues() {
        return this.mContentValues;
    }

    @Override
    public Result exec() {
        return this.mExecutor.exec();
    }

    /******************************************************************************************************************/

    protected Setter put(String column, Boolean value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, String value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, byte[] value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Byte value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Short value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Integer value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Long value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Float value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(String column, Double value) {
        this.mContentValues.put(column, value);
        return this;
    }

    protected Setter put(android.content.ContentValues contentValues) {
        this.mContentValues.putAll(contentValues);
        return this;
    }

    protected Setter putNull(String column) {
        this.mContentValues.putNull(column);
        return this;
    }

}