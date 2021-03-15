package fr.doranco.myquizz.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.doranco.myquizz.MusiqueService;
import fr.doranco.myquizz.R;
import fr.doranco.myquizz.entity.Joueur;
import fr.doranco.myquizz.controller.JoueurProvider;

public class MainActivity extends AppCompatActivity implements IQuizzConstants {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 10;

    private TextView mtextViewTitre;
    private EditText mEditTextName;
    private Button mbuttonCommencer;
    private Joueur mJoueur;
    private int score;

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextViewTitre = findViewById(R.id.textViewtitre);
        mEditTextName = findViewById(R.id.editTextName);
        mbuttonCommencer = findViewById(R.id.buttonCommencer);

        mPreferences = getPreferences(MODE_PRIVATE);

        mbuttonCommencer.setEnabled(false);
        mJoueur = new Joueur();

        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mbuttonCommencer.setEnabled(!charSequence.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String firstName = mPreferences.getString(PREF_KEY_USER_FIRSTNAME, "");
        int score = mPreferences.getInt(PREF_KEY_USER_SCORE, 0);
        if (firstName.isEmpty()) {
            mtextViewTitre.setText("Bonjour ! Veuillez saisir votre nom. ");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Bonjour ");
            sb.append(firstName);
            sb.append("! Bon retour !\n");
            sb.append("Votre dernier score était de : ");
            sb.append(score);
            sb.append(" points.");
            mtextViewTitre.setText(sb.toString());
        }

    }

    public void commencerQuizz(View view) {
        try {
            ContentValues values = new ContentValues();
            String userName = mEditTextName.getText().toString();
            int score = mPreferences.getInt(PREF_KEY_USER_SCORE, 0);
            values.put("joueur", userName);
            values.put("score", score);
            //Uri uri = Uri.parse("content://fr.doranco.myquizz.modele.JoueurProvider/joueurs/insert");
            Uri uriAjout = getContentResolver().insert(JoueurProvider.CONTENT_USERS_URI, values);
            Log.i(TAG, "Joueur ajouté avec succès  avec son score !");
           // Log.i(TAG, "URI Utilisateur ajouté : " + uriAjout);
             //mUser.setmFirstName(userName);
             mPreferences.edit().putString(PREF_KEY_USER_FIRSTNAME, userName).apply();
             Intent quizzActivityIntent = new Intent(MainActivity.this, QuizzActivity.class);
             startActivityForResult(quizzActivityIntent, QUIZZ_ACTIVITY_REQUEST_CODE);
            Toast.makeText(getBaseContext(), "Nouveau joueur ajouté", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(MainActivity.this, MusiqueService.class);
            startService(intent);
        } catch (SQLiteException e) {
            Toast.makeText(getBaseContext(), "Erreur lors de l'ajout du nouveau joueur", Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
    }


    public void onClickAfficher(View view) {
        TextView textViewListUsers = findViewById(R.id.textViewListUsers);
        Uri uri = Uri.parse("content://fr.doranco.myquizz.controller.JoueurProvider/joueurs");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            StringBuilder str = new StringBuilder();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String userName = cursor.getString(cursor.getColumnIndex("joueur"));
                score = mPreferences.getInt(PREF_KEY_USER_SCORE, 0);
                Log.i(TAG, "L'id est : " + id);
                Log.i(TAG, "Le nom du joueur est : " + userName + score);
                str.append(id);
                str.append(" - ");
                str.append(userName);
                str.append("  ");
                str.append(score);
                str.append(System.lineSeparator());
                cursor.moveToNext();
            }
            textViewListUsers.setText(str);
        } else {
            textViewListUsers.setText("Pas de joueurs");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUIZZ_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int score = data.getIntExtra(QuizzActivity.EXTRA_SCORE_PARAM, 0);
            mPreferences.edit().putInt(PREF_KEY_USER_SCORE, score).apply();
        }
    }
}