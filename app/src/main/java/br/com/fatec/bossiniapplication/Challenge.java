package br.com.fatec.bossiniapplication;

/**
 * Created by arthurlps on 4/5/17.
 */

public class Challenge {

    private String question;
    private String answer;

    public Challenge(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRight(String answer){
        if(answer.isEmpty())
            return false;

        return answer.equalsIgnoreCase(this.answer);
    }


}
