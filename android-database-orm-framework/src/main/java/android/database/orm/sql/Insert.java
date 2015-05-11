package android.database.orm.sql;

import android.content.ContentValues;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbException;
import android.database.orm.DbMapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Insert extends ResultExecutable {

    private final DbContext         mDbContext;
    private final Setter            mSetter;
    private Class<? extends Dao>    mTable;
    private String                  mNullColumnHack;
    private int                     mConflictAlgorithm;

    public Insert(DbContext context, Class<? extends Dao> table) {
        this.mSetter            = new Setter(this);
        this.mDbContext         = context;
        this.mTable             = table;
        this.mConflictAlgorithm = SQLiteDatabase.CONFLICT_NONE;
        this.mNullColumnHack    = null;
    }

    public Insert nullColumnHack(String nullColumnHack) {
        this.mNullColumnHack = nullColumnHack;
        return this;
    }

    public Insert conflict(int conflictAlgorithm) {
        this.mConflictAlgorithm = conflictAlgorithm;
        return this;
    }

    public Setter values() {
        return this.mSetter;
    }

    /******************************************************************************************************************/

    @Override
    public Result exec() {
        DbMapper mapper = this.mDbContext.getMapper(this.mTable, true);
        return this.exec(mapper, this.mSetter.getContentValues());
    }

    private Result exec(DbMapper mapper, ContentValues contentValues) {
        DbException.checkMapper(mapper);
        DbException.checkNull(contentValues, "contentValues");

        SQLiteOpenHelper helper = this.mDbContext.getDbHelper();
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            long inserted = db.insertWithOnConflict(
                    mapper.getName(),
                    this.mNullColumnHack,
                    contentValues,
                    this.mConflictAlgorithm);
            return Result.success(inserted);
        } catch (Exception e) {
            return Result.failed(e);
        } finally {
            db.close();
        }
    }

    /******************************************************************************************************************/

    public <T extends Dao> Result insert(T dao) {
        return Result.failed(null); // TODO
    }

    public Result insert(ContentValues contentValues) {
        return values().set(contentValues).exec();
    }

    /******************************************************************************************************************/

    public static String[] columns(String... columns) {
        return columns;
    }

    public static Object[] values(Object... values) {
        return values;
    }

    /******************************************************************************************************************/

    public static class Setter extends android.database.orm.sql.Setter {

        private Setter(Insert insert) {
            super(insert);
        }

        public Setter set(String column, Boolean value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, String value) {
            super.put(column, value);
            return this;
        }

        public Setter value(String column, byte[] value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Byte value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Short value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Integer value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Long value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Float value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, Double value) {
            super.put(column, value);
            return this;
        }

        public Setter setNull(String column) {
            super.putNull(column);
            return this;
        }

        protected Setter set(ContentValues contentValues) {
            super.put(contentValues);
            return this;
        }
    }
}