package fr.doranco.myquizz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import fr.doranco.myquizz.controller.QuestionBank;

public class MusiqueService extends Service {
    private static final String TAG = MusiqueService.class.getSimpleName();
    private static final String RAW_SONG = "help_yourself";

    private MediaPlayer mediaPlayer;
    private final Handler threadHandler = new Handler();

    QuestionBank qb = new QuestionBank();
    public MusiqueService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(MusiqueService.this, R.raw.help_yourself);
        mediaPlayer.setVolume(50, 50);
        mediaPlayer.start();
        mediaPlayer.isPlaying();
// On retourne au début du média, 0 est la première milliseconde
        mediaPlayer.seekTo(0);

        mediaPlayer.setLooping(true);
        Toast.makeText(this, "Musique de fond", Toast.LENGTH_LONG).show();

   /*     if (qb.getQuestion().getNextQuestion().equals("0")) {
            mediaPlayer.stop();
        }*/
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"methode onBind() invoquée");
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        Toast.makeText(this, "Musique terminée", Toast.LENGTH_LONG).show();
    }
}
