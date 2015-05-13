package android.database.orm.sql;

import android.content.ContentValues;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbException;
import android.database.orm.DbMapper;
import android.database.orm.converter.Converter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Update extends ResultExecutable {

    private final Setter            mSetter;
    private final DbContext         mDbContext;
    private Class<? extends Dao>    mTable;
    private int                     mConflictAlgorithm;

    public Update(DbContext context, Class<? extends Dao> table) {
        this.mSetter            = new Setter(this);
        this.mConflictAlgorithm = SQLiteDatabase.CONFLICT_NONE;
        this.mDbContext         = context;
        this.mTable             = table;
    }

    /******************************************************************************************************************/

    public Update conflict(int conflictAlgorithm) {
        this.mConflictAlgorithm = conflictAlgorithm;
        return this;
    }

    /******************************************************************************************************************/

    public <T extends Dao> Result update(T dao) {
        return this.update(dao, null);
    }

    public <T extends Dao> Result update(T dao, String clause, String... args) {
        DbException.checkNull(dao, "dao");
        Class<T> table = (Class<T>)dao.getClass();
        DbMapper mapper = this.mDbContext.getMapper(table, true);
        Converter<T> converter = this.mDbContext.getConverter(table);
        ContentValues contentValues = converter.toContentValues(mapper, dao);
        return this.exec(mapper, contentValues, clause, args);
    }

    /******************************************************************************************************************/

    @Override
    public Result exec() {
        return this.exec(Where.All);
    }

    public Result exec(Where where) {
        DbMapper mapper = this.mDbContext.getMapper(this.mTable, false);
        return this.exec(mapper, this.mSetter.getContentValues(), where.getClause(), where.getArgs());
    }

    private Result exec(DbMapper mapper, ContentValues contentValues, String clause, String... args) {
        DbException.checkMapper(mapper);
        DbException.checkNull(contentValues, "contentValues");

        SQLiteOpenHelper helper = this.mDbContext.getDbHelper();
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            int updated = db.updateWithOnConflict(
                    mapper.getName(),
                    contentValues,
                    clause,
                    args,
                    this.mConflictAlgorithm);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.failed(e);
        } finally {
            db.close();
        }
    }

    /******************************************************************************************************************/

    private Setter getSetter() {
        return this.mSetter;
    }

    public Setter set(String column, String value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Integer value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Long value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Float value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Double value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Short value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Byte value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, byte[] value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(String column, Boolean value) {
        return this.getSetter().set(column, value);
    }

    public Setter set(ContentValues contentValues) {
        return this.getSetter().set(contentValues);
    }

    public Setter setNull(String column) {
        return this.getSetter().setNull(column);
    }

    public static class Setter extends android.database.orm.sql.Setter {

        private final Update mUpdate;
        private Setter(Update update) {
            super(update);
            this.mUpdate = update;
        }

        public Where<Result> where(String clause, String... args) {
            return new Where<Result>(clause, args) {
                @Override
                public Result exec() {
                    return mUpdate.exec(this);
                }
            };
        }

        public Setter set(String column, Boolean value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, String value) {
            super.put(column, value);
            return this;
        }

        public Setter set(String column, byte[] value) {
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

        public Setter set(ContentValues contentValues) {
            super.put(contentValues);
            return this;
        }

        public Setter setNull(String column) {
            super.putNull(column);
            return this;
        }
    }
}