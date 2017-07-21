package danilbiktashev.aidadok.architecture.presentation_layer.authentication.registration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import danilbiktashev.aidadok.R;

/**
 * Created by User on 21.07.2017.
 */

public class RegistrationFragment extends Fragment   implements View.OnClickListener{

    private EditText password;
    private EditText email;
    private EditText userName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        setHasOptionsMenu(true);

        root.findViewById(R.id.registration_button).setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        userName = (EditText) root.findViewById(R.id.user_name);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.registration);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registration_button){
//
        }
    }
}
