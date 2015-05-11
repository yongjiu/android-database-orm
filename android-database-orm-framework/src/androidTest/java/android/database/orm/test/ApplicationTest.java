package android.database.orm.test;

import android.app.Application;
import android.content.Context;
import android.database.orm.DbContext;
import android.database.orm.test.dao.*;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public abstract class ApplicationTest extends ApplicationTestCase<Application> {

    protected DbContext mDbContext;

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
                .addModels(
                        UserDao.class,
                        SessionDao.class,
                        MessageDao.class
                ).build();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.mDbContext.destroy();
    }

    public abstract void testCreateMethod();
    public abstract void testTruncateMethod();
    public abstract void testDropMethod();
    public abstract void testDeleteMethod();
    public abstract void testSelectMethod();
    public abstract void testInsertMethod();
    public abstract void testUpdateMethod();

}