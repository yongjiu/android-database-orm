package android.database.orm.test;

import android.app.Application;
import android.content.Context;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.sql.*;
import android.test.ApplicationTestCase;
import junit.framework.Assert;

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
        Boolean success = new Create(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

    public void testTruncateMethod() {
        Boolean success = new Truncate(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

    public void testDropMethod() {
        Boolean success = new Drop(this.mDbContext, TestDao.class).exec();
        Assert.assertTrue(success);
    }

}

class TestDao implements Dao {

}