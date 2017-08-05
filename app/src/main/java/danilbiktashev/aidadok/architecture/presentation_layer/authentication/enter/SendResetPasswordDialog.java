package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import danilbiktashev.aidadok.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static danilbiktashev.aidadok.architecture.presentation_layer.authentication.AuthenticationUtils.EMAIL_VALIDATION_REGEX;

/**
 * Created by Evgeniya on 27.07.2017.
 *
 */

public    class SendResetPasswordDialog extends DialogFragment  implements View.OnClickListener {

    private static final String TAG = "SendResetPasswordDialog";
    private EditText email;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    interface ISendResetPasswordListener{
        void onSendMessage(String email);
    }

    private ISendResetPasswordListener listener;


    private void initViews(View root){
        TextView okButton = (TextView) root.findViewById(R.id.ok);
        okButton.setOnClickListener(this);

        root.findViewById(R.id.cancel).setOnClickListener(this);


        email = (EditText) root.findViewById(R.id.email);

        TextInputLayout tilEmail = (TextInputLayout) root.findViewById(R.id.til_email);
        compositeDisposable.add(RxTextView.textChanges(email).skipInitialValue().debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(charSequence -> {
                    if(isMailCorrect(charSequence)){

                        tilEmail.setError(null);
                        tilEmail.setErrorEnabled(false);

                        if(!okButton.isEnabled()){
                            okButton.setEnabled(true);
                        }

                    }else {
                        okButton.setEnabled(false);
                        tilEmail.setError("Неверный e-mail");
                    }
                }).subscribe());


    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_send_reset_password, null);
        initViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(email.getText().toString().length() > 0) {
                    listener.onSendMessage(email.getText().toString());
                    this.dismiss();
                }
                break;
            case R.id.cancel:
                this.dismiss();
                break;

        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public boolean isMailCorrect(CharSequence email) {
        String emailS = email.toString();
        return emailS.matches(EMAIL_VALIDATION_REGEX);
    }

    public void setListener(ISendResetPasswordListener listener) {
        this.listener = listener;
    }

    public static void showSendEmailDialog(FragmentManager fragmentManager, ISendResetPasswordListener listener){
        SendResetPasswordDialog dialog = new SendResetPasswordDialog();
        dialog.setCancelable(false);
        dialog.setListener(listener);
        dialog.show(fragmentManager, "SendResetPasswordDialog");
    }

}
