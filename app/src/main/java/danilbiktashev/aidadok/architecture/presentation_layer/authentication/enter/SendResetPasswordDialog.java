package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import danilbiktashev.aidadok.R;
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

    public interface ISendResetPasswordListener{
        void onSendMessage(String email);
        void onCancel();
    }

    private ISendResetPasswordListener listener;


    private void initViews(View root){
        TextView okButton = (TextView) root.findViewById(R.id.ok);
        TextView cancelButton = (TextView) root.findViewById(R.id.cancel);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);

        TextInputLayout tilEmail = (TextInputLayout) root.findViewById(R.id.til_email);
        compositeDisposable.add(RxTextView.textChanges(email).skipInitialValue()
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(view);

        initViews(view);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                listener.onSendMessage(email.getText().toString());
                break;
            case R.id.cancel:
                listener.onCancel();
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

    public ISendResetPasswordListener getListener() {
        return listener;
    }

    public void setListener(ISendResetPasswordListener listener) {
        this.listener = listener;
    }

}
