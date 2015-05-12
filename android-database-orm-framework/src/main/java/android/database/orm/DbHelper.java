package android.database.orm;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbHelper extends SQLiteOpenHelper {

    private final DbMapping mDbMapping;
    public DbHelper(DbContext.Builder builder) {
        super(builder.getContext(),
                builder.getDatabaseName(),
                builder.getCursorFactory(),
                builder.getDatabaseVersion(),
                builder.getDatabaseErrorHandler());
        this.mDbMapping = builder;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DbContext.createTables(db, this.mDbMapping);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DbContext.dropTables(db, this.mDbMapping);
        // this.onCreate(db);
    }

}