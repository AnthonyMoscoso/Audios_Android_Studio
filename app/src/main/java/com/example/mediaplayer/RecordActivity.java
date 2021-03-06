package com.example.mediaplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@RequiresApi(api = Build.VERSION_CODES.O)
public class RecordActivity extends AppCompatActivity {

    public static final int DURACION_MAXIMA=100000;

    protected MediaRecorder recorder;
    protected ImageButton btnRecord;
    protected TextView txtEstado,txtTime;
    protected ProgressBar progressBar;
    protected CountDownTimer reloj;
    private boolean IsRecording;
    private long tiempoRes;
    private File mFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        btnRecord=(ImageButton)findViewById(R.id.btnRecord);
        txtEstado=(TextView)findViewById(R.id.txtEstado);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        txtTime=(TextView)findViewById(R.id.txtTime);
        txtTime.setText("hola");
        progressBar.setMax(DURACION_MAXIMA);
        progressBar.setProgress(0);
        IsRecording=false;
         ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        mFolder = new File(getFilesDir() + "/Audios");
        if(!mFolder.exists()){
            mFolder.mkdir();
        }


    }






    private void LoadRecorder() throws IOException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd MM-mm-ss");
        String name=simpleDateFormat.format(date).trim();
        File audio = new File(mFolder.getAbsolutePath() + "/"+name+".3gp");
        audio.createNewFile();
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audio.getAbsolutePath());
        try {
            recorder.prepare();
            recorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public CountDownTimer crearTimer(long rest) {
        reloj = new CountDownTimer(rest, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRes = millisUntilFinished;
                progressBar.setProgress(progressBar.getProgress() + 1000);
                txtTime.setText(tiempoRes+"");
            }

            @Override
            public void onFinish() {
                if(IsRecording){
                    GuardarAudio();
                    ReiniciarBarra();
                }
            }
        };

        return reloj;
    }


    public void Record(View v) throws IOException {
        if(!IsRecording){
            LoadRecorder();
            reloj=crearTimer(DURACION_MAXIMA);
            reloj.start();
            btnRecord.setBackground(getDrawable(R.drawable.boton_red));
            txtEstado.setText("Recording...");
            btnRecord.setImageResource(R.drawable.stop);
            IsRecording=true;
        }
        else{
            reloj.cancel();
            btnRecord.setBackground(getDrawable(R.drawable.boton_circular));
            GuardarAudio();
            btnRecord.setImageResource(R.drawable.microphone);

        }

    }
    private void GuardarAudio(){
        txtEstado.setText("Pulsa para grabar");
        recorder.stop();
        recorder.release();
        recorder=null;
        btnRecord.setBackground(getDrawable(R.drawable.boton_circular));
        btnRecord.setImageResource(R.drawable.microphone);
        IsRecording=false;
        txtTime.setText("Pulsa para grabar");
        ReiniciarBarra();
    }
    private void ReiniciarBarra(){
        progressBar.setMax(DURACION_MAXIMA);
        progressBar.setProgress(0);
    }

    protected void onStart(){
        super.onStart();
    }
    protected void onRestart(){
        super.onRestart();
    }




    protected void onPause(){
        super.onPause();
        if(recorder!=null){
            GuardarAudio();
        }

    }

    protected void onStop(){
        super.onStop();

    }

    public void Back(View v){
        Intent intent=new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(IsRecording || recorder!=null){
            GuardarAudio();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        if(IsRecording){
            GuardarAudio();
        }

    }
}
