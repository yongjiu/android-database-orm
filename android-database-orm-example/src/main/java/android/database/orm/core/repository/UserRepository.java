package android.database.orm.core.repository;

import android.database.orm.DbContext;
import android.database.orm.core.db.DbContextProvider;
import android.database.orm.core.db.dao.UserDao;
import android.database.orm.sql.Query;
import android.database.orm.sql.Result;

/**
 * Created by yongjiu on 15/5/1.
 */
public class UserRepository extends AbstractRepository<UserDao> {

    public UserRepository(DbContext context) {
        super(context);
    }

    public UserRepository(DbContextProvider provider) {
        super(provider);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public UserDao getItem(int index) {
        return null;
    }

    public void fireTestMethod() {
        DbContext dbContext = this.getDbContext();

        UserDao dao     = new UserDao();
        dao.name        = "yongjiu";
        dao.avatar      = "avatar.1.png";
        dao.created     = System.currentTimeMillis();
        Result result   = dbContext.insert(dao);

        if(!result.success()) return;

        String user_id  = 1 + "";
        String clause   = String.format("%s = ?", UserDao.COLUMN_ID);
        Query query     = dbContext.select(UserDao.class).where(clause, user_id).exec();
        UserDao user    = query.singleOrNull(UserDao.class);

        // ...
    }
}