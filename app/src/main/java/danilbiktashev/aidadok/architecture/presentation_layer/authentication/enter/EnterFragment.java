package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import danilbiktashev.aidadok.R;
import danilbiktashev.aidadok.architecture.presentation_layer.main_content.MainActivity;

/**
 * Created by User on 21.07.2017.
 *
 */

public class EnterFragment extends Fragment   implements View.OnClickListener{
    private static final String TAG = "EnterFragment";

    private EditText password;
    private EditText email;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter, container, false);
        setHasOptionsMenu(true);
        initViews(root);
        setupToolBar(root);
        return root;
    }

    private void setupToolBar(View root){
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.enter1);
    }

    private void initViews(View root){
        root.findViewById(R.id.enter_button).setOnClickListener(this);
        root.findViewById(R.id.forget_password).setOnClickListener(this);
        root.findViewById(R.id.facebook).setOnClickListener(this);
        root.findViewById(R.id.vk).setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
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
        switch (v.getId()){
            case R.id.enter_button:
                signInUser(email.getText().toString(), password.getText().toString());
                break;
            case R.id.forget_password:
                sendResetPasswordEmail();
                break;
            case R.id.facebook:
                signInFacebook();
                break;
            case R.id.vk:

                break;
        }
    }

    private void signInUser(String email, String password){
        if(email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    openNewsActivity();
                } else {
                    errorHandling(task.getException().getMessage());
                }
            });
        }else {
            EnterErrorDialog errorDialog = EnterErrorDialog.newInstance("Необходимо заполнить все поля");
            errorDialog.show(getFragmentManager(), "EnterErrorDialog");
        }

    }

    private void sendResetPasswordEmail(){
        SendResetPasswordDialog.showSendEmailDialog(getFragmentManager(),
                email1 -> {
                    Log.d(TAG, "onSendMessage: " + email1);
                    firebaseSendResetPasswordEmail(email1);
                });
    }

    private void firebaseSendResetPasswordEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "письмо отправлено", Toast.LENGTH_SHORT).show();
                    }else {
                        errorHandling(task.getException().getMessage());
                    }
                });
    }

    private void signInFacebook(){
        ArrayList<String> permissions = new ArrayList<>(); permissions.add("email");
        callbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.getMessage());
                error.printStackTrace();
                errorHandling(error.getMessage());
            }
        });

        loginManager.logInWithReadPermissions(this, permissions);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {


                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){

                    Log.d(TAG, "signInWithCredential:success" + user.getUid());

                    for (UserInfo profile : mAuth.getCurrentUser().getProviderData()){

                        writeUserToStorage(profile.getDisplayName(), profile.getEmail(), user.getUid());
                    }
                }

            } else {

                Log.w(TAG, "signInWithCredential:failure", task.getException());
                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void errorHandling(String errorMsg){
        Log.d(TAG, "errorHandling: " + errorMsg);

        errorMsg = errorMsg.toLowerCase();

        if(errorMsg.contains("there is no user record corresponding to this identifier")){
            EnterErrorDialog.showErrorDialog(getString(R.string.no_user_with_such_email), getFragmentManager());

        }else if(errorMsg.contains("network")){
            EnterErrorDialog.showErrorDialog(getString(R.string.verify_internet_connection), getFragmentManager());

        }else if(errorMsg.contains("the email address is badly formatted")){
            EnterErrorDialog.showErrorDialog(getString(R.string.passwor_email_incorrect), getFragmentManager());
        }
    }

    private void writeUserToStorage(String userName, String email, String uid){

        DatabaseReference.CompletionListener completionListener = (databaseError, databaseReference) -> {

            if(databaseError != null) {
                Log.d(TAG, "writeUserToStorage: " + databaseError.getMessage());
                Log.d(TAG, "writeUserToStorage: " + databaseError.getCode());
                Log.d(TAG, "writeUserToStorage: " + databaseError.getDetails());

            }else {
                Log.d(TAG, "writeUserToStorage: SUCCESS");

                openNewsActivity();
            }

        };

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("ПОЛЬЗОВАТЕЛИ");
        DatabaseReference userUID = usersReference.child(uid);
        userUID.child("email").setValue(email, completionListener);
        userUID.child("имя_пользователя").setValue(userName, completionListener);
        userUID.push();
    }

    private void openNewsActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
