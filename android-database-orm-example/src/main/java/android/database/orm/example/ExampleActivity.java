package android.database.orm.example;

import android.app.Activity;
import android.database.orm.DbContext;
import android.database.orm.core.db.DbContextProvider;
import android.database.orm.core.repository.UserRepository;
import android.os.Bundle;

/**
 * Created by yongjiu on 15/5/1.
 */
public class ExampleActivity extends Activity implements DbContextProvider {

    private UserRepository mUserRepository;

    @Override
    public DbContext getDbContext() {
        return ((ExampleApplication)getApplication()).getDbContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mUserRepository = new UserRepository(this);
        this.mUserRepository.fireTestMethod();
    }

}