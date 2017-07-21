package danilbiktashev.aidadok.architecture.presentation_layer.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import danilbiktashev.aidadok.R;

/**
 * Created by User on 16.07.2017.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        replaceFragment(new LoginFragment(), "LoginFragment", false);

    }


    public void replaceFragment(Fragment fragment, String tag, boolean addTobackStack){
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag);

        if(addTobackStack) ft.addToBackStack(null);

        ft.commit();
    }
}
