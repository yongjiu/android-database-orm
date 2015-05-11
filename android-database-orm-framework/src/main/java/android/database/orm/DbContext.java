package android.database.orm;

import android.content.Context;
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

    public static class Builder implements DbMapping {

        private final Context                               mContext;
        private final Map<Class<? extends Dao>, DbMapper>   mTables;
        private SQLiteDatabase.CursorFactory                mCursorFactory;
        private String                                      mDatabaseName;
        private int                                         mDatabaseVersion;

        public Builder(Context context) {
            this.mTables = new HashMap<Class<? extends Dao>, DbMapper>();
            this.mContext = context;
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
            return this.mTables.get(table);
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

    public static <T extends Dao> void createTables(SQLiteDatabase db, DbMapping mapping, Class<T>... daos) {
        List<Class<T>> tables = daos.length == 0 ? new ArrayList(mapping.getTables()) : Arrays.asList(daos);
        if(tables.isEmpty()) return;
        for (Class<T> table : tables) {
            DbMapper mapper = mapping.getMapper(table, true);
            new Create(mapper, db).exec();
        }
    }

}