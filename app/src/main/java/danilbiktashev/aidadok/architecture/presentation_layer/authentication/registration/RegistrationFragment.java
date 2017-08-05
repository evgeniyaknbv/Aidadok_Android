package danilbiktashev.aidadok.architecture.presentation_layer.authentication.registration;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding2.widget.RxTextView;


import danilbiktashev.aidadok.R;
import danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter.EnterErrorDialog;
import danilbiktashev.aidadok.architecture.presentation_layer.main_content.MainActivity;
import io.reactivex.disposables.CompositeDisposable;

import static danilbiktashev.aidadok.architecture.presentation_layer.authentication.AuthenticationUtils.EMAIL_VALIDATION_REGEX;

/**
 * Created by User on 21.07.2017.
 *
 */

public class RegistrationFragment extends Fragment   implements View.OnClickListener{

    private static final String TAG = "RegistrationFragment";

    private EditText password;
    private EditText email;
    private EditText userName;

    private Button registrationButton;

    private FirebaseAuth mAuth;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        setHasOptionsMenu(true);

       initViews(root);

        setupToolBar(root);

        return root;
    }

    private void setupToolBar(View root){
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.registration);
        }
    }

    private void initViews(View root){
        mAuth = FirebaseAuth.getInstance();

        registrationButton = (Button) root.findViewById(R.id.registration_button);
        registrationButton.setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        userName = (EditText) root.findViewById(R.id.user_name);

        TextInputLayout tilEmail = (TextInputLayout) root.findViewById(R.id.til_email);
        TextInputLayout tilPassword = (TextInputLayout) root.findViewById(R.id.til_password);

        compositeDisposable.add(RxTextView.textChanges(email).skipInitialValue()
                .doOnNext(charSequence -> {
            if(isMailCorrect(charSequence)){

                tilEmail.setError(null);
                tilEmail.setErrorEnabled(false);
                tryEnableRegistrationButton();
            }else {
                registrationButton.setEnabled(false);
                tilEmail.setError("Неверный e-mail");
            }
        }).subscribe());

        compositeDisposable.add(RxTextView.textChanges(password).skipInitialValue()
                .doOnNext(charSequence -> {
                    if(isPasswordCorrect(charSequence)){

                        tilPassword.setError(null);
                        tilPassword.setErrorEnabled(false);
                        tryEnableRegistrationButton();
                    }else {
                        registrationButton.setEnabled(false);
                        tilPassword.setError("Пароль должен быть длиннее 6ти символов");
                    }
                }).subscribe());

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

    public boolean isMailCorrect(CharSequence email) {
        String emailS = email.toString();
        return emailS.matches(EMAIL_VALIDATION_REGEX);
    }

    public boolean isPasswordCorrect(CharSequence password) {
        return password.length() > 5;
    }

    public void tryEnableRegistrationButton(){
        if(email.getText().length() > 0 && password.getText().length() > 0 ){
            if(isMailCorrect(email.getText().toString()) && isPasswordCorrect(password.getText().toString())){
                registrationButton.setEnabled(true);
            }else registrationButton.setEnabled(false);
        }else registrationButton.setEnabled(false);
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
                String errorMsg = task.getException().getMessage();
                Log.d(TAG, "createAccount: " + errorMsg);

                if(errorMsg.contains("The email address is already in use by another account.")){
                    EnterErrorDialog.showErrorDialog(getString(R.string.email_is_already_used), getFragmentManager());
                }else if(errorMsg.contains("Network Error")){
                    EnterErrorDialog.showErrorDialog(getString(R.string.verify_internet_connection), getFragmentManager());
                }
            }
        });
    }

//    в этом методе прописать пользователя в storage
    private void setupUserProfile(String userName){
        Log.d(TAG, "setupUserProfile: userName=" + userName);
        Log.d(TAG, "setupUserProfile: useruid=" + mAuth.getCurrentUser().getUid());

        writeUserToStorage(userName, email.getText().toString());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        if(mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()){
                    Log.d(TAG, "setupUserProfile: userName = " + mAuth.getCurrentUser().getDisplayName());
                    openNewsActivity();
                }else {
                    task1.getException().printStackTrace();
                    EnterErrorDialog.showErrorDialog(getString(R.string.verify_internet_connection), getFragmentManager());
                }
            });
        }
    }

    private void openNewsActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void writeUserToStorage(String userName, String email){

        DatabaseReference.CompletionListener completionListener = (databaseError, databaseReference) -> {

            if(databaseError != null) {
                Log.d(TAG, "writeUserToStorage: " + databaseError.getMessage());
                Log.d(TAG, "writeUserToStorage: " + databaseError.getCode());
                Log.d(TAG, "writeUserToStorage: " + databaseError.getDetails());

            }else {
                Log.d(TAG, "writeUserToStorage: SUCCESS");
            }

        };

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("ПОЛЬЗОВАТЕЛИ");
        DatabaseReference userUID = usersReference.child(mAuth.getCurrentUser().getUid());
        userUID.child("email").setValue(email, completionListener);
        userUID.child("имя_пользователя").setValue(userName, completionListener);
        userUID.push();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
