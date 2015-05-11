package android.database.orm.test.dao;

import android.database.orm.annotation.Column;
import android.database.orm.annotation.Table;

/**
 * Created by yongjiu on 15/5/1.
 */
@Table("tbl_user")
public class UserDao extends TestDao {

    @Column("Name")
    public String name;

    @Column("Gender")
    public Boolean gender;

    @Column("Age")
    public Integer age;

}