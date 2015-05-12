package android.database.orm.core.db.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.DbMapping;
import android.database.orm.DbUtils;
import android.database.orm.core.db.dao.UserDao;

/**
 * Created by yongjiu on 15/5/1.
 */
public class UserConverter extends BaseConverter<UserDao> {

    @Override
    public Class<UserDao> getTable() {
        return UserDao.class;
    }

    @Override
    public ContentValues toContentValues(DbMapping mapping, UserDao dao) {
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_ID,       dao.id);
        values.put(UserDao.COLUMN_NAME,     dao.name);
        values.put(UserDao.COLUMN_AVATAR,   dao.avatar);
        values.put(UserDao.COLUMN_GENDER,   dao.gender);
        values.put(UserDao.COLUMN_AGE,      dao.age);
        values.put(UserDao.COLUMN_CREATED,  dao.created);
        return values;
    }

    @Override
    public UserDao toDao(Cursor cursor, DbMapping mapping, Class<UserDao> table) {
        /*
        UserDao dao     = new UserDao();
        dao.id          = cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_ID));
        dao.name        = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME));
        dao.avatar      = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_AVATAR));

        int gender      = cursor.getColumnIndex(UserDao.COLUMN_GENDER);
        dao.gender      = cursor.isNull(gender) ? null : cursor.getInt(gender) > 0;

        dao.age         = cursor.getInt(cursor.getColumnIndex(UserDao.COLUMN_AGE));
        dao.created     = cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_CREATED));
        */
        UserDao dao = DbUtils.toObject(mapping, cursor, table);
        return dao;
    }

}