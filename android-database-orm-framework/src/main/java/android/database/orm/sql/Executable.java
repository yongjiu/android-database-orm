package android.database.orm.sql;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface Executable<T> {
    T exec();
}

abstract class QueryExecutable implements Executable<Query> {

}

abstract class ResultExecutable implements Executable<Result> {

}

abstract class BooleanExecutable implements Executable<Boolean> {

}