package fr.doranco.myquizz.modele;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class MyDatabaseSQLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "JoueurDB";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME="Joueurs";
    private static final String TABLE_NAME_QR ="QuestionReponse";
    private static final String CREATE_DB_TABLE_JOUEUR = "CREATE TABLE " + TABLE_NAME
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, joueur TEXT NOT NULL, score INTEGER NOT NULL)";
    private static final String CREATE_DB_TABLE_QR = "CREATE TABLE "
                    + TABLE_NAME_QR + "(id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT NOT NULL, reponse TEXT NOT NULL)";
    public MyDatabaseSQLite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE_JOUEUR);
        db.execSQL(CREATE_DB_TABLE_QR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_QR);
        onCreate(db);
           // db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME + TABLE_NAME_QR);
           // db.execSQL(CREATE_DB_TABLE);
//       db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN score");
//       // db.execSQL(CREATE_DB_TABLE);
//        db.execSQL("ALTER TABLE " + TABLE_NAME_QR + " ADD COLUMN question");
//        db.execSQL("ALTER TABLE " + TABLE_NAME_QR + " ADD COLUMN reponse");
    }
}
