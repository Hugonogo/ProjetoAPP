package com.example.appform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appform.database.PassosDBHelper;
import com.example.appform.database.PassosDataSource;
import com.example.appform.databinding.ActivityFormulario2Binding;
import com.example.appform.databinding.ActivityStepCountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.Calendar;
import java.text.SimpleDateFormat;
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

    private TextView dataView;

    private boolean isCountingSteps;
    private DatabaseReference passosRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Button cadastrarPassosButton, IniciarButton;

    private PassosDataSource dataSource;

    private String dataAtual;

    private Cursor cursor;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar data = Calendar.getInstance();
        // Formata a data para o formato desejado (por exemplo, "dd/MM/yyyy HH:mm:ss")
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataAtual = sdf.format(data.getTime());
        setContentView(R.layout.activity_step_count);
        context = this;
        dataSource = new PassosDataSource(this);
        dataView = findViewById(R.id.Data);
        circleBar = findViewById(R.id.progres_steps);
        loadIsCount();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        countTextView = findViewById(R.id.Count_TextView);
        updateStepCountView();
        dataView.setText(dataAtual+"");
        // Inicializa a referência do Firebase se o usuário estiver logado



        if (user != null) {
            passosRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("Registro de Datas");
            passosRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(user.getUid())
                    .child("passos");
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cadastrarPassosButton = findViewById(R.id.cadastrarPassosButton);
        IniciarButton = findViewById(R.id.IniciarButton);

        if (isCountingSteps){
            IniciarButton.setVisibility(View.GONE);
            cadastrarPassosButton.setVisibility(View.VISIBLE);
        }
        else{
            cadastrarPassosButton.setVisibility(View.GONE);
            IniciarButton.setVisibility(View.VISIBLE);

        }
        buttons();



        possuiSensor();
        loadData();
        //AlarmeDeAbrirApp();
        //MidnightReset();



    }

    // Método chamado quando a atividade está pausada
    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            dataSource.close();
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
        if(isCountingSteps){
            if (sensorEvent.sensor == stepCounterSensor) {
                stepCount = (int) sensorEvent.values[0];
                steptaken = stepCount - previewCount;

                updateStepCountView();

            }
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
    //..
    //public void AlarmeDeAbrirApp() {
    //        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    //        boolean isAlarmSet = preferences.getBoolean("Alarme", false);
    //        if (!isAlarmSet){
    //            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    //            Intent intent = new Intent(context, AbrirAppMidNight.class);
    //            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    //
    //            Calendar calendar = Calendar.getInstance();
    //            calendar.setTimeInMillis(System.currentTimeMillis());
    //            calendar.set(Calendar.HOUR_OF_DAY, 11);
    //            calendar.set(Calendar.MINUTE, 12);
    //            calendar.set(Calendar.SECOND, 55);
    //            long triggerTime = calendar.getTimeInMillis();
    //
    //            //if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
    //            //    calendar.add(Calendar.DAY_OF_YEAR, 1);
    //            //}
    //            if (alarmManager != null) {
    //                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    //                SharedPreferences.Editor editor = preferences.edit();
    //                editor.putBoolean("Alarme", true);
    //                editor.apply();
    //            }
    //        }
    //
    //
    //    }
    // .//

    //..
    //  public void MidnightReset() {
    //        Calendar midnight = Calendar.getInstance();
    //        midnight.setTimeInMillis(System.currentTimeMillis());
    //        midnight.set(Calendar.HOUR_OF_DAY, 11);
    //        midnight.set(Calendar.MINUTE, 12);
    //        midnight.set(Calendar.SECOND, 0);
    //        midnight.set(Calendar.MILLISECOND, 0);
    //
    //
    //
    //        if (midnight.getTimeInMillis() <= System.currentTimeMillis()) {
    //            midnight.add(Calendar.DAY_OF_YEAR, 1);
    //        }
    //
    //
    //        long timeUntilMidnight = midnight.getTimeInMillis() - System.currentTimeMillis();
    //
    //
    //        midnightResetTimer.schedule(new TimerTask() {
    //            @Override
    //            public void run() {
    //                resetStep();
    //            }
    //        }, timeUntilMidnight, 24 * 60 * 60 * 1000); // 24 horas em milissegundos
    //
    //    }


    public void resetStep(){

        previewCount = stepCount;
        steptaken = 0;
        saveData();
        runOnUiThread(this::updateStepCountView);
    }


    @SuppressLint("SetTextI18n")
    private void updateStepCountView() {
        runOnUiThread(() -> {
            countTextView.setText("Passos: " + steptaken);
            circleBar.setProgress(steptaken);
        });
    }

    private void saveData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("chave-passos", previewCount);
        editor.apply();
    }

    //Recarrega os passos do SharedPreferences
    private void loadData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        steptaken = preferences.getInt("chave-passos", 0);
        previewCount = steptaken;


    }
    private void saveIsCount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("data", isCountingSteps);
        editor.apply();
    }

    private void loadIsCount(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isCountingSteps = preferences.getBoolean("data", false);

    }
    private void buttons(){
        cadastrarPassosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chame o método para enviar os passos para o Firebase e resetar o contador
                //atualizarPassoFirebase(steptaken);

                dataSource.open();
                dataSource.salvarPassosDiarios(dataAtual, steptaken);

                Cursor cursor = dataSource.obterPassosDiarios();

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(PassosDBHelper.COLUMN_ID));
                            @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(PassosDBHelper.COLUMN_DATA));
                            @SuppressLint("Range") int passos = cursor.getInt(cursor.getColumnIndex(PassosDBHelper.COLUMN_PASSOS));

                            Log.d("MeuApp", "ID: " + id + ", Data: " + data + ", Passos: " + passos);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } else {
                    Log.d("MeuApp", "Nenhum dado encontrado no banco de dados.");
                }
                resetStep();

                isCountingSteps = false;
                saveIsCount();
                cadastrarPassosButton.setVisibility(View.GONE);
                IniciarButton.setVisibility(View.VISIBLE);



            }
        });

        IniciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCountingSteps = true;
                saveIsCount();
                IniciarButton.setVisibility(View.GONE);
                cadastrarPassosButton.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

}

