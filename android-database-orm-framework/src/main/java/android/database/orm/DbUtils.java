package android.database.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbUtils {

    public static final String TAG = "DbUtils";

    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    /******************************************************************************************************************/

    public static <T extends Dao> T toObject(DbMapping dbMapping, Cursor cursor, Class<T> table) {
        try {
            DbMapper mapper = dbMapping.getMapper(table, true);
            T t = table.newInstance();
            int columnCount = cursor.getColumnCount();
            for (int columnIndex=0; columnIndex<columnCount; columnIndex++) {
                String columnName = cursor.getColumnName(columnIndex);
                DbMapper.Mapping map = mapper.getMapping(columnName);
                try {
                    Object value = null;
                    Field field = map.getField();
                    Class<?> type = field.getType();
                    if(cursor.isNull(columnIndex)) {
                        // do nothing
                    } else if(type == String.class) {
                        value = cursor.getString(columnIndex);
                    } else if (type.equals(Character.class) || type.equals(char.class)) {
                        value = cursor.getString(columnIndex).charAt(0);
                    } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                        value = cursor.getInt(columnIndex) != 0;
                    } else if(type == Byte.class || type == byte.class) {
                        value = cursor.getInt(columnIndex);
                    } else if(type == Byte[].class || type == byte[].class) {
                        value = cursor.getBlob(columnIndex);
                    } else if(type == Double.class || type == double.class) {
                        value = cursor.getDouble(columnIndex);
                    } else if(type == Float.class || type == float.class) {
                        value = cursor.getFloat(columnIndex);
                    } else if(type == Integer.class || type == int.class) {
                        value = cursor.getInt(columnIndex);
                    } else if(type == Long.class || type == long.class) {
                        value = cursor.getLong(columnIndex);
                    } else if(type == Short.class || type == short.class) {
                        value = cursor.getShort(columnIndex);
                    }
                    if(field.isAccessible() == false)
                        field.setAccessible(true);
                    field.set(t, value);
                } catch (java.lang.IllegalAccessException e) {
                    e.printStackTrace();
                    Log.d(TAG, String.format("toObject.set @ %s\n%s", e, e.getMessage()));
                } catch (java.lang.IllegalArgumentException e) {
                    e.printStackTrace();
                    Log.d(TAG, String.format("toObject.set @ %s\n%s", e, e.getMessage()));
                }
            }
            return t;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            Log.d(TAG, String.format("toObject.newInstance @ %s\n%s", e, e.getMessage()));
            return null;
        } catch (java.lang.IllegalAccessException e) {
            e.printStackTrace();
            Log.d(TAG, String.format("toObject.newInstance @ %s\n%s", e, e.getMessage()));
            return null;
        }
    }

    public static void putContentValue(ContentValues cv, String column, Object value) {
        if(value == null) {
            cv.putNull(column);
        } else if(value instanceof String) {
            cv.put(column, String.valueOf(value));
        } else if(value instanceof Byte) {
            cv.put(column, (Byte)value);
        } else if(value instanceof Short) {
            cv.put(column, (Short)value);
        } else if(value instanceof Integer) {
            cv.put(column, (Integer)value);
        } else if(value instanceof Long) {
            cv.put(column, (Long)value);
        } else if(value instanceof Float) {
            cv.put(column, (Float)value);
        } else if(value instanceof Double) {
            cv.put(column, (Double)value);
        } else if(value instanceof Boolean) {
            cv.put(column, (Boolean)value);
        } else if(value instanceof byte[]) {
            cv.put(column, (byte[])value);
        } else {
            Log.d(TAG, String.format("putContentValue @ %s:%s",  column, value));
        }
    }

    public static <T extends Dao> ContentValues toContentValues(DbMapper dbMapper, T dao) {
        ContentValues contentValues = new ContentValues();
        if(dbMapper == null || dao == null) return contentValues;
        Set<String> mappings = dbMapper.getMappings();
        for (String name : mappings) {
            try {
                DbMapper.Mapping mapping = dbMapper.getMapping(name);
                Field field = mapping.getField();
                if(!field.isAccessible())
                    field.setAccessible(true);
                Object value = field.get(dao);
                DbUtils.putContentValue(contentValues, name, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d(TAG, String.format("toContentValues.get @ %s\n%s", e, e.getMessage()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.d(TAG, String.format("toContentValues.get @ %s\n%s", e, e.getMessage()));
            }
        }
        return contentValues;
    }

    public static ContentValues toContentValues(String[] columns, Object[] values) {
        if(columns == null || values == null || columns.length != values.length)
            throw new IllegalArgumentException();
        ContentValues cv = new ContentValues();
        for (int i=0; i<columns.length; i++) {
            DbUtils.putContentValue(cv, columns[i], values[i]);
        }
        return cv;
    }

    /**************************************************************************************************************/

    static DbMapper createMapper(DbMapping mapping, Class<? extends Dao> table) {
        Map<String, DbMapper.Mapping> columns = new LinkedHashMap<String, DbMapper.Mapping>();
        Table t = table.isAnnotationPresent(Table.class) ? table.getAnnotation(Table.class) : null;
        DbUtils.findMappings(mapping, table, t, columns);
        return new DbMapper(mapping, table, t, columns);
    }

    static void findMappings(DbMapping mapping, Class<?> type, Table table, Map<String, DbMapper.Mapping> columns) {
        Class<?> sc = type.getSuperclass();
        if(sc != null && !sc.equals(Dao.class) && !sc.equals(Object.class)) {
            DbUtils.findMappings(mapping, sc, table, columns);
        }

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(Column.class)) continue;

            Column column = field.getAnnotation(Column.class);
            String columnName = column == null ? null : column.value();
            if(isEmpty(columnName)) columnName = field.getName();

            columns.put(columnName, new DbMapper.Mapping(mapping, table, field, column, columnName));
        }
    }

    /******************************************************************************************************************/

    static String createTableDefinition(DbMapper dbMapper, String format) {
        List<String> definitions = new ArrayList<String>();
        Set<String> columns = dbMapper.getMappings();
        for (String column : columns) {
            DbMapper.Mapping map = dbMapper.getMapping(column);
            definitions.add(map.toString(dbMapper));
        }
        return String.format(format, dbMapper.getName(), android.text.TextUtils.join(", ", definitions));
    }

    static String createColumnDefinition(DbMapping dbMapping, DbMapper dbMapper, DbMapper.Mapping mapping) {
        StringBuilder definition = new StringBuilder();
        Field field = mapping.getField();
        Class<?> type = field.getType();
        final String name = mapping.getName();
        final Column column = mapping.getColumn();

        if (DbUtils.TYPE_MAP.containsKey(type)) {
            definition.append(name);
            definition.append(" ");
            definition.append(DbUtils.TYPE_MAP.get(type).toString());
        } else if (DbUtils.isSubclassOf(type, Enum.class)) {
            definition.append(name);
            definition.append(" ");
            definition.append(DbUtils.SQLiteType.TEXT.toString());
        }

        if (TextUtils.isEmpty(definition)) {
            Log.e(TAG, "createColumnDefinition @ No type mapping for: " + type.toString());
        } else {

        }
        return definition.toString();
    }

    /******************************************************************************************************************/

    static boolean isSubclassOf(Class<?> type, Class<?> superClass) {
        Class<?> cls = type.getSuperclass();
        if (cls != null) {
            if (cls.equals(superClass)) {
                return true;
            }
            return isSubclassOf(cls, superClass);
        }
        return false;
    }

    static boolean isModel(Class<?> type) {
        return isSubclassOf(type, Dao.class) && !type.isInterface() && (!Modifier.isAbstract(type.getModifiers()));
    }

    /******************************************************************************************************************/

    static final HashMap<Class<?>, SQLiteType> TYPE_MAP = new HashMap<Class<?>, SQLiteType>() {
        {
            put(CharSequence.class, SQLiteType.TEXT);
            put(String.class,       SQLiteType.TEXT);

            put(char.class,         SQLiteType.TEXT);
            put(Character.class,    SQLiteType.TEXT);

            put(boolean.class,      SQLiteType.INTEGER);
            put(Boolean.class,      SQLiteType.INTEGER);

            put(byte.class,         SQLiteType.INTEGER);
            put(Byte.class,         SQLiteType.INTEGER);

            put(short.class,        SQLiteType.INTEGER);
            put(Short.class,        SQLiteType.INTEGER);

            put(int.class,          SQLiteType.INTEGER);
            put(Integer.class,      SQLiteType.INTEGER);

            put(long.class,         SQLiteType.INTEGER);
            put(Long.class,         SQLiteType.INTEGER);

            put(float.class,        SQLiteType.REAL);
            put(Float.class,        SQLiteType.REAL);

            put(double.class,       SQLiteType.REAL);
            put(Double.class,       SQLiteType.REAL);

            put(byte[].class,       SQLiteType.BLOB);
            put(Byte[].class,       SQLiteType.BLOB);
        }
    };

    /******************************************************************************************************************/

    static enum SQLiteType {
        INTEGER, REAL, TEXT, BLOB
    }

}