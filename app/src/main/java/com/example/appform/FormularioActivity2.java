package com.example.appform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appform.databinding.ActivityFormulario2Binding;
import com.example.appform.databinding.CarregandoLayoutBinding;
import com.example.appform.model.ModeloUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class FormularioActivity2 extends AppCompatActivity {

    private ActivityFormulario2Binding vb;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private DatabaseReference alturaRef, sexoRef, idadeRef, pesoRef, ativoRef, tipoAtivRef, freqAtivRef, tempoAtivRef;


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String praticaAtividade;
    private boolean isAtivo;
    private String sexo, tipoAtiv;
    private int idade, quantAtiv, tempoAtiv;
    private Double peso, altura;

    private AlertDialog dialog_carregando;

    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityFormulario2Binding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        configurarAlert();





        // pega o id do usuário do primeiro forumalrio
        if ( user != null){
            pesoRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("peso");

            idadeRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("idade");

            sexoRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("sexo");

            alturaRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("altura");

            ativoRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("pratica_atividade");
            tipoAtivRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("tipo_de_atividade");
            freqAtivRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("frequencia_de_atividade");

            tempoAtivRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("tempo_em_minutos");

        }

        vb.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioAtividadeSim:
                        praticaAtividade = "sim";
                        isAtivo = true;
                        vb.txtCadastrarTipoAtiv.setVisibility(View.VISIBLE);
                        vb.tipoAtividade.setVisibility(View.VISIBLE);
                        vb.txtCadastrarFreqAtiv.setVisibility(View.VISIBLE);
                        vb.frequenciaAtividade.setVisibility(View.VISIBLE);
                        vb.txtCadastrarTempoAtiv.setVisibility(View.VISIBLE);
                        vb.edtTempoAtiv.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioAtividadeNao:
                        praticaAtividade = "não";
                        isAtivo = false;
                        vb.txtCadastrarTipoAtiv.setVisibility(View.GONE);
                        vb.tipoAtividade.setVisibility(View.GONE);
                        vb.txtCadastrarFreqAtiv.setVisibility(View.GONE);
                        vb.frequenciaAtividade.setVisibility(View.GONE);
                        break;
                }
            }
        });


        vb.btnConcluir.setOnClickListener( concluirView -> {
            String idade_texto = "";
            String peso_texto = "";
            String altura_texto = "";

            idade_texto = vb.edtIdade.getText().toString();
            peso_texto = vb.edtPeso.getText().toString().replace(",", ".");
            altura_texto = vb.edtAltura.getText().toString().replace(",", ".");


            if (
                    !idade_texto.isEmpty() &&
                    !peso_texto.isEmpty() &&
                    !altura_texto.isEmpty() && (!(sexo == null)) && (!(praticaAtividade == null))){



                dialog_carregando.show();
                {
                    idade = Integer.parseInt( idade_texto );
                    peso = Double.parseDouble( peso_texto );
                    altura = Double.parseDouble( altura_texto );

                    if (Objects.equals(praticaAtividade, "sim")) {
                        tipoAtiv = vb.tipoAtividade.getText().toString();
                        String textoQuant = vb.frequenciaAtividade.getText().toString();
                        String textTempoAtiv = vb.edtTempoAtiv.getText().toString().replace(",", ".");



                        if(!tipoAtiv.isEmpty() && !textoQuant.isEmpty()){
                            quantAtiv = Integer.parseInt(textoQuant);
                            tempoAtiv = Integer.parseInt(textTempoAtiv);

                            atualizarFirebase(peso, idade, sexo, altura, isAtivo, tipoAtiv, quantAtiv, tempoAtiv);
                            Toast.makeText(this, "Cadastro Concluido", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ContadorPassosActivity.class));

                        }
                        else{
                            Snackbar.make(concluirView, "Preencha todos os campos com suas informações", Snackbar.LENGTH_LONG).show();
                        }

                    }
                    else {
                        atualizarFirebase(peso, idade, sexo, altura, isAtivo, tipoAtiv, quantAtiv, tempoAtiv);
                        Toast.makeText(this, "Cadastro Concluido", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ContadorPassosActivity.class));
                    }


                }


            }else{
                Snackbar.make(
                        concluirView, "Preencha todos os campos!", Snackbar.LENGTH_LONG
                ).show();
            }
        });
    }

    public void atualizarFirebase(double peso, int idade, String sexo, double altura, boolean isAtivo, String tipoAtiv, int quantAtiv, int tempoAtiv){
        pesoRef.setValue(peso);
        idadeRef.setValue(idade);
        sexoRef.setValue(sexo);
        alturaRef.setValue(altura);
        ativoRef.setValue(isAtivo);
        tipoAtivRef.setValue(tipoAtiv);
        freqAtivRef.setValue(quantAtiv);
        tempoAtivRef.setValue(tempoAtiv);
    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FormularioActivity2.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_femenino:
                if (checked) {
                    sexo = "Femenino";
                    break;
                }
            case R.id.radio_masculino:
                if (checked) {
                    sexo = "Masculino";
                    break;
                }

        }
    }

}