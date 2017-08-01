package danilbiktashev.aidadok.architecture.presentation_layer.authentication;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import danilbiktashev.aidadok.R;
import danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter.EnterFragment;
import danilbiktashev.aidadok.architecture.presentation_layer.authentication.registration.RegistrationFragment;
import danilbiktashev.aidadok.architecture.presentation_layer.main_content.MainActivity;

/**
 * Created by User on 21.07.2017.
 */

public   class LoginFragment extends Fragment  implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        root.findViewById(R.id.enter_button).setOnClickListener(this);
        root.findViewById(R.id.registration_button).setOnClickListener(this);
        root.findViewById(R.id.continue_text).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.enter_button:
               replaceFragment(new EnterFragment(), "EnterFragment", true);
               break;
           case R.id.registration_button:
               replaceFragment(new RegistrationFragment(), "RegistrationFragment", true);
               break;
           case R.id.continue_text:
               openNewsActivity();
               break;
       }
    }


    private void replaceFragment(Fragment fragment, String tag, boolean addTobackStack){
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag);

        if(addTobackStack) ft.addToBackStack(null);

        ft.commit();
    }

    private void openNewsActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
