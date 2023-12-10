package com.example.appform;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BorgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_borg);

        Spinner spinnerAtividade = findViewById(R.id.spinner_atividade);
        Spinner spinnerBorg = findViewById(R.id.spinner_Borg);

        // Definir opções para o spinner de exercícios
        ArrayAdapter<CharSequence> exerciseAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.atividades_array,
                android.R.layout.simple_spinner_item
        );
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAtividade.setAdapter(exerciseAdapter);

        // Definir opções para o spinner de nível de esforço
        ArrayAdapter<CharSequence> effortLevelAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.intensidade_array,
                android.R.layout.simple_spinner_item
        );
        effortLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBorg.setAdapter(effortLevelAdapter);

        // Adicionar listener para o spinner de exercícios
        spinnerAtividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obter o item selecionado no spinner de exercícios
                String selectedExercise = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Exercício selecionado: " + selectedExercise, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nada a fazer aqui
            }
        });

        // Adicionar listener para o spinner de nível de esforço
        spinnerBorg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obter o item selecionado no spinner de nível de esforço
                String selectedEffortLevel = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Nível de esforço selecionado: " + selectedEffortLevel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nada a fazer aqui
            }
        });
    }
}
