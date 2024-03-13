package com.example.appform;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appform.database.ActivityDataModel;
import com.example.appform.database.ActivityDatabase;
import com.example.appform.database.CountsDatabase;
import com.example.appform.database.PassosDBHelper;
import com.example.appform.database.PassosDataSource;

import java.util.HashMap;
import java.util.List;

public class AcelerometroActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double magnitude;
    private final double nivelSedenario = 10;
    private final double nivelModerado = 15;
    private boolean isAcelerometroAtivo = false;
    private Button startStopButton;
    private String niveis, eixoValues, atiValue, lastId, lastActivityType, lastActivityTimeMinutes,
            lastActivityLevel;
    private TextView accelerationValues, activityLevel;
    float xAxis, zAxis, yAxis;
    double meX, meY, meZ;
    private CountsDatabase databaseHelper;
    private Handler handler;
    private long primeiraContagem;
    private long ultimaContagem;
    private ActivityDatabase activityDatabase;
    private Spinner SpinerAtividade;
    private final int INTERVALO_SALVAR_DADOS = 5000; // 5 segundos em milissegundos
    private AcelerometroService acelerometroService;
    private  int passos;
    private PassosDataSource dataSource;
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);
        SpinerAtividade = findViewById(R.id.spinner_atividade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.atividades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinerAtividade.setAdapter(adapter);
        //float xAxis1 = acelerometroService.xAxis;
        dataSource = new PassosDataSource(this);


        SpinerAtividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // O código aqui será executado quando uma opção for selecionada
                String opcaoSelecionada = parentView.getItemAtPosition(position).toString();
                Toast.makeText(AcelerometroActivity.this, "Opção selecionada: " + opcaoSelecionada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este método é chamado quando nada está selecionado
            }
        });
        startStopButton = findViewById(R.id.startStopButton);
        startStopButton.setOnClickListener(v -> toggleAcelerometro());

        activityDatabase = new ActivityDatabase(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        databaseHelper = new CountsDatabase(this);
        handler = new Handler();

        // Se o serviço ainda não estiver em execução, inicie-o
        if (acelerometroService == null) {
            Intent serviceIntent = new Intent(this, AcelerometroService.class);
            startService(serviceIntent);
        }
    }
    private Runnable salvarDadosRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAcelerometroAtivo) {
                // Salvar os valores do acelerômetro no banco de dados
                databaseHelper.insertData(xAxis, yAxis, zAxis);
            }
            handler.postDelayed(this, INTERVALO_SALVAR_DADOS);
        }
    };


    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // Aqui, os counts podem ser acessados em values[0], values[1], values[2]
            xAxis = values[0];
            yAxis = values[1];
            zAxis = values[2];




        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // Método para alternar o estado do acelerômetro
    private void toggleAcelerometro() {
        if (isAcelerometroAtivo) {
            pauseAcelerometro();
        } else {
            startAcelerometro();
        }
    }

    // Método para iniciar o acelerômetro
    private void startAcelerometro() {
        isAcelerometroAtivo = true;
        zerarTabela();
        primeiraContagem = System.currentTimeMillis();
        handler.postDelayed(salvarDadosRunnable, INTERVALO_SALVAR_DADOS);
        // Registrar o sensor para começar a receber atualizações
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        // Atualizar texto do botão para indicar a ação de pausar
        Button startStopButton = findViewById(R.id.startStopButton);
        startStopButton.setText("Pausar");

    }

    // Método para pausar o acelerômetro
    @SuppressLint("DefaultLocale")
    private void pauseAcelerometro() {
        isAcelerometroAtivo = false;
        handler.removeCallbacks(salvarDadosRunnable);
        ultimaContagem = System.currentTimeMillis();  // Obter o tempo de término
        // Parar de receber atualizações do sensor
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            selectAll();
            calcularMedia();
            mensurarNivel();
            calcularTempoTotal();
            salvarAtividade();
            mostrarTabelaNoLog();
            saveInfo();

        }

        // Atualizar texto do botão para indicar a ação de iniciar
        Button startStopButton = findViewById(R.id.startStopButton);
        startStopButton.setText("Iniciar");

    }
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

    }
    public void mensurarNivel(){
        magnitude = Math.sqrt(meX * meX + meY * meY + meZ * meZ);
        Log.d("Mag", "Magnitude: " + magnitude);
        if (magnitude < nivelSedenario) {
            niveis = "Sedentário";
        } else if (magnitude < nivelModerado) {
            niveis = "Atividade Moderada";
        } else {
            niveis = "Atividade Intensa";
        }
    }
    // Método que calcula a média do eixo x y e z
    @SuppressLint("Range")
    private void calcularMedia() {
        CountsDatabase databaseHelper = new CountsDatabase(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT AVG(" + CountsDatabase.COL_X + ") AS avgX, AVG(" +
                CountsDatabase.COL_Y + ") AS avgY, AVG(" + CountsDatabase.COL_Z + ") AS avgZ FROM " +
                CountsDatabase.TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            meX = cursor.getDouble(cursor.getColumnIndex("avgX"));
            meY = cursor.getDouble(cursor.getColumnIndex("avgY"));
            meZ = cursor.getDouble(cursor.getColumnIndex("avgZ"));

            // Exibir no log do Android Studio
            Log.d("AcelerometroAverages", "Média do eixo X: " + meX);
            Log.d("AcelerometroAverages", "Média do eixo Y: " + meY);
            Log.d("AcelerometroAverages", "Média do eixo Z: " + meZ);

            cursor.close();
        }
        db.close();
    }

    // metodo para fazer um select da tabela
    private void selectAll() {
        databaseHelper = new CountsDatabase(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CountsDatabase.TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(CountsDatabase.COL_ID));
                @SuppressLint("Range") double xValue = cursor.getDouble(cursor.getColumnIndex(CountsDatabase.COL_X));
                @SuppressLint("Range") double yValue = cursor.getDouble(cursor.getColumnIndex(CountsDatabase.COL_Y));
                @SuppressLint("Range") double zValue = cursor.getDouble(cursor.getColumnIndex(CountsDatabase.COL_Z));
                @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex(CountsDatabase.COL_TIMESTAMP));

                // Exibir no log do Android Studio
                Log.d("AcelerometroData", "ID: " + id + ", X: " + xValue + ", Y: " + yValue + ", Z: " + zValue + ", Timestamp: " + timestamp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }
    private void zerarTabela() {
        CountsDatabase databaseHelper = new CountsDatabase(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Excluir todos os registros da tabela
        db.delete(CountsDatabase.TABLE_NAME, null, null);

        db.close();
    }

    private void calcularTempoTotal() {
        long tempoTotalMillis = ultimaContagem - primeiraContagem;
        long tempoTotalMinutos = tempoTotalMillis / (60 * 1000);  // Converter para minutos

        // Agora, 'tempoTotalMinutos' contém a diferença de tempo em minutos
        Log.d("AcelerometroTime", "Tempo Total em Minutos: " + tempoTotalMinutos);
    }

    private void salvarAtividade() {
        // Obter o tipo de atividade do Spinner
        String activityType = SpinerAtividade.getSelectedItem().toString();

        // Calcular a diferença de tempo em minutos
        long tempoTotalMillis = ultimaContagem - primeiraContagem;
        long tempoTotalMinutos = tempoTotalMillis / (60 * 1000);

        // Obter o nível de atividade
        String nivelAtividade = niveis;

        // Salvar no banco de dados
        activityDatabase.insertActivityData(activityType, (int) tempoTotalMinutos, nivelAtividade);
    }

    private void mostrarTabelaNoLog() {
        Log.d("AcelerometroActivity", "Exibindo Tabela de Atividades:");

        // Recuperar todos os registros da tabela
        List<HashMap<String, String>> activityDataList = activityDatabase.getAllActivityData();

        // Iterar sobre os registros e exibir no Log
        for (HashMap<String, String> activityData : activityDataList) {
            Log.d("AcelerometroActivity", "ID: " + activityData.get("id") +
                    ", Tipo: " + activityData.get("activityType") +
                    ", Tempo (min): " + activityData.get("activityTimeMinutes") +
                    ", Nível: " + activityData.get("activityLevel"));
        }

    }

//    Salva as informações das tabelas em variáveis
    @SuppressLint("Range")
    private void saveInfo(){
        HashMap<String, String> lastActivityData = activityDatabase.getLastActivityData();
//        todas as variáveis da tabela de atividades
        lastId = lastActivityData.get("id");
        lastActivityType = lastActivityData.get("activityType");
        lastActivityTimeMinutes = lastActivityData.get("activityTimeMinutes");
        lastActivityLevel = lastActivityData.get("activityLevel");
        dataSource.open();
        Cursor cursor = dataSource.obterPassosDiarios();

        if (cursor !=  null){
            cursor.moveToLast();
//            variável passos pegando o ultimo registro da tabela passos
            passos = cursor.getInt(cursor.getColumnIndex(PassosDBHelper.COLUMN_PASSOS));
        }
        cursor.close();
        dataSource.close();

        Log.d("Ultimos registros", "ultimos registros:");
        Log.d("Ultimos registros",
                "Passos: "+passos+
                        ", tipo: "+lastActivityType+
                        ", level: "+lastActivityLevel);

    }
}
