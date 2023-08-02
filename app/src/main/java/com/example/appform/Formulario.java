package com.example.appform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appform.databinding.ActivityFormularioBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModeloUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Formulario extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private ActivityFormularioBinding vb;
    private String nome, senha,email, telefone;
    private AlertDialog dialog_carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityFormularioBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        configurarAlert();

        vb.btnProx.setOnClickListener( viewProx -> {

            nome = vb.edtNome.getText().toString().trim();
            senha = vb.edtPass.getText().toString().trim();
            email = vb.edtEmail.getText().toString().trim();
            telefone = vb.edtTel.getText().toString().trim();


            if (
                    !nome.isEmpty() && !senha.isEmpty() && !email.isEmpty() && !telefone.isEmpty()
            ){
                ModeloUsuario modeloUsuario = new ModeloUsuario();

                modeloUsuario.setEmail(email);
                modeloUsuario.setNome(nome);
                modeloUsuario.setTelefone(telefone);

                dialog_carregando.show();
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(
                        task -> {
                            if( task.isSuccessful() ){

                                String id = Base64.encodeToString(email.getBytes(), Base64.DEFAULT);

                                DatabaseReference novoUsuarioRef = usuariosRef.child(
                                        id
                                );

                                novoUsuarioRef.setValue(modeloUsuario).addOnCompleteListener(task1 -> {

                                    if ( task1.isSuccessful() ){
                                        dialog_carregando.dismiss();
                                        Intent i = new Intent(getApplicationContext(), Formulario2.class);
                                        i.putExtra("id", id);
                                        startActivity(
                                            i
                                        );
                                    }

                                    else{
                                        dialog_carregando.dismiss();
                                        Log.d("bucetinha", id);
                                        Log.d("bucetinha", task1.toString());
                                        Toast.makeText(
                                                Formulario.this, "Erro ao fazer cadastro no banco, tente novamente!", Toast.LENGTH_LONG
                                        ).show();
                                    }
                                });
                            }else{
                                dialog_carregando.dismiss();
                                Toast.makeText(
                                        Formulario.this, "Erro ao fazer cadastro, tente novamente!", Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
            }else{
                Snackbar.make(
                        viewProx, "Preencha todos os campos!", Snackbar.LENGTH_LONG
                ).show();
            }

        });
    }


    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }

}