package com.example.appform;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appform.database.CountsDatabase;

public class AcelerometroActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double magnitude;
    private final double nivelSedenario = 10;
    private final double nivelModerado = 15;
    private boolean isAcelerometroAtivo = false;
    private Button startStopButton;
    private String niveis, eixoValues, atiValue;
    private TextView accelerationValues, activityLevel;
    float xAxis, zAxis, yAxis;
    double meX, meY, meZ;
    private CountsDatabase databaseHelper;
    private Handler handler;
    private final int INTERVALO_SALVAR_DADOS = 5000; // 5 segundos em milissegundos
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);


        startStopButton = findViewById(R.id.startStopButton);
        startStopButton.setOnClickListener(v -> toggleAcelerometro());
        accelerationValues = findViewById(R.id.accelerationValues);
        activityLevel = findViewById(R.id.nivelAtividade);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        databaseHelper = new CountsDatabase(this);
        handler = new Handler();
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

            magnitude = Math.sqrt(meX * meX + meY * meY + meZ * meZ);




            eixoValues = String.format("X: %.2f\nY: %.2f\nZ: %.2f", xAxis, yAxis, zAxis);
            accelerationValues.setText(eixoValues);

            atiValue = String.format("Magnitude: %.2f\nNível de Atividade: %s", magnitude, niveis);
            activityLevel.setText(atiValue);

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
    private void pauseAcelerometro() {
        isAcelerometroAtivo = false;
        handler.removeCallbacks(salvarDadosRunnable);
        // Parar de receber atualizações do sensor
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            selectAll();
            calcularMedia();
            mensurarNivel();
            atiValue = String.format("Magnitude: %.2f\nNível de Atividade: %s", magnitude, niveis);
            activityLevel.setText(atiValue);
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
}