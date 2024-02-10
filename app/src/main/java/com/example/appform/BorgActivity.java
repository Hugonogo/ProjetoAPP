package com.example.appform;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appform.databinding.ActivityFormularioBorgBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModelResponse;
import com.example.appform.model.ModeloAtividadeFisica;
import com.example.appform.model.ModeloBodyPostAtividade;
import com.example.appform.model.ModeloRespostaAtividadeFisica;
import com.example.appform.retrofitUtils.RetrofitUtil;
import com.example.appform.retrofitUtils.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BorgActivity extends Activity {

    private AlertDialog dialog_carregando;
    private ActivityFormularioBorgBinding vb;
    private SharedPreferences preferences;
    String opcaoNomeAtividade;
    String opcaIntensidadeAtividade;
    private Retrofit retrofit;
    private ServiceApi service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityFormularioBorgBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());


        configurarAlert();


        preferences  = getSharedPreferences("user_prefs", MODE_PRIVATE);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        retrofit = RetrofitUtil.createRetrofit();
        service = RetrofitUtil.createService(retrofit);


        // Referência ao Spinner no layout
        Spinner spinner = findViewById(R.id.spinner_atividade);

        // Criar um ArrayAdapter usando o array de opções e um layout padrão do Android
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.atividades_array, android.R.layout.simple_spinner_item);

        // Especificar o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar o adaptador ao spinner
        spinner.setAdapter(adapter);

        // Definir um ouvinte para lidar com a seleção do item no spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // O código aqui será executado quando uma opção for selecionada
                opcaoNomeAtividade  = parentView.getItemAtPosition(position).toString();
                Toast.makeText(BorgActivity.this, "Opção selecionada: " + opcaoNomeAtividade, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este método é chamado quando nada está selecionado
            }
        });

        // Referência ao Spinner no layout
        Spinner spinner2 = findViewById(R.id.spinner_Borg);

        // Criar um ArrayAdapter usando o array de opções e um layout padrão do Android
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.intensidade_array, android.R.layout.simple_spinner_item);

        // Especificar o layout que será usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar o adaptador ao spinner
        spinner2.setAdapter(adapter2);

        // Definir um ouvinte para lidar com a seleção do item no spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // O código aqui será executado quando uma opção for selecionada
                opcaIntensidadeAtividade = parentView.getItemAtPosition(position).toString();
                Toast.makeText(BorgActivity.this, "Opção selecionada: " + opcaIntensidadeAtividade, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este método é chamado quando nada está selecionado
            }
        });

        vb.volarButton.setOnClickListener(view -> {
            finish();
        });


        vb.SalvarButton.setOnClickListener(view -> {
            try{
                ModeloAtividadeFisica.Exercise exercise = new ModeloAtividadeFisica.Exercise();
                ModeloAtividadeFisica atividadeModelo = new ModeloAtividadeFisica();
                atividadeModelo.setExercise_metric(exercise);
                atividadeModelo.exercise_metric.setName(
                        opcaoNomeAtividade
                );
                atividadeModelo.exercise_metric.setIntensity(
                        opcaIntensidadeAtividade
                );
                atividadeModelo.exercise_metric.setDistance_in_m(
                        Integer.parseInt(vb.edtDistancia.getText().toString())
                );
                atividadeModelo.exercise_metric.setDuration_in_min(
                        Integer.parseInt(vb.edtMinutos.getText().toString())
                );
                atividadeModelo.exercise_metric.setUser_id(
                        preferences.getInt("id", 0)
                );

                ModeloBodyPostAtividade modeloBodyPostAtividade = new ModeloBodyPostAtividade(atividadeModelo.exercise_metric);
                dialog_carregando.show();
                service.criarAtividade(
                        atividadeModelo.getExercise_metric().getUser_id(),
                        modeloBodyPostAtividade.getExercise_metric()
                        ).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ModeloRespostaAtividadeFisica> call, Response<ModeloRespostaAtividadeFisica> response) {
                        dialog_carregando.dismiss();

                        if (response.isSuccessful()) {
                            Log.d("mayara", "onResponse: " + response.body().getData().toString());
                            Toast.makeText(BorgActivity.this, "Atividade Criada!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), BorgActivity.class));
                        }else{
                            Log.d("mayara", "onResponse: " + response.code());
                            Log.d("mayara", "onResponse: " + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ModeloRespostaAtividadeFisica> call, Throwable t) {
                        dialog_carregando.dismiss();
                        Toast.makeText(BorgActivity.this, "Erro na requisição, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                });


            }catch (Exception e){
                Log.d("mayara", "onCreate: " + e.getMessage());
                Toast.makeText(this, "Verifique os dados e tente novamente!", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }


}
