package fr.doranco.myquizz.controller;

import java.util.Collections;
import java.util.List;

import fr.doranco.myquizz.entity.Question;

public class QuestionBank {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(){

    }
    public QuestionBank(List<Question>mQuestionList){
        this.mQuestionList = mQuestionList;

        Collections.shuffle(mQuestionList);
        mNextQuestionIndex = 0;
    }

    public Question getQuestion(){
        //  on s'assure d'abord que l'on boucle sur la liste des questions au cas où on atteindrait la fin de la liste
        if(mNextQuestionIndex == mQuestionList.size()){
            mNextQuestionIndex = 0;
        }
        return mQuestionList.get(mNextQuestionIndex++);
    }
}
