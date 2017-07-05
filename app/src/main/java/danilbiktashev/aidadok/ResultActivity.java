package danilbiktashev.aidadok;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    Context ctx;
    String status;
    TextView titleTextView;
    TextView suggestionTextView;
    ImageView resultImage;
    Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        defaultUiSettings();
        getAnswerFromPreviousActivity();
        setSettingsByStatus();
    }

    void getAnswerFromPreviousActivity() {
        Bundle args = getIntent().getBundleExtra("answer");
        Answer currentAnswer = (Answer) args.getSerializable("a");
        status = currentAnswer.getStatus();
        Log.d("SYMPTOM",status);
        suggestionTextView.setText(currentAnswer.getAnswerText());
    }

    void defaultUiSettings() {
        titleTextView = (TextView) findViewById(R.id.resultTitle);
        suggestionTextView = (TextView) findViewById(R.id.resultSuggestion);
        resultImage = (ImageView) findViewById(R.id.resultImage);
        actionButton = (Button) findViewById(R.id.actionButton);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(),"systopie_regular.otf"));
        suggestionTextView.setTypeface(Typeface.createFromAsset(getAssets(),"ProximaNovaRegular.otf"));
        actionButton.setTypeface(Typeface.createFromAsset(getAssets(),"systopie_regular.otf"));
    }

    void setSettingsByStatus() {
        if (status.equals("easy")) {
            easyStatus();
        } else if (status.equals("normal")) {
            normalStatus();
        } else if (status.equals("hard")) {
            hardStatus();
        } else {
            insaneStatus();
        }
    }

    void easyStatus() {
        this.getWindow().getDecorView().setBackgroundColor(Color.rgb(57,181,74));
        titleTextView.setText("Помощь в домашних условиях");
        actionButton.setAlpha(0);
        resultImage.setBackgroundResource(R.drawable.first);
    }

    void normalStatus() {
        this.getWindow().getDecorView().setBackgroundColor(Color.rgb(211,186,50));
        titleTextView.setText("Запись на прием к врачу");
        actionButton.setText("ЗАПИСАТЬСЯ К ВРАЧУ");
        resultImage.setBackgroundResource(R.drawable.norm);
    }

    void hardStatus() {
        this.getWindow().getDecorView().setBackgroundColor(Color.rgb(235,138,47));
        titleTextView.setText("Срочный вызов врача");
        actionButton.setText("ВЫЗОВ ТЕРАПЕВТА");
        resultImage.setBackgroundResource(R.drawable.hard);
    }

    void insaneStatus() {
        this.getWindow().getDecorView().setBackgroundColor(Color.rgb(239,71,35));
        titleTextView.setText("Немедленный вызов «Скорой помощи»");
        actionButton.setText("ПОЗВОНИТЬ 112");
        resultImage.setBackgroundResource(R.drawable.ins);
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


    public void actionButtonPressed(View view) {
        switch (status) {
            case "normal":
                appointmentWithADoctor(this);
                break;
            case "hard":
                callDoctor();
                break;
            case "insane":
                callAmbulance();
                break;
            default:
                break;
        }
    }

    public static void appointmentWithADoctor(Activity activity) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://emias.info/appointment/"));
        activity.startActivity(browserIntent);
    }

    void callDoctor() {
        Intent myIntent = new Intent(ResultActivity.this, CallDoctorActivity.class);
        ResultActivity.this.startActivity(myIntent);
    }

    void callAmbulance() {
        CallDoctorActivity.makeCall("112",ResultActivity.this);
    }

}
