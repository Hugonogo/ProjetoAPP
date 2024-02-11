package com.example.appform.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appform.R;
import com.example.appform.model.ModeloRespostaAtividadeFisica;

import java.util.ArrayList;
import java.util.List;

public class AdapterAtividades extends RecyclerView.Adapter<AdapterAtividades.MyViewHolder>{

    List<ModeloRespostaAtividadeFisica.Data> listaAtividades;
    Activity c;

    public AdapterAtividades(List<ModeloRespostaAtividadeFisica.Data> listaAtividades, Activity c) {
        this.listaAtividades = listaAtividades;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exibir_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModeloRespostaAtividadeFisica.Data data = listaAtividades.get(position);

        holder.textInfo.setText(data.toString());
    }

    @Override
    public int getItemCount() {
        return listaAtividades.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textInfo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textInfo = itemView.findViewById(R.id.text_infos_atividade);
        }
    }
}
