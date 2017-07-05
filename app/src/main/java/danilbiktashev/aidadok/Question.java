package danilbiktashev.aidadok;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mikhailkoroteev on 14.06.17.
 */

public class Question implements Serializable {

    String yesAnswer;
    String noAnswer;
    String idQuestion;
    String questionText;

    Question(String _yesAnswer, String _noAnswer, String _idQuestion, String _questionText) {
        yesAnswer = _yesAnswer;
        noAnswer = _noAnswer;
        idQuestion = _idQuestion;
        questionText = _questionText;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setNoAnswer(String noAnswer) {
        this.noAnswer = noAnswer;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setYesAnswer(String yesAnswer) {
        this.yesAnswer = yesAnswer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public String getNoAnswer() {
        return noAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getYesAnswer() {
        return yesAnswer;
    }
}
