package com.example.appform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.appform.databinding.ActivityFormularioBinding;

public class MainActivity extends AppCompatActivity {


    private Button btnCad;

    private Button btnLogin;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCad = findViewById(R.id.btn_cad);
        btnLogin = findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(vLogin ->{
            startActivity(
                    new Intent(getApplicationContext(), StepCount.class)
            );
        });


        btnCad.setOnClickListener( viewCad -> {
            startActivity(
                    new Intent(getApplicationContext(), Formulario.class)
            );
        });
    }
}