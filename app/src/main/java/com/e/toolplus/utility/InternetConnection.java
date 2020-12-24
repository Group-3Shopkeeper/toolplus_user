package com.e.toolplus.utility;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.databinding.CustomAlertDialogBinding;

public class InternetConnection extends BroadcastReceiver {
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnect = manager.getNetworkInfo(manager.TYPE_WIFI);
        NetworkInfo mobileConnect = manager.getNetworkInfo(manager.TYPE_MOBILE);

        if ((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {
            return true;
        } else
            return false;
    }


    @Override
    public void onReceive(final Context context, Intent intent) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnect = manager.getNetworkInfo(manager.TYPE_WIFI);
        NetworkInfo mobileConnect = manager.getNetworkInfo(manager.TYPE_MOBILE);

        if ((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {
            if (alertDialog != null){
                alertDialog.dismiss();
            }
        } else {
            builder = new AlertDialog.Builder(context);

            CustomAlertDialogBinding binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(context));
            builder.setView(binding.getRoot());

            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            binding.negative.setVisibility(View.INVISIBLE);

            binding.tvConfirmation.setText("Internet Connection Alert");

            binding.tvMessage.setText("Please Connect to Internet");
            binding.tvPositive.setText("Connect");

            binding.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                }
            });

            alertDialog.show();
        }

    }

}
