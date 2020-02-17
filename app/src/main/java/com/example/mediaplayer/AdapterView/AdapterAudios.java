package com.example.mediaplayer.AdapterView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.mediaplayer.Interfaz.EnviarAudio;
import com.example.mediaplayer.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterAudios extends ArrayAdapter  implements EnviarAudio {
    private ArrayList<String>NotasDeAudio=new ArrayList<String>();
    private EnviarAudio enviarAudio;

    public AdapterAudios(Context context, ArrayList<String>categorias) {
        super(context, R.layout.adapter_audio,categorias);
        this.NotasDeAudio=categorias;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View item =convertView;
        Holder holder;
        if(item==null){
            LayoutInflater inflater= LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.adapter_audio,null);
            holder = new Holder();
            holder.txtNombre=(TextView)item.findViewById(R.id.txtNombre);
            item.setTag(holder);
        }
        else{
            holder=(Holder) item.getTag();
        }
        final String audio=NotasDeAudio.get(position);

        holder.txtNombre.setText(audio);
        return (item);
    }



    @Override
    public void OpenAudio(String audio) {

    }


    public static class Holder {
        TextView txtNombre;


    }
}
