package com.example.appform;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appform.databinding.ActivityFormularioBinding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModeloUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private ActivityFormularioBinding vb;
    private String nome, senha,email, telefone;
    private String sexo;
    private int idade;
    private Double peso, altura;
    private AlertDialog dialog_carregando;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityFormularioBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        configurarAlert();

        vb.btnConcluir.setOnClickListener( viewProx -> {

            nome = vb.edtNome.getText().toString().trim();
            senha = vb.edtPass.getText().toString().trim();
            email = vb.edtEmail.getText().toString().trim();
            telefone = vb.edtTel.getText().toString().trim();
            sexo = vb.edtSexo.getText().toString();
            String idade_texto = vb.edtIdade.getText().toString();
            String peso_texto = vb.edtPeso.getText().toString().replace(",", ".");
            String altura_texto = vb.edtAltura.getText().toString().replace(",", ".");
            idade = Integer.parseInt( idade_texto );
            peso = Double.parseDouble( peso_texto );
            altura = Double.parseDouble( altura_texto );


            if (
                !nome.isEmpty()
                && !senha.isEmpty()
                && !email.isEmpty()
                && !telefone.isEmpty()
                && !sexo.isEmpty()
                && !idade_texto.isEmpty()
                && !peso_texto.isEmpty()
                && !altura_texto.isEmpty()
            ){
                ModeloUsuario modeloUsuario = new ModeloUsuario();

                modeloUsuario.setEmail(email);
                modeloUsuario.setNome(nome);
                modeloUsuario.setTelefone(telefone);
                modeloUsuario.setAltura(altura);
                modeloUsuario.setIdade(idade);
                modeloUsuario.setPeso(peso);
                modeloUsuario.setSexo(sexo);
                modeloUsuario.setPassos(0);

                dialog_carregando.show();
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(
                        task -> {
                            if( task.isSuccessful() ){

                                FirebaseUser user = task.getResult().getUser();
                                String userUID = user.getUid();

                                DatabaseReference novoUsuarioRef = usuariosRef.child(userUID);
                                novoUsuarioRef.setValue(modeloUsuario).addOnCompleteListener(task1 -> {

                                    if ( task1.isSuccessful() ){
                                        dialog_carregando.dismiss();
                                        startActivity(new Intent(getApplicationContext(), ContadorPassosActivity.class));
                                        Toast.makeText(FormularioActivity.this, "Cadastro completo!", Toast.LENGTH_SHORT).show();
                                    } else  {
                                        dialog_carregando.dismiss();

                                        Toast.makeText(
                                                FormularioActivity.this, "Erro ao fazer cadastro no banco, tente novamente!", Toast.LENGTH_LONG
                                        ).show();
                                    }

                                });
                            }else{
                                dialog_carregando.dismiss();
                                Toast.makeText(
                                        FormularioActivity.this, "Erro ao fazer cadastro, tente novamente!", Toast.LENGTH_LONG
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
        AlertDialog.Builder builder = new AlertDialog.Builder(FormularioActivity.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }

}