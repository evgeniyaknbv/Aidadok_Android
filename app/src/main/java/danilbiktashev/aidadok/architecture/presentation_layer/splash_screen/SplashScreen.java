package danilbiktashev.aidadok.architecture.presentation_layer.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import danilbiktashev.aidadok.architecture.presentation_layer.authentication.LoginActivity;

/**
 * Created by User on 16.07.2017.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        кусочек для пример загузки данных, убрать а релизе
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startLoginActivity();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
