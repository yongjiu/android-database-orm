package android.database.orm.sql;

import android.database.Cursor;
import android.database.orm.Dao;
import android.database.orm.DbContext;
import android.database.orm.DbMapper;
import android.database.orm.DbMapping;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongjiu on 15/5/1.
 */
public class Select extends QueryExecutable {

    private final From      mFrom;
    private final String[]  mColumns;
    private boolean         mDistinct;
    private String          mGroupBy;
    private String          mOrderBy;
    private String          mHaving;
    private Integer         mLimit;

    public Select(From from, String... columns) {
        this.mFrom      = from;
        this.mColumns   = columns;
        this.mDistinct  = false;
    }

    public Select distinct() {
        this.mDistinct = true;
        return this;
    }

    public Select groupBy(String groupBy) {
        this.mGroupBy = groupBy;
        return this;
    }

    public Select orderBy(String orderBy) {
        this.mOrderBy = orderBy;
        return this;
    }

    public Select having(String having) {
        this.mHaving = having;
        return this;
    }

    public Select limit(int limit) {
        this.mLimit = limit;
        return this;
    }

    public Where where(String clause, String... args) {
        return new Where(this, clause, args);
    }

    /******************************************************************************************************************/

    private synchronized Query exec(android.database.orm.sql.Where where) {
        DbContext dbContext = this.mFrom.getDbContext();
        SQLiteDatabase db   = dbContext.getDbHelper().getReadableDatabase();
        DbMapper mapper     = this.mFrom.getMapper();
        String table        = mapper.getName();
        String limit        = this.mLimit == null ? null : String.valueOf(this.mLimit);
        return new Query(dbContext, db, db.query(
                this.mDistinct,
                table,
                this.mColumns,
                where.getClause(),
                where.getArgs(),
                this.mGroupBy,
                this.mHaving,
                this.mOrderBy,
                limit));
    }

    @Override
    public Query exec() {
        return this.exec(Where.All);
    }

    /******************************************************************************************************************/

    public static Query rawQuery(DbContext context, String sql, String[] selectionArgs) {
        SQLiteDatabase db = context.getDbHelper().getReadableDatabase();
        return Select.rawQuery(context, db, sql, selectionArgs);
    }

    public static Query rawQuery(DbMapping mapping, SQLiteDatabase db, String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return new Query(mapping, db, cursor);
    }

    /******************************************************************************************************************/

    public static class Where<T extends Dao> extends android.database.orm.sql.Where<Query> {

        private final Select mSelect;
        Where(Select select, String clause, String... args) {
            super(clause, args);
            this.mSelect = select;
        }

        public Select groupBy(String groupBy) {
            return this.mSelect.groupBy(groupBy);
        }

        public Select orderBy(String orderBy) {
            return this.mSelect.orderBy(orderBy);
        }

        public Select having(String having) {
            return this.mSelect.having(having);
        }

        public Select limit(int limit) {
            return this.mSelect.limit(limit);
        }

        @Override
        public Query exec() {
            return this.mSelect.exec(this);
        }
    }
}