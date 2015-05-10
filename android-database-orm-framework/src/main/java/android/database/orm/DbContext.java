package android.database.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbContext implements DbMapping {

    private DbContext(DbMapping mapping, SQLiteOpenHelper dbHelper) {

    }

    public void destroy() {

    }

    public static class Builder implements DbMapping {

        private final Context                               mContext;
        private SQLiteDatabase.CursorFactory                mCursorFactory;
        private String                                      mDatabaseName;
        private int                                         mDatabaseVersion;

        public Builder(Context context) {
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

        public DbContext build(SQLiteOpenHelper dbHelper) {
            return new DbContext(this, dbHelper);
        }

        public DbContext build() {
            return new DbContext(this, new SQLiteOpenHelper(
                    this.getContext(),
                    this.getDatabaseName(),
                    this.getCursorFactory(),
                    this.getDatabaseVersion()) {
                @Override
                public void onCreate(SQLiteDatabase db) {

                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                }
            });
        }
    }

}