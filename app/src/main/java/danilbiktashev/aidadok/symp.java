package danilbiktashev.aidadok;

/**
 * Created by mikhailkoroteev on 23.05.17.
 */

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;


public class symp implements Serializable {

    String name;
    String descr;
    String endText;
    String idCharapter;
    ArrayList<Question> question = new ArrayList<Question>();
    ArrayList<Answer> answer = new ArrayList<Answer>();

    symp(String _descr, String _name, String _idCharapter, ArrayList<Question> _question, ArrayList<Answer> _answer, String _endText) {
        descr = _descr;
        name = _name;
        idCharapter = _idCharapter;
        question = _question;
        answer = _answer;
        endText = _endText;
    }

    symp(DataSnapshot symptom) {
        descr = symptom.child("descr").getValue(String.class);
        idCharapter = symptom.child("idCharapter").getValue(String.class);
        name = idCharapter + ". " + symptom.child("name").getValue(String.class);
        endText = symptom.child("end").getValue(String.class);
        question = parseQuestions(symptom.child("ВОПРОСЫ"));
        answer = parseAnswers(symptom.child("ОТВЕТЫ"));
    }

    public String getDescr() {
        return descr;
    }

    public String getIdCharapter() {
        return idCharapter;
    }

    public String getName() {
        return name;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setIdCharapter(String idCharapter) {
        this.idCharapter = idCharapter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestion(ArrayList<Question> question) {
        this.question = question;
    }

    public ArrayList<Question> getQuestion() {
        return question;
    }

    public ArrayList<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Answer> answer) {
        this.answer = answer;
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    ArrayList<Question> parseQuestions(DataSnapshot questionsShot) {
        ArrayList<Question> quetionsArray = new ArrayList<Question>();
        for (DataSnapshot question: questionsShot.getChildren()) {
            Question currentQuestion = new Question(question.child("yes").getValue(String.class),
                    question.child("no").getValue(String.class),
                    question.getKey(),
                    question.child("question").getValue(String.class));
            quetionsArray.add(currentQuestion);
        }
        return quetionsArray;
    }

    ArrayList<Answer> parseAnswers(DataSnapshot answerShot) {
        ArrayList<Answer> answersArray = new ArrayList<Answer>();
        for (DataSnapshot answer: answerShot.getChildren()) {
            Answer currentAnswer = new Answer(answer.child("answer").getValue(String.class),
                    answer.child("status").getValue(String.class),
                    answer.getKey());
            answersArray.add(currentAnswer);
        }
        return answersArray;
    }

    public Question extractQuestionById(String questionId) {
        for(Question question : this.getQuestion()) {
            if (question.getIdQuestion().equals(questionId)) {
                return question;
            }
        }
        return null;
    }
}