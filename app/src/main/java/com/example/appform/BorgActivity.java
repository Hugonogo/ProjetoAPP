package com.example.appform;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class BorgActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_borg);

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
                String opcaoSelecionada = parentView.getItemAtPosition(position).toString();
                Toast.makeText(BorgActivity.this, "Opção selecionada: " + opcaoSelecionada, Toast.LENGTH_SHORT).show();
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
                String opcaoSelecionada2 = parentView.getItemAtPosition(position).toString();
                Toast.makeText(BorgActivity.this, "Opção selecionada: " + opcaoSelecionada2, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este método é chamado quando nada está selecionado
            }
        });
    }
}
