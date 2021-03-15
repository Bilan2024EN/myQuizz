package fr.doranco.myquizz.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Arrays;

import fr.doranco.myquizz.MusiqueService;
import fr.doranco.myquizz.R;
import fr.doranco.myquizz.entity.Question;
import fr.doranco.myquizz.controller.QuestionBank;

public class QuizzActivity extends AppCompatActivity implements IQuizzConstants, View.OnClickListener {

    private static final String TAG = QuizzActivity.class.getSimpleName();
    public static final String EXTRA_SCORE_PARAM = "EXTRA_SCORE_PARAM";

    private TextView mQuestion;
    private Button mButtonAnswer1, mButtonAnswer2, mButtonAnswer3, mButtonAnswer4;
    private Question mCurrentQuestion;
    private QuestionBank mQuestionBank;
    private int mScore;
    private int mMaxNumberQuestion;
    private int mNumberOfQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        if(savedInstanceState != null){
            mScore = savedInstanceState.getInt(STATE_SCORE_BEFORE_ROTATION);
            mNumberOfQuestion = savedInstanceState.getInt(STATE_QUESTION_BEFORE_ROTATION);
        } else{
            mScore = 0;
            mNumberOfQuestion = 4;
        }

        mQuestion = findViewById(R.id.textViewQuestion);
        mButtonAnswer1 = findViewById(R.id.buttonReponse1);
        mButtonAnswer2 = findViewById(R.id.buttonReponse2);
        mButtonAnswer3 = findViewById(R.id.buttonReponse3);
        mButtonAnswer4 = findViewById(R.id.buttonReponse4);

        mQuestionBank = generateQuestions();
        mScore = 0;
        mNumberOfQuestion = mMaxNumberQuestion = 4;
        mButtonAnswer1.setOnClickListener(this);

        //Utiliser TAG pour nommer les boutons et surtout pour les distinguer lorsque la méthode onClick est déclenchée
        mButtonAnswer1.setTag(0);
        mButtonAnswer2.setTag(1);
        mButtonAnswer3.setTag(2);
        mButtonAnswer4.setTag(3);

        mButtonAnswer1.setOnClickListener(this);
        mButtonAnswer2.setOnClickListener(this);
        mButtonAnswer3.setOnClickListener(this);
        mButtonAnswer4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayCurrentQuestion(mCurrentQuestion);

    }

    private void displayCurrentQuestion(Question question) {
        mQuestion.setText(question.getNextQuestion());
        mButtonAnswer1.setText(question.getChoiceList().get(0));
        mButtonAnswer2.setText(question.getChoiceList().get(1));
        mButtonAnswer3.setText(question.getChoiceList().get(2));
        mButtonAnswer4.setText(question.getChoiceList().get(3));
    }

    // Ceci équivaut à un seul listener pour tous les boutons
    // Pour que ça marche il faut juste implémenter l'interface View.onlistener
    // view = page
    @Override
    public void onClick(View view) {
        int responseIndex = (int) view.getTag();
        if (responseIndex == mCurrentQuestion.getAnswerIndex()) {
            mScore++;
            Toast.makeText(this, "Bonne réponse :-)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mauvaise réponse :-(", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//si c'est la dernière question, arrêtez le quizz sinon affichez la prochaine question
                if (--mNumberOfQuestion == 0) {
                     endQuizz();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayCurrentQuestion(mCurrentQuestion);
                }
            }
        }, 2000);
    }

        private void endQuizz () {
            //On affiche une boite de dialogue pour indiquer la fin du quizz et le score
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quizz terminé !").setMessage("Votre score est de " + mScore + "/" + mMaxNumberQuestion)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_SCORE_PARAM, mScore);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
            Intent intent = new Intent(QuizzActivity.this, MusiqueService.class);
            stopService(intent);

        }

        //Pour régler le problème technique de la destruction de l'activité courante lorsqu on effectue vertical vers horizontal
        // et vice versa et donc la perte de la position
       // de la question et du score on doit implémenter cette méthode afin de préserver les infos utiles
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_SCORE_BEFORE_ROTATION, mScore);
        outState.putInt(STATE_SCORE_BEFORE_ROTATION, mNumberOfQuestion);
        super.onSaveInstanceState(outState);
    }

    private QuestionBank generateQuestions () {
            Question question1 = new Question("Quel est le pays le plus peuplé du monde ?", Arrays.asList("USA", "Chine", "Inde", "Indonésie"), 1);
            Question question2 = new Question("Combien existe-t-il de pays dans l'Union Européenne?", Arrays.asList("15", "24", "27", "32"), 2);
            Question question3 = new Question("Quel est le créateur du système d'exploitation d'Android?", Arrays.asList("Jake Warton", "Steve Wozniak", "Paul Smith", "Andu Rubin"), 3);
            Question question4 = new Question("Quel est le premier président de la IVe république?", Arrays.asList("Vincent Auriol", "René Coty", "Albert Lebrun", "Paul Doumer"), 0);
            Question question5 = new Question("Quelle est la plus petite république du monde en nombre d'habitants?", Arrays.asList("Monaco", "Nauru", "Les Tuvalu", "Les Palaos"), 1);
            Question question6 = new Question("Quelle est lla langue la mois parlée au monde?", Arrays.asList("L'artchi", "Le silbo", "Le Rotokas", "Le Piraha"), 0);

            return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6));
        }


    }
