package com.example.k_talk;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissaoCamera {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listadePermissoes = new ArrayList<>();


            for(String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao){
                    listadePermissoes.add(permissao);
                }
            }

            if (listadePermissoes.isEmpty()){
                return true;
            }

            String[] novasPermissoes = new String[listadePermissoes.size()];
            listadePermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }
        return true;
    }
}
