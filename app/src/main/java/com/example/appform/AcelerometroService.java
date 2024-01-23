package com.example.appform;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.example.appform.database.CountsDatabase;

public class AcelerometroService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double magnitude;
    float xAxis, zAxis, yAxis;
    private boolean isAcelerometroAtivo = false;
    private Handler handler;
    private CountsDatabase databaseHelper;
    private long primeiraContagem;
    private PowerManager.WakeLock wakeLock;

    private final int INTERVALO_SALVAR_DADOS = 5000; // 5 segundos em milissegundos

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        databaseHelper = new CountsDatabase(this);
        handler = new Handler();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AcelerometroWakeLock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAcelerometro();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pauseAcelerometro();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable salvarDadosRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAcelerometroAtivo) {
                databaseHelper.insertData(xAxis, yAxis, zAxis);
            }
            handler.postDelayed(this, INTERVALO_SALVAR_DADOS);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            xAxis = values[0];
            yAxis = values[1];
            zAxis = values[2];
            magnitude = Math.sqrt(xAxis * xAxis + yAxis * yAxis + zAxis * zAxis);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void startAcelerometro() {
        isAcelerometroAtivo = true;
        primeiraContagem = System.currentTimeMillis();
        handler.postDelayed(salvarDadosRunnable, INTERVALO_SALVAR_DADOS);

        // Registrar o sensor para começar a receber atualizações
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        // Adquirir o WakeLock
        wakeLock.acquire();
    }

    private void pauseAcelerometro() {
        isAcelerometroAtivo = false;
        handler.removeCallbacks(salvarDadosRunnable);

        // Parar de receber atualizações do sensor
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            //calcularMedia();
            //mensurarNivel();
            //calcularTempoTotal();
            //salvarAtividade();
        }

        // Liberar o WakeLock
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}

// Restante do código...