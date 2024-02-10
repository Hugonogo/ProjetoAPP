package com.example.appform;


import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appform.databinding.ActivityStepCountBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;

public class ContadorPassosActivity extends AppCompatActivity{


    private ActivityStepCountBinding vb;

    private AlertDialog dialog_carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vb = ActivityStepCountBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(vb.getRoot());

        configurarAlert();

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        vb.btnCadastrarAtitivade.setOnClickListener(view ->  startActivity(new Intent(this, BorgActivity.class)));

    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }
}
