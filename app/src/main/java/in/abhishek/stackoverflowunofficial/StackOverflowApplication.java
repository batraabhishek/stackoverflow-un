package in.abhishek.stackoverflowunofficial;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by abhishek on 16/09/17
 */

public class StackOverflowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
