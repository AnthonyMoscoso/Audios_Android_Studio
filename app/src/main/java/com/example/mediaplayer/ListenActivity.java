package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class ListenActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    int playPosicion=0;
    int n=0;
    protected SeekBar seekAudio;
    protected ImageButton btnClick;
    String Audio;
    private Handler handler;
    final static String POSICION="POSICION";
    protected TextView txtTitule, txtSize,txtTiempo;
    File mFolder;

     long LONGITUD_AUDIOS,tiempoRes,TIEMPO_RESTANTE;
    protected CountDownTimer reloj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
       // seekAudio=(SeekBar)findViewById(R.id.seekBar);
        mFolder = new File(getFilesDir() + "/Audios");
        btnClick=(ImageButton)findViewById(R.id.btnClick);
        txtSize=(TextView)findViewById(R.id.txtSize);
        txtTitule=(TextView)findViewById(R.id.txtNombre);
        txtTiempo=(TextView)findViewById(R.id.txtTiempo);
        GetBundle();

        try {
            if(savedInstanceState!=null){
                playPosicion=savedInstanceState.getInt(POSICION);
                TIEMPO_RESTANTE=savedInstanceState.getLong("RELOJ");
            }
            if(playPosicion>0){
                continuousPlayer(Audio,0);
            }
            else{
                prepareMediaPlayer(Audio);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        LONGITUD_AUDIOS =mediaPlayer.getDuration();
        txtSize.setText(LONGITUD_AUDIOS +"");
        txtTiempo.setText(LONGITUD_AUDIOS +"");
    }

    private void continuousPlayer(String Audio,int posicion) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(Audio));
        mediaPlayer.prepare();
        mediaPlayer.seekTo(posicion);
        mediaPlayer.start();
        reloj=crearTimer(TIEMPO_RESTANTE);
        reloj.start();

    }

    private void prepareMediaPlayer(String Audio) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(Audio));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public CountDownTimer crearTimer(long rest) {
        reloj = new CountDownTimer(rest, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRes = millisUntilFinished;
                TIEMPO_RESTANTE= LONGITUD_AUDIOS-tiempoRes;
                txtTiempo.setText(tiempoRes+"/");
            }

            @Override
            public void onFinish() {
                playPosicion=0;
                mediaPlayer.seekTo(0);
              mediaPlayer.pause();
              btnClick.setImageResource(R.drawable.stop);

            }
        };

        return reloj;
    }



    public void Listen(View v){
        playPosicion=mediaPlayer.getCurrentPosition();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnClick.setImageResource(R.drawable.play);
            reloj.cancel();


        }
        else if(!mediaPlayer.isPlaying() && playPosicion>0){
                mediaPlayer.start();
                mediaPlayer.seekTo(playPosicion);
                btnClick.setImageResource(R.drawable.pause);
                TIEMPO_RESTANTE=LONGITUD_AUDIOS-playPosicion;
                reloj=crearTimer(TIEMPO_RESTANTE);
                reloj.start();
        }
        else if(!mediaPlayer.isPlaying() && playPosicion==0) {
            try {
                reloj = crearTimer(LONGITUD_AUDIOS);
                reloj.start();
                mediaPlayer.start();
                btnClick.setImageResource(R.drawable.pause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void GetBundle(){
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String  mp3=bundle.getString("Audio");
                Audio=mFolder.getAbsolutePath()+"/"+mp3;
                txtTitule.setText(mp3);

            }
        } catch (Exception ex) {

        }
    }
    public void ResetAudio(View v){
            reloj.cancel();
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            btnClick.setImageResource(R.drawable.play);
            txtSize.setText(LONGITUD_AUDIOS +"");
            txtTiempo.setText(LONGITUD_AUDIOS +"");

    }



    protected void onRestart(){
        super.onRestart();
        GetBundle();
        prepareMediaPlayer(Audio);
        btnClick.setImageResource(R.drawable.play);
        mediaPlayer.seekTo(playPosicion);



    }

    public void Back2(View view){
        Intent intent=new Intent();
        setResult(Activity.RESULT_OK, intent);
        if(reloj!=null){

            reloj.cancel();

        }
        mediaPlayer.stop();
        mediaPlayer.release();

        finish();
    }

    protected void onResume(){
        super.onResume();


    }

    protected void onPause(){
        super.onPause();
        reloj.cancel();
        playPosicion = mediaPlayer.getCurrentPosition();

        mediaPlayer.pause();
    }

    protected void onStop(){
        super.onStop();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(reloj!=null){

            reloj.cancel();

        }
        mediaPlayer.stop();
        mediaPlayer.release();

    }
    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        if(mediaPlayer.isPlaying()){
            playPosicion=mediaPlayer.getCurrentPosition();
        }
        else if(!mediaPlayer.isPlaying() && playPosicion>0){
            playPosicion=mediaPlayer.getCurrentPosition();
        }
        else{
            playPosicion=0;
        }

        mediaPlayer.release();
        bundle.putLong("RELOJ",TIEMPO_RESTANTE);
        reloj.cancel();
        bundle.putInt(POSICION,playPosicion);
    }

}
