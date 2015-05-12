package android.database.orm.core.db;

import android.content.Context;
import android.database.orm.DbContext;
import android.database.orm.core.db.converter.*;
import android.database.orm.core.db.dao.*;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbContextBuilder {

    public static DbContext newInstance(Context context) {
        return new DbContext.Builder(context)
                .setDatabaseName("example.orm.db")
                .setDatabaseVersion(1551)
                .addModels(UserDao.class, SessionDao.class, MessageDao.class)
                .addConverters(new UserConverter(), new MessageConverter())
                .build();
    }

}