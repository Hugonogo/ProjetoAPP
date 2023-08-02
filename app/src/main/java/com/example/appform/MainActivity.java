package com.example.appform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.appform.databinding.ActivityFormularioBinding;
import com.example.appform.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private Button btnCad;

    private Button btnLogin;
    
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ActivityMainBinding vb ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        btnCad = findViewById(R.id.btn_cad);
        btnLogin = findViewById(R.id.login_btn);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // verifica se o user já ta logado
        if ( user != null){
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
            startActivity(
                    new Intent(getApplicationContext(), StepCount.class)
            );
        }else{
            Toast.makeText(this, "Faça login ou se cadastre!", Toast.LENGTH_SHORT).show();
        }
        
        btnLogin.setOnClickListener(vLogin ->{
            String email, senha;
            email = vb.edtEmail.getText().toString().trim();
            senha = vb.Password.getText().toString().trim();

            if ( email != null && senha != null){
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                    if ( task.isSuccessful() ){
                        startActivity(
                                new Intent(getApplicationContext(), StepCount.class)
                        );
                    }else{
                        Toast.makeText(MainActivity.this, "Credenciais erradas.", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });


        btnCad.setOnClickListener( viewCad -> {
            startActivity(
                    new Intent(getApplicationContext(), Formulario.class)
            );
        });
    }
}