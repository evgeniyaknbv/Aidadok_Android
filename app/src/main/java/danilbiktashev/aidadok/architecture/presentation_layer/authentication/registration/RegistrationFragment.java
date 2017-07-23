package danilbiktashev.aidadok.architecture.presentation_layer.authentication.registration;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;

import danilbiktashev.aidadok.R;
import danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter.EnterErrorDialog;
import danilbiktashev.aidadok.architecture.presentation_layer.main_content.MainActivity;

/**
 * Created by User on 21.07.2017.
 */

public class RegistrationFragment extends Fragment   implements View.OnClickListener{

    private static final String TAG = "RegistrationFragment";

    private EditText password;
    private EditText email;
    private EditText userName;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();

        root.findViewById(R.id.registration_button).setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        userName = (EditText) root.findViewById(R.id.user_name);

        setupToolBar(root);

        return root;
    }

    private void setupToolBar(View root){
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.registration);
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
            if(isAllFilled()){
                createAccount(email.getText().toString(), password.getText().toString());
            }else {
                EnterErrorDialog errorDialog = EnterErrorDialog.newInstance("Необходимо заполнить все поля");
                errorDialog.show(getFragmentManager(), "EnterErrorDialog");
            }
        }
    }

    private boolean isAllFilled(){
        return email.getText().length() > 0 && password.getText().length() > 0 && userName.getText().length() > 0;
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
//                    изменяе профиль, записываем имя пользователя
               setupUserProfile(userName.getText().toString());
            }else {
               task.getException().printStackTrace();

            }
        });
    }

    private void setupUserProfile(String userName){
        Log.d(TAG, "setupUserProfile: userName=" + userName);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        if(mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()){
                    Log.d(TAG, "setupUserProfile: userName = " + mAuth.getCurrentUser().getDisplayName());
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    task1.getException().printStackTrace();
                    EnterErrorDialog errorDialog = EnterErrorDialog.newInstance("Проверте подключение к интернету");
                    errorDialog.show(getFragmentManager(), "EnterErrorDialog");
                }
            });
        }
    }
}
