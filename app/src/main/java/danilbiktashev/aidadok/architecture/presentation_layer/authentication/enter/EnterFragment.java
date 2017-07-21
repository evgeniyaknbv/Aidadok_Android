package danilbiktashev.aidadok.architecture.presentation_layer.authentication.enter;
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

import danilbiktashev.aidadok.R;

/**
 * Created by User on 21.07.2017.
 */

public class EnterFragment extends Fragment   implements View.OnClickListener{
    private static final String TAG = "EnterFragment";

    private EditText password;
    private EditText email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter, container, false);
        setHasOptionsMenu(true);

        root.findViewById(R.id.enter_button).setOnClickListener(this);
        root.findViewById(R.id.forget_password).setOnClickListener(this);
        root.findViewById(R.id.facebook).setOnClickListener(this);
        root.findViewById(R.id.vk).setOnClickListener(this);

        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.enter1);

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
        switch (v.getId()){
            case R.id.enter_button:
//                переход в активити с новостями
                break;
            case R.id.forget_password:

                break;
            case R.id.facebook:

                break;
            case R.id.vk:

                break;
        }
    }
}
