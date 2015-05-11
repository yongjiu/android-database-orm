package android.database.orm.test;

import android.database.orm.sql.*;
import android.database.orm.test.dao.*;
import junit.framework.Assert;
import java.util.List;

/**
 * Created by yongjiu on 15/5/1.
 */
public class SqlTest extends ApplicationTest {

    @Override
    public void testCreateMethod() {
        Boolean success = new Create(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

    @Override
    public void testTruncateMethod() {
        Boolean success = new Truncate(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

    @Override
    public void testDropMethod() {
        Boolean success = new Drop(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

    @Override
    public void testDeleteMethod() {
        From from       = new From(this.mDbContext, UserDao.class);

        Result result1  = from.delete().exec("1=1");
        Assert.assertTrue(result1.success());

        Result result2  = from.delete().where("1=1").exec();
        Assert.assertTrue(result2.success());

        Result result3  = new Delete(from).exec();
        Assert.assertTrue(result3.success());
    }

    @Override
    public void testSelectMethod() {
        From from       = new From(this.mDbContext, UserDao.class);

        // EXEC SELECT * from TABLE WHERE 1=1;
        Query query1        = from.select().where("1=1").exec(); // select all
        UserDao dao         = query1.single(UserDao.class, 0);

        Query query2        = from.select().distinct().groupBy("").orderBy("").having("").limit(100).exec();
        List<UserDao> list2 = query2.list(UserDao.class);

        // EXEC SELECT ID, Name, Gender, Age from TABLE;
        Query query3        = new Select(from, "ID", "Name", "Gender", "Age").exec();
        List<UserDao> list3 = query3.list(UserDao.class);

    }

    @Override
    public void testInsertMethod() {
        Result result = new Insert(this.mDbContext, UserDao.class)
                .values()
                .set("Name", "yongjiu")
                .set("Gender", false)
                .setNull("Age")
                .exec();
        Assert.assertTrue(result.success());
    }

    @Override
    public void testUpdateMethod() {
        Result result1 = new Update(this.mDbContext, UserDao.class)
                .set("Name", "yongjiu")
                .set("Gender", true)
                .setNull("Age")
                .where("ID=?", "1")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = new Update(this.mDbContext, UserDao.class).update(new UserDao(), "ID=?", "1");
        Assert.assertTrue(result2.success());
    }

}