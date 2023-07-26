package com.example.appform;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class StepCount extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private boolean isSensorPresent;
    Context context;
    private TextView countTextView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        context = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        countTextView = findViewById(R.id.Count_TextView);


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
            sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == stepCounterSensor) {
            stepCount = (int) sensorEvent.values[0];
            countTextView.setText(String.valueOf(stepCount));
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
}