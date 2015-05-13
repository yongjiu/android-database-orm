package android.database.orm.test;

import android.database.orm.sql.*;
import android.database.orm.test.dao.*;
import junit.framework.Assert;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbContextTest extends ApplicationTest {

    @Override
    public void testCreateMethod() {
        Boolean success = this.mDbContext.create(SessionDao.class);
        Assert.assertTrue(success);
    }

    @Override
    public void testTruncateMethod() {
        Boolean success = this.mDbContext.truncate(UserDao.class);
        Assert.assertTrue(success);
    }

    @Override
    public void testDropMethod() {
        Boolean success = this.mDbContext.drop(MessageDao.class);
        Assert.assertTrue(success);
    }

    @Override
    public void testDeleteMethod() {
        Result result1  = this.mDbContext.delete(UserDao.class).exec(); // delete all
        Assert.assertTrue(result1.success());

        Result result2  = this.mDbContext.delete(UserDao.class).exec("1=1");
        Assert.assertTrue(result2.success());

        Result result3  = this.mDbContext.delete(UserDao.class).where("1=1").exec();
        Assert.assertTrue(result3.success());
    }

    @Override
    public void testSelectMethod() {
        // EXEC SELECT * from TABLE WHERE 1=1;
        Query query1        = this.mDbContext.select(UserDao.class).where("1=1").exec();
        UserDao dao1        = query1.singleOrNull(UserDao.class);

        Query query2        = this.mDbContext.select(UserDao.class).distinct().groupBy("").orderBy("").having("").limit(100).exec();
        List<UserDao> list2 = query2.list(UserDao.class);

        // EXEC SELECT ID, Name, Gender, Age from TABLE;
        Query query3        = this.mDbContext.select(UserDao.class, UserDao.COLUMN_ID, UserDao.COLUMN_NAME, UserDao.COLUMN_GENDER, UserDao.COLUMN_AGE).exec(); // select all
        List<UserDao> list3 = query3.list(UserDao.class);
    }

    @Override
    public void testInsertMethod() {
        Result result1  = this.mDbContext.insert(UserDao.class)
                .values()
                .set(UserDao.COLUMN_NAME, "yongjiu")
                .set(UserDao.COLUMN_GENDER, false)
                .setNull(UserDao.COLUMN_AGE)
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = this.mDbContext.insert(UserDao.class).insert(new UserDao());
        Assert.assertTrue(result2.success());
    }

    @Override
    public void testUpdateMethod() {
        String clause = String.format("%s=?", UserDao.COLUMN_ID);
        Result result1  = this.mDbContext.update(UserDao.class)
                .set(UserDao.COLUMN_NAME, "yongjiu")
                .set(UserDao.COLUMN_GENDER, true)
                .setNull(UserDao.COLUMN_AGE)
                .where(clause, "1")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = this.mDbContext.update(UserDao.class).update(new UserDao(), clause, "1");
        Assert.assertTrue(result2.success());
    }

}