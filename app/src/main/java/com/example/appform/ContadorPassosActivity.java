package com.example.appform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class ContadorPassosActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private int previewCount = 0;
    private boolean isSensorPresent;
    Context context;
    private TextView countTextView;
    private ProgressBar circleBar;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        context = this;
        circleBar = findViewById(R.id.progres_steps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        countTextView = findViewById(R.id.Count_TextView);

        resetStep();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        possuiSensor();


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
            int steptaken = stepCount - previewCount;
            countTextView.setText("Passos: " + steptaken);

            circleBar.setProgress(steptaken);
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

        countTextView.setOnClickListener(viewClick -> Snackbar.make(viewClick, "Segure Para Resetar o Contador", Snackbar.LENGTH_SHORT).show());

        countTextView.setOnLongClickListener(viewLongClick ->{

            previewCount = stepCount;
            countTextView.setText("Passos: 0");
            circleBar.setProgress(0);
            return true;
        });

    }


}