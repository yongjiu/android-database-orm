package android.database.orm;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yongjiu on 15/5/1.
 */
public class DbMapper {

    private final Map<String, Mapping>  mMappings;
    private final DbMapping             mDbMapping;
    private final Class<? extends Dao>  mType;
    private final Table                 mTable;
    private final String                mName;

    DbMapper(DbMapping dbMapping, Class<? extends Dao> type, Table table, Map<String, Mapping> mappings) {
        this.mMappings  = new LinkedHashMap<String, Mapping>(mappings);
        this.mDbMapping = dbMapping;
        this.mTable     = table;
        this.mType      = type;

        String tableName = table == null ? null : table.value();
        if(DbUtils.isEmpty(tableName)) tableName = type.getSimpleName();

        this.mName = tableName;
    }

    public DbMapping getDbMapping() {
        return this.mDbMapping;
    }

    public Class<? extends Dao> getType() {
        return this.mType;
    }

    public Set<String> getMappings() {
        return this.mMappings.keySet();
    }

    public Mapping getMapping(String name) {
        return this.mMappings.get(name);
    }

    public String getName() {
        return this.mName;
    }

    public String toString(String format) {
        return DbUtils.createTableDefinition(this, format);
    }

    public <T extends Dao> T newInstance() throws InstantiationException, IllegalAccessException {
        Object instance = this.mType.newInstance();
        return instance == null ? null : (T)instance;
    }

    public static class Mapping {

        private final DbMapping mDbMapping;
        private final Table     mTable;
        private final Field     mField;
        private final Column    mColumn;
        private final String    mName;

        Mapping(DbMapping dbMapping, Table table, Field field, Column column, String name) {
            this.mDbMapping = dbMapping;
            this.mTable     = table;
            this.mField     = field;
            this.mColumn    = column;
            this.mName      = name;
        }

        protected Column getColumn() {
            return this.mColumn;
        }

        public Field getField() {
            return this.mField;
        }

        public String getName() {
            return this.mName;
        }

        protected String toString(DbMapper mapper) {
            return DbUtils.createColumnDefinition(this.mDbMapping, mapper, this);
        }
    }
}