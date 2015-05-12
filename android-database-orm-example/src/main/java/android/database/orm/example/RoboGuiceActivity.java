package android.database.orm.example;

import android.database.orm.DbContext;
import android.database.orm.core.repository.UserRepository;
import android.os.Bundle;
import roboguice.activity.RoboActivity;

/**
 * Created by yongjiu on 15/5/1.
 */
public class RoboGuiceActivity extends RoboActivity {

    @com.google.inject.Inject
    private DbContext mDbContext;
    private UserRepository mUserRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mUserRepository = new UserRepository(this.mDbContext);
        this.mUserRepository.fireTestMethod();
    }

}