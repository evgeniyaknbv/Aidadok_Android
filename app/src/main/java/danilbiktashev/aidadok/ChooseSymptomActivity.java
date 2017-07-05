package danilbiktashev.aidadok;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseSymptomActivity extends AppCompatActivity {

    public String currentCharapterId = new String();
    String currentQuestionId;
    String currentShemId;

    String questionText = new String();
    ArrayList<symp> products = new ArrayList<symp>();
    ArrayList<String> allQuestions = new ArrayList<>();
    ArrayList<String> allCharapters = new ArrayList<>();
    ArrayList<String> allShems = new ArrayList<>();
    String maleOrFemaleTimeId;
    TextView question;
    Button backButton;
    Button yesButton;
    Button noButton;
    Boolean isGettingMale = false;

    symp currentCharapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_symptom);
        currentQuestionId = "1";
        currentShemId = "1";
        Bundle args = getIntent().getBundleExtra("question");
        products = (ArrayList<symp>) args.getSerializable("a");
        currentCharapterId = getIntent().getStringExtra("charapterID");
        currentCharapter = this.extractCharapterById();

        Question currentQuestion = currentCharapter.extractQuestionById("1");
        questionText = currentQuestion.getQuestionText();
        addNewCharapterQuestionAndShemToHistoryArrays();

        uiGetViews();

        backButton.setAlpha(0);
        question.setText(questionText);
    }

    void uiGetViews() {
        backButton = (Button)findViewById(R.id.back_button);
        question = (TextView) findViewById(R.id.question_text);
        yesButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);
    }

    public void yesButtonPressed(View view) {
        if (!isGettingMale) {
            nextStep("ДА");
        } else {
            isGettingMale = false;
            cutFemale();
            noButton.setText("НЕТ");
            yesButton.setText("ДА");
        }
    }

    public void noButtonPressed(View view) {
        if (!isGettingMale) {
            nextStep("НЕТ");
        } else {
            isGettingMale = false;
            cutMale();
            noButton.setText("НЕТ");
            yesButton.setText("ДА");
        }
    }

    symp extractCharapterById() {
        for(symp symptom : products) {
            Log.d("SYMPTOM", "symp:" + symptom.getIdCharapter() + "==" + currentCharapterId + (symptom.getIdCharapter() == currentCharapterId));
            if (symptom.getIdCharapter().equals(currentCharapterId)) {
                return symptom;
            }
        }
        return null;
    }

    Question extractQuestionById(String questionId) {
        for(Question question : currentCharapter.getQuestion()) {
            if (question.getIdQuestion().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

    Answer extractAnswerById(String answerId) {
        for(Answer answer : currentCharapter.getAnswer()) {
            if (answer.getIdAnswer().equals(answerId)) {
                return answer;
            }
        }
        return null;
    }

    void nextStep(String answer) {
        if (answer.equals("ДА")) {
            yesAnswer();
        } else {
            noAnswer();
        }
    }

    void yesAnswer() {
        Question currentQuestion = extractQuestionById(currentQuestionId);
        String[] nextAction = currentQuestion.getYesAnswer().split("");
        String[] nextActionWithoutEmptySlot = Arrays.copyOfRange(nextAction,1,nextAction.length);
        detectActionFromAnswerArray(nextActionWithoutEmptySlot);
    }

    void noAnswer() {
        Question currentQuestion = extractQuestionById(currentQuestionId);
        String[] nextAction = currentQuestion.getNoAnswer().split("");
        detectActionFromAnswerArray(Arrays.copyOfRange(nextAction,1,nextAction.length));
    }

    void detectActionFromAnswerArray(String[] answer) {
        String comand = answer[0];
        String Id = getIdFromAnswer(answer);
        if (comand.equals("Q")) {
            backButton.setAlpha(1);
            nextQuestion(Id);
        } else if (comand.equals("W")) {
            Intent myIntent = new Intent(ChooseSymptomActivity.this, ResultActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("a",(Serializable)extractAnswerById(Id));
            myIntent.putExtra("answer", args);
            ChooseSymptomActivity.this.startActivityForResult(myIntent,2);
        } else if (comand.equals("Z")) {
            backButton.setAlpha(1);
            nextCharapter(Id);
        } else if (comand.equals("G")){
            noMoreQuestions();
        } else if (comand.equals("M")) {
            maleOrFemale(Id);
        }
    }

    void noMoreQuestions() {
        Intent myIntent = new Intent(ChooseSymptomActivity.this, EndOfSymptomActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("a",(Serializable)currentCharapter.getEndText());
        myIntent.putExtra("endtext", args);
        ChooseSymptomActivity.this.startActivityForResult(myIntent,2);
    }

    void maleOrFemale(String timeId) {
        isGettingMale = true;
        yesButton.setText("М");
        noButton.setText("Ж");
        maleOrFemaleTimeId = timeId;
    }

    void cutMale() {
        String[] nextAction = maleOrFemaleTimeId.split("");
        List<String> list = new ArrayList<String>(Arrays.asList(nextAction));
        list.remove(0);
        List<String> newRequestForNewQuestion = new ArrayList<>();
        boolean fDetected = false;
        for (String s: list) {
            if (!s.equals("F")) {
                if (fDetected) {
                    newRequestForNewQuestion.add(s);
                }
            } else {
                fDetected = true;
            }
        }
        String[] newStringNextActionArray = new String[newRequestForNewQuestion.size()];
        newRequestForNewQuestion.toArray(newStringNextActionArray);
        detectActionFromAnswerArray(newStringNextActionArray);
    }

    void cutFemale() {
        String[] nextAction = maleOrFemaleTimeId.split("");
        List<String> list = new ArrayList<String>(Arrays.asList(nextAction));
        list.remove(0);
        List<String> newRequestForNewQuestion = new ArrayList<>();
        for (String s: list) {
            if (!s.equals("F")) {
                newRequestForNewQuestion.add(s);
            } else {
                break;
            }
        }
        String[] newStringNextActionArray = new String[newRequestForNewQuestion.size()];
        newRequestForNewQuestion.toArray(newStringNextActionArray);
        detectActionFromAnswerArray(newStringNextActionArray);
    }

    String getIdFromAnswer(String[] answer) {
        List<String> list = new ArrayList<String>(Arrays.asList(answer));
        list.remove(0);
        return listToString(list);
    }

    public static String listToString (List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            sb.append(s);
        }
        return sb.toString();
    }

    void nextCharapter(String charapterId) {
        currentCharapterId = charapterId;
        currentCharapter = extractCharapterById();
        nextQuestion("1");
    }

    void nextQuestion(String questioId) {
        currentQuestionId = questioId;
        Question nextQuestion = extractQuestionById(currentQuestionId);
        questionText = nextQuestion.getQuestionText();
        question.setText(questionText);
        addNewCharapterQuestionAndShemToHistoryArrays();
    }

    void addNewCharapterQuestionAndShemToHistoryArrays() {
        allQuestions.add(currentQuestionId);
        allCharapters.add(currentCharapterId);
        allShems.add(currentShemId);
    }

    ArrayList<String> removeCharapterQuestionAndShemFromHistoryArrays() {
        allQuestions.remove(allQuestions.size()-1);
        allCharapters.remove(allCharapters.size()-1);
        allShems.remove(allShems.size()-1);
        ArrayList<String> questionCharapterShem = new ArrayList<>();
        questionCharapterShem.add(allQuestions.get(allQuestions.size()-1));
        questionCharapterShem.add(allCharapters.get(allQuestions.size()-1));
        questionCharapterShem.add(allShems.get(allShems.size()-1));
        return questionCharapterShem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getExtras().containsKey("menu")) {
                finish();
            }
        }
    }

    public void backButtonAction(View view) {
        if (allQuestions.size() != 1) {
            ArrayList<String> questionCharapterShem = removeCharapterQuestionAndShemFromHistoryArrays();
            currentQuestionId = questionCharapterShem.get(0);
            currentCharapterId = questionCharapterShem.get(1);
            currentCharapter = extractCharapterById();
            currentShemId = questionCharapterShem.get(2);
            Question newCurrentQuestion = extractQuestionById(currentQuestionId);
            question.setText(newCurrentQuestion.getQuestionText());
            currentQuestionId = newCurrentQuestion.getIdQuestion();
            if (allQuestions.size() == 1) {
                backButton.setAlpha(0);
            }
        }
    }

    public void menuButtonAction(View view) {
        finish();
    }
}
