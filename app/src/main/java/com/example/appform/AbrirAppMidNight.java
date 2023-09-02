package com.example.appform;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.appform.util.PassoUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AbrirAppMidNight extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent openAppIntent = new Intent(context, ContadorPassosActivity.class);
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openAppIntent);
        MidnightReset();


    }

    public static void MidnightReset() {
        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY, 22);
        midnight.set(Calendar.MINUTE, 32);
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
                PassoUtil.saveData(0, new ContadorPassosActivity().context);
            }
        }, timeUntilMidnight, 24 * 60 * 60 * 1000); // 24 horas em milissegundos

    }

}
