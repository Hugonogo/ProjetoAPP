package com.example.appform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.example.appform.databinding.ActivityFormulario2Binding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModeloUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Formulario2 extends AppCompatActivity {

    private ActivityFormulario2Binding vb;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    private String sexo;
    private int idade;
    private Double peso, altura;

    private AlertDialog dialog_carregando;

    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityFormulario2Binding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        configurarAlert();

        if ( user == null){
            Toast.makeText(this, "Usuarios não cadastrado ainda.", Toast.LENGTH_SHORT).show();
            finish();
        }

        vb.btnConcluir.setOnClickListener( concluirView -> {
            sexo = vb.edtSexo.getText().toString();
            String idade_texto = vb.edtIdade.getText().toString();
            String peso_texto = vb.edtPeso.getText().toString().replace(",", ".");
            String altura_texto = vb.edtAltura.getText().toString().replace(",", ".");
            idade = Integer.parseInt( idade_texto );
            peso = Double.parseDouble( peso_texto );
            altura = Double.parseDouble( altura_texto );

            if (    !sexo.isEmpty() &&
                    !idade_texto.isEmpty() &&
                    !peso_texto.isEmpty() &&
                    !altura_texto.isEmpty() ){

                dialog_carregando.show();

                usuariosRef.child(
                        Base64.encodeToString(user.getEmail().getBytes(), Base64.DEFAULT)
                ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModeloUsuario modeloUsuario = snapshot.getValue(ModeloUsuario.class);
                        modeloUsuario.setAltura(altura);
                        modeloUsuario.setIdade(idade);
                        modeloUsuario.setPeso(peso);
                        modeloUsuario.setSexo(sexo);

                        usuariosRef.child(
                                Base64.encodeToString(user.getEmail().getBytes(), Base64.DEFAULT)
                        ).setValue(
                                modeloUsuario
                        ).addOnCompleteListener(task -> {
                            if ( task.isSuccessful() ){
                                dialog_carregando.dismiss();
                                Toast.makeText(Formulario2.this, "Cadastro completo!", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog_carregando.dismiss();
                                Toast.makeText(Formulario2.this, "Errp ao realizar cadastro", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Formulario2.this, error.toString(), Toast.LENGTH_SHORT).show();
                        dialog_carregando.dismiss();
                    }
                });


            }else{
                Snackbar.make(
                        concluirView, "Preencha todos os campos!", Snackbar.LENGTH_LONG
                ).show();
            }
        });
    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Formulario2.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }
}