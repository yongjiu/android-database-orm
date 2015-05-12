package android.database.orm.example;

import android.app.Application;
import android.database.orm.DbContext;
import android.database.orm.core.db.DbContextBuilder;
import android.database.orm.core.db.DbContextProvider;

/**
 * Created by yongjiu on 15/5/1.
 */
public class ExampleApplication extends Application implements DbContextProvider {

    private DbContext mDbContext;

    @Override
    public DbContext getDbContext() {
        return this.mDbContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mDbContext = DbContextBuilder.newInstance(this.getApplicationContext());
    }

}