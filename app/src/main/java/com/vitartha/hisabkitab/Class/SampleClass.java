package com.vitartha.hisabkitab.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

public class SampleClass extends AppCompatActivity {

    public void showDialogBox(int Titlecode, String ERROR_MESSAGE, final Runnable runnable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (Titlecode == 300) {
            alertDialogBuilder.setTitle("Alert");
        } else alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage(ERROR_MESSAGE);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                runnable.run();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
