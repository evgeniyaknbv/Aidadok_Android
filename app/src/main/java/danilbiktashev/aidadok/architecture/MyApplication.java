package danilbiktashev.aidadok.architecture;
import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Evgeniya on 27.07.2017.
 *
 */

public    class MyApplication extends Application   {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
