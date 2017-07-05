package danilbiktashev.aidadok;

import java.io.Serializable;

/**
 * Created by mikhailkoroteev on 15.06.17.
 */

public class Answer implements Serializable {
    String answerText;
    String status;
    String idAnswer;

    Answer(String _answerText, String _status, String _idAnswer) {
        status = _status;
        answerText = _answerText;
        idAnswer = _idAnswer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getStatus() {
        return status;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIdAnswer(String idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getIdAnswer() {
        return idAnswer;
    }
}
