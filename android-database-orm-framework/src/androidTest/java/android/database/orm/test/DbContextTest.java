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
        Boolean success = this.mDbContext.create(TestDao.class);
        Assert.assertTrue(success);
    }

    @Override
    public void testTruncateMethod() {
        Boolean success = this.mDbContext.truncate(TestDao.class);
        Assert.assertTrue(success);
    }

    @Override
    public void testDropMethod() {
        Boolean success = this.mDbContext.drop(TestDao.class);
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
        Query query1        = this.mDbContext.select(UserDao.class).where("1=1").exec(); // select all
        UserDao dao1        = query1.singleOrNull(UserDao.class);

        Query query2        = this.mDbContext.select(UserDao.class).distinct().groupBy("").orderBy("").having("").limit(100).exec();
        List<UserDao> list2 = query2.list(UserDao.class);

        // EXEC SELECT ID, Name, Gender, Age from TABLE;
        Query query3        = this.mDbContext.select(UserDao.class, "ID", "Name", "Gender", "Age").exec(); // select all
        List<UserDao> list3 = query3.list(UserDao.class);
    }

    @Override
    public void testInsertMethod() {
        Result result1  = this.mDbContext.insert(UserDao.class)
                .values()
                .set("Name", "yongjiu")
                .set("Gender", false)
                .setNull("Age")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = this.mDbContext.insert(UserDao.class).insert(new UserDao());
        Assert.assertTrue(result2.success());
    }

    @Override
    public void testUpdateMethod() {
        Result result1  = this.mDbContext.update(UserDao.class)
                .set("Name", "yongjiu")
                .set("Gender", true)
                .setNull("Age")
                .where("ID=?", "1")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = this.mDbContext.update(UserDao.class).update(new UserDao(), "ID=?", "1");
        Assert.assertTrue(result2.success());
    }

}