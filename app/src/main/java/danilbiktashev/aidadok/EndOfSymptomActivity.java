package danilbiktashev.aidadok;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndOfSymptomActivity extends AppCompatActivity {

    TextView endTextView;
    Button emiacButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_symptom);
        defaulUiSettings();
        getFromIntent();
    }

    void defaulUiSettings() {
        emiacButton = (Button) findViewById(R.id.emiacButton);
        endTextView = (TextView) findViewById(R.id.endText);
        ((TextView) findViewById(R.id.title)).setTypeface(Typeface.createFromAsset(getAssets(),"Proxima Nova Bold.otf"));
        endTextView.setTypeface(Typeface.createFromAsset(getAssets(),"ProximaNovaRegular.otf"));
        emiacButton.setTypeface(Typeface.createFromAsset(getAssets(),"systopie_regular.otf"));
    }

    void getFromIntent() {
        Bundle args = getIntent().getBundleExtra("endtext");
        String endTextString = (String) args.getSerializable("a");
        endTextView.setText(endTextString);
    }

    public void emiacButtonPressed(View view) {
        ResultActivity.appointmentWithADoctor(this);
    }

    public void backButtonAction(View view) {
        Intent i = getIntent();
        i.putExtra("back",true);
        setResult(RESULT_OK,i);
        finish();
    }

    public void menuButtonAction(View view) {
        Intent i = getIntent();
        i.putExtra("menu",true);
        setResult(RESULT_OK,i);
        finish();
    }
}
