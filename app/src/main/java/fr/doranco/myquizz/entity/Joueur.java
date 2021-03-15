package fr.doranco.myquizz.entity;

public class Joueur {
    private String mFirstName;

    public int getmScoreQuizz() {
        return mScoreQuizz;
    }

    public void setmScoreQuizz(int mScoreQuizz) {
        this.mScoreQuizz = mScoreQuizz;
    }

    private int mScoreQuizz;

    public Joueur() {

    }
    public Joueur(String mFirstName, int mScoreQuizz) {
        this.mFirstName = mFirstName;
        this.mScoreQuizz = mScoreQuizz;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;


    }

    @Override
    public String toString() {
        return "Joueur{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mScoreQuizz=" + mScoreQuizz +
                '}';
    }
}
