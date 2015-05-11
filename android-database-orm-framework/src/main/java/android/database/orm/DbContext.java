package android.database.orm;

import android.content.Context;
import android.database.orm.converter.Converter;
import android.database.orm.converter.ReflectConverter;
import android.database.orm.sql.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.*;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbContext implements DbMapping {

    private final DbMapping         mDbMapping;
    private final SQLiteOpenHelper  mDbHelper;

    private DbContext(DbMapping dbMapping, SQLiteOpenHelper dbHelper) {
        this.mDbMapping = dbMapping;
        this.mDbHelper  = dbHelper;
    }

    public void destroy() {

    }

    /**************************************************************************************************************/

    public <T extends Dao> From from(Class<T> table) {
        return new From(this, table);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Select select(Class<T> table, String... columns) {
        DbException.checkNull(table, "table");
        return this.from(table).select(columns);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Delete delete(Class<T> table) {
        DbException.checkNull(table, "table");
        return this.from(table).delete();
    }

    public <T extends Dao> Result delete(Class<T> table, String whereClause, String... whereArgs) {
        DbException.checkNull(table, "table");
        return this.from(table).delete().exec(whereClause, whereArgs);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Insert insert(Class<T> table) {
        DbException.checkNull(table, "table");
        return new Insert(this, table);
    }

    public <T extends Dao> Result insert(T dao) {
        DbException.checkNull(dao, "dao");
        return new Insert(this, dao.getClass()).insert(dao);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Update update(Class<T> table) {
        DbException.checkNull(table, "table");
        return new Update(this, table);
    }

    public <T extends Dao> Result update(T dao) {
        DbException.checkNull(dao, "dao");
        return new Update(this, dao.getClass()).update(dao);
    }

    public <T extends Dao> Result update(T dao, String clause, String... args) {
        DbException.checkNull(dao, "dao");
        return new Update(this, dao.getClass()).update(dao, clause, args);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Boolean create(Class<T> table) {
        DbException.checkNull(table, "table");
        return new Create(this, table).exec();
    }

    public <T extends Dao> Boolean create(Class<T>... tables) {
        return this.exec(true, tables);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Boolean truncate(Class<T> table) {
        DbException.checkNull(table, "table");
        return new Truncate(this, table).exec();
    }

    public <T extends Dao> Boolean truncate(Class<T>... tables) {
        return this.exec(null, tables);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public <T extends Dao> Boolean drop(Class<T> table) {
        DbException.checkNull(table, "table");
        return new Drop(this, table).exec();
    }

    public <T extends Dao> Boolean drop(Class<T>... tables) {
        return this.exec(false, tables);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private <T extends Dao> Boolean exec(Boolean exec, Class<T>... tables) {
        if(tables.length == 0) return false;
        boolean success = true;
        SQLiteDatabase db = this.getDbHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            success &= exec(exec, db, this.mDbMapping, tables);
            if(success) db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return success;
    }

    /******************************************************************************************************************/

    public void execSQL(String sql) {
        SQLiteDatabase db = this.getDbHelper().getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void execSQL(String sql, Object[] bindArgs) {
        SQLiteDatabase db = this.getDbHelper().getWritableDatabase();
        db.execSQL(sql, bindArgs);
        db.close();
    }

    public Query rawQuery(String sql, String[] selectionArgs) {
        return Select.rawQuery(this, sql, selectionArgs);
    }

    /**************************************************************************************************************/

    public SQLiteOpenHelper getDbHelper() {
        return this.mDbHelper;
    }

    @Override
    public <T extends Dao> Set<Class<T>> getTables() {
        return this.mDbMapping.getTables();
    }

    @Override
    public <T extends Dao> DbMapper getMapper(Class<T> table, boolean check) {
        return this.mDbMapping.getMapper(table, check);
    }

    @Override
    public <T extends Dao> Converter<T> getConverter(Class<T> table) {
        return this.mDbMapping.getConverter(table);
    }

    public static class Builder implements DbMapping {

        private final Context                               mContext;
        private final Map<Class<? extends Dao>, DbMapper>   mTables;
        private final Map<Class<? extends Dao>, Converter>  mConverters;
        private SQLiteDatabase.CursorFactory                mCursorFactory;
        private String                                      mDatabaseName;
        private int                                         mDatabaseVersion;
        private ReflectConverter                            mReflectConverter;

        public Builder(Context context) {
            this.mTables        = new HashMap<Class<? extends Dao>, DbMapper>();
            this.mConverters    = new HashMap<Class<? extends Dao>, Converter>();
            this.mContext       = context;
        }

        public Context getContext() {
            return this.mContext;
        }

        public String getDatabaseName() {
            return this.mDatabaseName;
        }

        public int getDatabaseVersion() {
            return this.mDatabaseVersion;
        }

        public SQLiteDatabase.CursorFactory getCursorFactory() {
            return this.mCursorFactory;
        }

        public Builder setDatabaseName(String name) {
            this.mDatabaseName = name;
            return this;
        }

        public Builder setDatabaseVersion(int version) {
            this.mDatabaseVersion = version;
            return this;
        }

        public Builder setCursorFactory(SQLiteDatabase.CursorFactory cursorFactory) {
            this.mCursorFactory = cursorFactory;
            return this;
        }

        /**************************************************************************************************************/

        @Override
        public Set<Class<? extends Dao>> getTables() {
            return this.mTables.keySet();
        }

        @Override
        public synchronized <T extends Dao> DbMapper getMapper(Class<T> table, boolean check) {
            DbMapper mapper = null;
            if(this.mTables.containsKey(table)) {
                mapper = this.mTables.get(table);
                if(mapper == null) {
                    mapper = DbUtils.createMapper(this, table);
                    this.mTables.put(table, mapper);
                }
            }
            if(check) DbException.checkMapper(mapper);
            return mapper;
        }

        @Override
        public <T extends Dao> Converter<T> getConverter(Class<T> table) {
            if(this.mConverters.containsKey(table))
                return this.mConverters.get(table);

            if(this.mReflectConverter == null)
                this.mReflectConverter = new ReflectConverter<T>(this);

            return this.mReflectConverter;
        }

        /**************************************************************************************************************/

        public Builder addConverters(Converter<? extends Dao>... converters) {
            if(converters.length == 0) return this;
            return this.addConverters(Arrays.asList(converters));
        }

        public Builder addConverters(List<Converter<? extends Dao>> converters) {
            return this.setConverters(converters, true);
        }

        public Builder setConverters(Converter<? extends Dao>... converters) {
            if(converters.length == 0) return this;
            return this.setConverters(Arrays.asList(converters));
        }

        public Builder setConverters(List<Converter<? extends Dao>> converters) {
            return this.setConverters(converters, false);
        }

        private Builder setConverters(List<Converter<? extends Dao>> converters, boolean append) {
            if(!append && !this.mConverters.isEmpty())
                this.mConverters.clear();

            if(converters != null && !converters.isEmpty()) {
                for (Converter<? extends Dao> converter : converters) {
                    this.mConverters.put(converter.getTable(), converter);
                }
            }
            return this;
        }

        /**************************************************************************************************************/

        public Builder addModels(Class<? extends Dao>... tables) {
            if(tables.length == 0) return this;
            return this.addModels(Arrays.asList(tables));
        }

        public Builder addModels(List<Class<? extends Dao>> tables) {
            return this.setModels(tables, true);
        }

        public Builder setModels(Class<? extends Dao>... tables) {
            if(tables.length == 0) return this;
            return this.setModels(Arrays.asList(tables));
        }

        public Builder setModels(List<Class<? extends Dao>> tables) {
            return this.setModels(tables, false);
        }

        private Builder setModels(List<Class<? extends Dao>> tables, boolean append) {
            if(!append && !this.mTables.isEmpty())
                this.mTables.clear();

            if(tables != null && !tables.isEmpty()) {
                for (Class<? extends Dao> table : tables) {
                    this.mTables.put(table, null);
                }
            }
            return this;
        }

        /**************************************************************************************************************/

        public DbContext build(SQLiteOpenHelper dbHelper) {
            return new DbContext(this, dbHelper);
        }

        public DbContext build() {
            final DbMapping mapping = this;
            return new DbContext(this, new SQLiteOpenHelper(
                    this.getContext(),
                    this.getDatabaseName(),
                    this.getCursorFactory(),
                    this.getDatabaseVersion()) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    DbContext.createTables(db, mapping);
                }
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                }
            });
        }
    }

    /**************************************************************************************************************/

    public static <T extends Dao> void createTables(SQLiteDatabase db, DbMapping mapping, Class<T>... tables) {
        List<Class<T>> daos = tables.length == 0 ? new ArrayList(mapping.getTables()) : Arrays.asList(tables);
        if(daos.isEmpty()) return;
        exec(true, db, mapping, tables);
    }

    private static <T extends Dao> Boolean exec(Boolean exec, SQLiteDatabase db, DbMapping mapping, Class<T>... tables) {
        boolean success = true;
        for (Class<T> table : tables) {
            DbMapper mapper = mapping.getMapper(table, true);
            if(exec == null) {
                success &= new Truncate(mapper, db).exec();
            } else if(exec) {
                success &= new Create(mapper, db).exec();
            } else {
                success &= new Drop(mapper, db).exec();
            }
        }
        return success;
    }

}