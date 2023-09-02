package com.example.appform;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appform.util.PassoUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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



    private DatabaseReference passosRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        context = this;
        circleBar = findViewById(R.id.progres_steps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        countTextView = findViewById(R.id.Count_TextView);
        updateStepCountView();
        // Inicializa a referência do Firebase se o usuário estiver logado
        if ( user != null){
            passosRef = FirebaseDatabase.getInstance()
                        .getReference("usuarios")
                        .child(user.getUid())
                        .child("passos");

        }else{
            Toast.makeText(context, "Faça login primeiro!", Toast.LENGTH_SHORT).show();
            finish();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Verifica se o dispositivo possui o sensor de contador de passos
        possuiSensor();
        loadData();
        AlarmeDeAbrirApp();


    }

    // Método chamado quando a atividade está pausada
    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            sensorManager.unregisterListener(this, stepCounterSensor);
        }

    }
    // Método chamado quando a atividade é retomada
    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent && sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    //Método para contar os passos
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //se o sensor utilizado for o "stepCounterSensor"
        if (sensorEvent.sensor == stepCounterSensor) {
            stepCount = (int) sensorEvent.values[0];
            steptaken = stepCount - previewCount;

            updateStepCountView();
            atualizarPassoFirebase(steptaken);
            PassoUtil.saveData(previewCount, this);
        }
    }
    // Atualiza o valor do contador de passos no Firebase
    public void atualizarPassoFirebase(int passos){
            passosRef.setValue(passos);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    // Verifica se o dispositivo possui o sensor StepCounter
    public void possuiSensor(){

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent =  true;
        }else{
            Toast.makeText(context, "Contador não disponivel no seu dispositivo", Toast.LENGTH_SHORT).show();
            isSensorPresent = false;
        }


    }
    // Agenda a abertura do aplicativo à meia-noite
    public void AlarmeDeAbrirApp() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AbrirAppMidNight.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 31);
        calendar.set(Calendar.SECOND, 55);



        long triggerTime = calendar.getTimeInMillis();
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            MidnightReset();
        }




    }


    //Reseta o contador de passos à Meia-Noite
    public static void MidnightReset() {
        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY, 21);
        midnight.set(Calendar.MINUTE, 25);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        Timer midnightResetTimer = new Timer();


        if (midnight.getTimeInMillis() <= System.currentTimeMillis()) {
            midnight.add(Calendar.DAY_OF_YEAR, 1);
        }


        long timeUntilMidnight = midnight.getTimeInMillis() - System.currentTimeMillis();


        midnightResetTimer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, timeUntilMidnight, 24 * 60 * 60 * 1000); // 24 horas em milissegundos

    }
    //Método de reset de passos
    @SuppressLint("SetTextI18n")
    public void resetStep(){
        previewCount = stepCount;
        steptaken = 0;
        PassoUtil.saveData(previewCount, this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateStepCountView();
            }
        });
    }

    private void updateStepCountView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countTextView.setText("Passos: " + steptaken);
                circleBar.setProgress(steptaken);
            }
        });
    }
    //Salva a contagem de passos no sharedPreferences

    //Recarrega os passos do SharedPreferences
    private void loadData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        steptaken = preferences.getInt("chave-passos", 0);
        previewCount = steptaken;


    }
}

