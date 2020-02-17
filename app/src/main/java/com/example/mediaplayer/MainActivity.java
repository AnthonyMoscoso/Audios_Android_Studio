package com.example.mediaplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.mediaplayer.AdapterView.AdapterAudios;

import java.io.File;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ListView listView;
    protected ArrayList<String>Audios;
    protected Context context;
    File File_audio;
    String DIRECT_AUDIOS= Environment.getExternalStorageDirectory().getAbsolutePath()+"/audios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        listView=(ListView)findViewById(R.id.ListAudios);
        LoadAudios();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String audio=Audios.get(position);
                Intent intent = new Intent(context,ListenActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Audio",audio);
                intent.putExtras(bundle);
                startActivityForResult(intent,500);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String audio=Audios.get(position);
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle("Desea Eliminar la nota");
                dialogo1.setMessage("¿Elimina esta nota?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        File f = new File(getFilesDir() + "/Audios/"+audio);
                        f.delete();
                        LoadAudios();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(getApplicationContext(),"cancelado",Toast.LENGTH_SHORT).show();
                    }
                });
                dialogo1.show();
                return false;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void LoadAudios(){
        Audios= new ArrayList<>();
        File f = new File(getFilesDir() + "/Audios");
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            file[i].getName();
            Audios .add(  file[i].getName());
        }
        AdapterAudios adapterAudios = new AdapterAudios(getApplicationContext(),Audios);
        listView.setAdapter(adapterAudios);

    }
    public void GoToRecordActivy(View v){
        Intent intent=new Intent(this,RecordActivity.class);
        startActivityForResult(intent,2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void Reload(MenuItem menuItem){
        LoadAudios();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            }
        }

}
