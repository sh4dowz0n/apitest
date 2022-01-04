package com.example.redelcom_test.helper;

import android.app.AlertDialog;
import android.content.Context;

import com.example.redelcom_test.R;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.io.IOException;

public class AppHelper {
    public boolean isInternetAvalaible() {
        try {
            String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String capitalizeFirst(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public void notNetworkDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.not_network_alert_title);
        builder.setMessage(R.string.not_network_alert_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.not_network_alert_yes, (dialog, which) -> dialog.cancel());
        builder.setNegativeButton(R.string.not_network_alert_no, (dialog, which) -> ProcessPhoenix.triggerRebirth(context));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
