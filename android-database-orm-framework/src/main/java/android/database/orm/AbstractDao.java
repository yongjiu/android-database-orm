package android.database.orm;

import android.database.orm.sql.Result;

/**
 * Created by yongjiu on 15/5/1.
 */
public abstract class AbstractDao implements Dao {

    private final DbContext mDbContext;
    public AbstractDao(DbContext dbContext) {
        this.mDbContext = dbContext;
    }

    protected DbContext getDbContext() {
        return this.mDbContext;
    }

    public Result insert() {
        return this.mDbContext.insert(this);
    }

    public Result delete() {
        Where where = this.onGetWhereByKeys();
        return this.mDbContext.delete(this.getClass(), where.clause, where.args);
    }

    public Result update() {
        Where where = this.onGetWhereByKeys();
        return this.mDbContext.update(this, where.clause, where.args);
    }

    protected Where onGetWhereByKeys() {
        Where where = this.getWhereByKeys();
        if(where == null) throw new IllegalArgumentException("where");
        return where;
    }

    protected abstract Where getWhereByKeys();

    public static class Where {
        public final String clause;
        public final String[] args;
        public Where(String clause, String... args) {
            this.clause = clause;
            this.args = args;
        }
    }

}