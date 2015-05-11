package android.database.orm.sql;

/**
 * Created by yongjiu on 15/5/1.
 */
public abstract class Where<T> implements Executable<T> {

    public static final Where All = new Where(null) {
        @Override
        public Object exec() {
            return null;
        }
    };

    private final String mClause;
    private final String[] mArgs;

    public String getClause() {
        return this.mClause;
    }

    public String[] getArgs() {
        return this.mArgs;
    }

    Where(String clause, String... args) {
        this.mClause = clause;
        this.mArgs = args;
    }

}