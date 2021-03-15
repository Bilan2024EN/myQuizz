package fr.doranco.myquizz.entity;

import java.util.List;

public class Question {

    private String mquestion;
    private List<String> mcChoiceList;
    private int mAnswerIndex;

    public Question(String mquestion, List<String> mcChoiceList, int mAnswerIndex) {
        this.mquestion = mquestion;
        this.mcChoiceList = mcChoiceList;
        this.mAnswerIndex = mAnswerIndex;
    }


    public String getNextQuestion() {
        return mquestion;
    }

    public List<String> getChoiceList() {
        return mcChoiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }
}
