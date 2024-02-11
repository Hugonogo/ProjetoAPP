package com.example.appform;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appform.adapter.AdapterAtividades;
import com.example.appform.databinding.ActivityStepCountBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModeloRespostaAtividadeFisica;
import com.example.appform.model.ModeloRespsotaListaAtividades;
import com.example.appform.retrofitUtils.RetrofitUtil;
import com.example.appform.retrofitUtils.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContadorPassosActivity extends AppCompatActivity{


    private ActivityStepCountBinding vb;

    private AlertDialog dialog_carregando;

    private RecyclerView recyclerAtividades;
    private AdapterAtividades adapterAtividades;

    private List<ModeloRespostaAtividadeFisica.Data> listaAtividades = new ArrayList<>();

    private Retrofit retrofit;
    private ServiceApi service;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vb = ActivityStepCountBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(vb.getRoot());

        retrofit = RetrofitUtil.createRetrofit();
        service = RetrofitUtil.createService(retrofit);

        preferences  = getSharedPreferences("user_prefs", MODE_PRIVATE);

        configurarAlert();

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configurarRecyclerAtividades();
        vb.btnCadastrarAtitivade.setOnClickListener(view ->  startActivity(new Intent(this, BorgActivity.class)));

    }


    @Override
    protected void onStart() {
        super.onStart();

        dialog_carregando.show();

        service.getAtividades(preferences.getInt("id", 0)).enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ModeloRespsotaListaAtividades> call, Response<ModeloRespsotaListaAtividades> response) {
                dialog_carregando.dismiss();

                if ( response.isSuccessful() ){
                    listaAtividades.clear();

                    listaAtividades.addAll(response.body().getData());

                    adapterAtividades.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ModeloRespsotaListaAtividades> call, Throwable t) {
                dialog_carregando.dismiss();
                Toast.makeText(ContadorPassosActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void configurarRecyclerAtividades() {
        recyclerAtividades = vb.recyclerAtividades;
        recyclerAtividades.setLayoutManager(new LinearLayoutManager(this));
        recyclerAtividades.setHasFixedSize(true);
        recyclerAtividades.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapterAtividades = new AdapterAtividades(listaAtividades, this);
        recyclerAtividades.setAdapter(adapterAtividades);
    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }
}
