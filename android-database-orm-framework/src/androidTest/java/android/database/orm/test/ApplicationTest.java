package android.database.orm.test;

import android.app.Application;
import android.content.Context;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;
import android.database.orm.sql.*;
import android.test.ApplicationTestCase;
import junit.framework.Assert;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private DbContext mDbContext;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = this.getContext().getApplicationContext();
        this.mDbContext = new DbContext.Builder(context)
                .setDatabaseName("orm.test.db")
                .setDatabaseVersion(1)
                .build();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.mDbContext.destroy();
    }

    /**************************************************************************************************************/

    public void testCreateMethod() {
        Boolean success1 = new Create(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success1);

        Boolean success2 = this.mDbContext.create(TestDao.class);
        Assert.assertTrue(success2);
    }

    public void testTruncateMethod() {
        Boolean success1 = new Truncate(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success1);

        Boolean success2 = this.mDbContext.truncate(TestDao.class);
        Assert.assertTrue(success2);
    }

    public void testDropMethod() {
        Boolean success1 = new Drop(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success1);

        Boolean success2 = this.mDbContext.drop(TestDao.class);
        Assert.assertTrue(success2);
    }

    /**************************************************************************************************************/

    public void testDeleteMethod() {
        From from       = new From(this.mDbContext, TestDao.class);

        Result result1  = new Delete(from).exec();
        Assert.assertTrue(result1.success());

        Result result2  = from.delete().exec("1=1");
        Assert.assertTrue(result2.success());

        Result result3  = from.delete().where("1=1").exec();
        Assert.assertTrue(result3.success());

        Result result4  = this.mDbContext.delete(TestDao.class).where("1=1").exec();
        Assert.assertTrue(result4.success());
    }

    public void testSelectMethod() {
        From from           = new From(this.mDbContext, TestDao.class);

        // EXEC SELECT ID, Name, Gender, Age from TABLE;
        Query query1        = new Select(from, "ID", "Name", "Gender", "Age").exec();
        List<TestDao> list1 = query1.list(TestDao.class);

        // EXEC SELECT * from TABLE WHERE 1=1;
        Query query2        = from.select().where("1=1").exec(); // select all
        TestDao dao         = query2.single(TestDao.class, 0);

        Query query3        = from.select().distinct().groupBy("").orderBy("").having("").limit(100).exec();
        List<TestDao> list2 = query1.list(TestDao.class);

        Query query4        = this.mDbContext.select(TestDao.class).where("1=1").exec();

    }

    public void testInsertMethod() {
        Result result1 = new Insert(this.mDbContext, TestDao.class)
                .values()
                .set("Name", "yongjiu")
                .set("Gender", false)
                .setNull("Age")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2  = this.mDbContext.insert(TestDao.class).insert(new TestDao());
        Assert.assertTrue(result2.success());
    }

    public void testUpdateMethod() {
        Result result1 = new Update(this.mDbContext, TestDao.class)
                .set("Name", "yongjiu")
                .set("Gender", true)
                .setNull("Age")
                .where("ID=?", "1")
                .exec();
        Assert.assertTrue(result1.success());

        Result result2 = new Update(this.mDbContext, TestDao.class).update(new TestDao(), "ID=?", "1");
        Assert.assertTrue(result2.success());

        Result result3  = this.mDbContext.update(TestDao.class).update(new TestDao(), "ID=?", "1");
        Assert.assertTrue(result3.success());
    }

}

@Table("tbl_test")
class TestDao implements Dao {

    @Column("ID")
    public Long id;

    @Column("Name")
    public String name;

    @Column("Gender")
    public Boolean gender;

    @Column("Age")
    public Integer age;

}