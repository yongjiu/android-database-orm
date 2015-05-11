package android.database.orm.serializer;

/**
 * Created by yongjiu on 15/5/1.
 */
public interface TypeSerializer {
    Class<?> getDeserializedType();
    Class<?> getSerializedType();
    Object serialize(Object data);
    Object deserialize(Object data);
}