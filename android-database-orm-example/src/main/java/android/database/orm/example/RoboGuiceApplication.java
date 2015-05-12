package android.database.orm.example;

import android.content.Context;
import android.database.orm.DbContext;
import android.database.orm.core.db.DbContextBuilder;
import com.google.inject.Module;
import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
public class RoboGuiceApplication extends RoboApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void addApplicationModules(List<Module> modules) {
        super.addApplicationModules(modules);
        modules.add(new OrmModule(this.getApplicationContext()));
    }

    static class OrmModule extends AbstractAndroidModule {
        private final Context mContext;
        public OrmModule(Context context) {
            this.mContext = context;
        }
        @Override
        protected void configure() {
            DbContext dbContext = DbContextBuilder.newInstance(this.mContext);
            this.bind(DbContext.class).toInstance(dbContext);
        }
    }
}