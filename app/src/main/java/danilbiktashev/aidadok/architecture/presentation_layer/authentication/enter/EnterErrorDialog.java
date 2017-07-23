package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by User on 22.07.2017.
 */

public class EnterErrorDialog extends DialogFragment   {
    public static EnterErrorDialog newInstance(String message) {

        Bundle args = new Bundle();
        args.putString("message", message);
        EnterErrorDialog fragment = new EnterErrorDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Ошибка");
        dialogBuilder.setMessage(getMessage());
        dialogBuilder.setPositiveButton("OK", (dialog, which) -> dismiss());
        return dialogBuilder.show();
    }

    private String getMessage(){
        if(getArguments()!= null){
            return getArguments().getString("message");
        }else return "ошибка";
    }
}
