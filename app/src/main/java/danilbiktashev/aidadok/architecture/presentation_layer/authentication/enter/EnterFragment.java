package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import danilbiktashev.aidadok.R;
import danilbiktashev.aidadok.architecture.presentation_layer.authentication.LoginActivity;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
                    task.getException().printStackTrace();
                    EnterErrorDialog errorDialog = EnterErrorDialog.newInstance("Неверно введен e-mail или пароль");
                    errorDialog.show(getFragmentManager(), "EnterErrorDialog");
                }
            });
        }else {
            EnterErrorDialog errorDialog = EnterErrorDialog.newInstance("Необходимо заполнить все поля");
            errorDialog.show(getFragmentManager(), "EnterErrorDialog");
        }

    }

    private void openNewsActivity(){
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendResetPasswordEmail(){
        SendResetPasswordDialog dialog = new SendResetPasswordDialog();
        dialog.setListener(new SendResetPasswordDialog.ISendResetPasswordListener() {
            @Override
            public void onSendMessage(String email) {
                Log.d(TAG, "onSendMessage: " + email);
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });

        dialog.show(getFragmentManager(), "SendResetPasswordDialog");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
