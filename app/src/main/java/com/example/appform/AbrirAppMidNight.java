package com.example.appform;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AbrirAppMidNight extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent openAppIntent = new Intent(context, ContadorPassosActivity.class);
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openAppIntent);
    }

}
