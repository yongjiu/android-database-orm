package android.database.orm.core.repository;

import android.database.orm.DbContext;
import android.database.orm.core.db.DbContextProvider;

/**
 * Created by yongjiu on 15/5/1.
 */
public abstract class AbstractRepository<T> implements Repository<T> {

    private final DbContext mDbContext;

    public DbContext getDbContext() {
        return this.mDbContext;
    }

    public AbstractRepository(DbContext context) {
        this.mDbContext = context;
    }

    public AbstractRepository(DbContextProvider provider) {
        this(provider.getDbContext());
    }

}