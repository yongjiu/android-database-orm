package android.database.orm.core.db.converter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.DbMapping;
import android.database.orm.core.db.dao.MessageDao;

/**
 * Created by yongjiu on 15/5/1.
 */
public class MessageConverter extends BaseConverter<MessageDao> {

    @Override
    public Class<MessageDao> getTable() {
        return MessageDao.class;
    }

    @Override
    public ContentValues toContentValues(DbMapping mapping, MessageDao dao) {
        ContentValues values = new ContentValues();
        values.put(MessageDao.COLUMN_ID,        dao.id);
        values.put(MessageDao.COLUMN_SESSION,   dao.session);
        values.put(MessageDao.COLUMN_USER,      dao.user);
        values.put(MessageDao.COLUMN_CONTENT,   dao.content);
        values.put(MessageDao.COLUMN_TYPE,      dao.type);
        values.put(MessageDao.COLUMN_CREATED,   dao.created);
        return values;
    }

    @Override
    public MessageDao toDao(Cursor cursor, DbMapping mapping, Class<MessageDao> table) {
        MessageDao dao  = new MessageDao();
        dao.id          = cursor.getLong(cursor.getColumnIndex(MessageDao.COLUMN_ID));
        dao.session     = cursor.getLong(cursor.getColumnIndex(MessageDao.COLUMN_SESSION));
        dao.user        = cursor.getLong(cursor.getColumnIndex(MessageDao.COLUMN_USER));
        dao.content     = cursor.getString(cursor.getColumnIndex(MessageDao.COLUMN_CONTENT));
        dao.type        = cursor.getInt(cursor.getColumnIndex(MessageDao.COLUMN_TYPE));
        dao.created     = cursor.getLong(cursor.getColumnIndex(MessageDao.COLUMN_CREATED));
        return dao;
    }

}