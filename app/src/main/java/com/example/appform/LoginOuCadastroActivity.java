package com.example.appform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appform.databinding.ActivityLoginCadastroBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModelResponse;
import com.example.appform.model.ModeloUsuario;
import com.example.appform.model.ModeloUsuarioKarine;
import com.example.appform.retrofitUtils.RetrofitUtil;
import com.example.appform.retrofitUtils.ServiceApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginOuCadastroActivity extends AppCompatActivity {

    private AlertDialog dialog_carregando;
    private ActivityLoginCadastroBinding vb ;

    private Retrofit retrofit;
    private ServiceApi service;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        preferences  = getSharedPreferences("user_prefs", MODE_PRIVATE);
        editor = preferences.edit();

        configurarAlert();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        retrofit = RetrofitUtil.createRetrofit();
        service = RetrofitUtil.createService(retrofit);

        vb.loginBtn.setOnClickListener(view -> {
            String email = vb.edtDigiteEmail.getText().toString().trim();

            if ( !email.isEmpty() ){
                dialog_carregando.show();

                service.getUsuarios().enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                        dialog_carregando.dismiss();
                        if (response.isSuccessful()) {

                            boolean isEncontrado = false;
                            assert response.body() != null;
                            for (ModeloUsuarioKarine usuario : response.body().getData()) {

                                if (usuario.getAttributes().getEmail().trim().equals(email)) {

                                    Toast.makeText(LoginOuCadastroActivity.this, "Usuário Reconhecido", Toast.LENGTH_SHORT).show();
                                    editor.putInt("id", usuario.getAttributes().getId()).apply();
                                    startActivity(new Intent(LoginOuCadastroActivity.this, ContadorPassosActivity.class));
                                    isEncontrado = true;
                                }
                            }

                            if (!isEncontrado) {
                                Toast.makeText(LoginOuCadastroActivity.this, "Usuário Não Encontrado!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelResponse> call, Throwable t) {
                        dialog_carregando.dismiss();
                        Log.d("mayara", "onFailure: " + t.getMessage());
                        Toast.makeText(LoginOuCadastroActivity.this, "Erro na requisição, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginOuCadastroActivity.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }
}