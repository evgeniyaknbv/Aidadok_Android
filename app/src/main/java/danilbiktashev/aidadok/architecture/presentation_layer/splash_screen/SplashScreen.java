package danilbiktashev.aidadok.architecture.presentation_layer.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import danilbiktashev.aidadok.architecture.presentation_layer.authentication.LoginActivity;
import danilbiktashev.aidadok.architecture.presentation_layer.main_content.MainActivity;

/**
 * Created by User on 16.07.2017.
 *
 */

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(TAG, "onCreate: user != null" );
            Log.d(TAG, "onCreate: user uid=" + user.getUid());
            Log.d(TAG, "onCreate: userName=" + user.getDisplayName());
            startActivity(MainActivity.class);
        }else {
            Log.d(TAG, "onCreate: user == null" );
            startActivity(LoginActivity.class);
        }

    }

    private void startActivity(Class<?> activity){
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
