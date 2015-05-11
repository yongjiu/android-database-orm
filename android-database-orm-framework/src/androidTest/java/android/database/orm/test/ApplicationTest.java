package android.database.orm.test;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.orm.DbContext;
import android.database.orm.converter.Converter;
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
                ).addConverters(
                        new Converter<UserDao>() {
                            @Override
                            public Class<UserDao> getTable() {
                                return UserDao.class;
                            }
                            @Override
                            public ContentValues toContentValues(UserDao dao) {
                                ContentValues contentValues = new ContentValues();
                                // ...
                                return contentValues;
                            }
                            @Override
                            public UserDao toDao(Cursor cursor, Class<UserDao> table) {
                                UserDao dao = new UserDao();
                                // ...
                                return dao;
                            }
                        },
                        new Converter<SessionDao>() {
                            @Override
                            public Class<SessionDao> getTable() {
                                return SessionDao.class;
                            }
                            @Override
                            public ContentValues toContentValues(SessionDao dao) {
                                ContentValues contentValues = new ContentValues();
                                // ...
                                return contentValues;
                            }
                            @Override
                            public SessionDao toDao(Cursor cursor, Class<SessionDao> table) {
                                SessionDao dao = new SessionDao();
                                // ...
                                return dao;
                            }
                        },
                        new Converter<MessageDao>() {
                            @Override
                            public Class<MessageDao> getTable() {
                                return MessageDao.class;
                            }
                            @Override
                            public ContentValues toContentValues(MessageDao dao) {
                                ContentValues contentValues = new ContentValues();
                                // ...
                                return contentValues;
                            }
                            @Override
                            public MessageDao toDao(Cursor cursor, Class<MessageDao> table) {
                                MessageDao dao = new MessageDao();
                                // ...
                                return dao;
                            }
                        }
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