package com.example.appform;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GerenciadorPermissao {
    private static final int REQUEST_PERMISSION_CODE = 1;
    private Activity activity;

    public GerenciadorPermissao(Activity activity) {
        this.activity = activity;
    }

    public boolean verificarPermissao(String permissao) {
        int permissionStatus = ContextCompat.checkSelfPermission(activity, permissao);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    public void solicitarPermissao(String permissao) {
        if (!verificarPermissao(permissao)) {
            ActivityCompat.requestPermissions(activity, new String[]{permissao}, REQUEST_PERMISSION_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida
            } else {
                // A permissão foi negada, você pode solicitar novamente aqui
            }
        }
    }
}
