package com.example.appform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ContadorPassosActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private int previewCount = 0;
    private int steptaken;
    private boolean isSensorPresent;
    Context context;
    private TextView countTextView;
    private ProgressBar circleBar;



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        context = this;
        circleBar = findViewById(R.id.progres_steps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        countTextView = findViewById(R.id.Count_TextView);
        countTextView.setText("Passos: " + steptaken);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        possuiSensor();
        loadData();
        resetStep();

    }

    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            sensorManager.unregisterListener(this, stepCounterSensor);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent && sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == stepCounterSensor) {
            stepCount = (int) sensorEvent.values[0];
            steptaken = stepCount - previewCount;

            countTextView.setText("Passos: " + steptaken);

            circleBar.setProgress(steptaken);
            saveData();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void possuiSensor(){

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent =  true;
        }else{
            Toast.makeText(context, "Contador não disponivel no seu dispositivo", Toast.LENGTH_SHORT).show();
            isSensorPresent = false;
        }


    }

    @SuppressLint("SetTextI18n")
    public void resetStep(){
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        if (hora == 0 && minuto == 0){
            previewCount = stepCount;
            steptaken = 0;
            saveData();
            countTextView.setText("Passos: "+steptaken);
            circleBar.setProgress(steptaken);
        }

    }


    private void saveData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("chave-passos", previewCount);
        editor.apply();

    }
    private void loadData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        steptaken = preferences.getInt("chave-passos", 0);
        previewCount = steptaken;


    }
}

