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
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.R;
import com.e.toolplus.databinding.CustomAlertDialogBinding;
import com.e.toolplus.databinding.OfflineBinding;

import java.net.URISyntaxException;

public class InternetConnection extends BroadcastReceiver {

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
        NetworkInfo wifiConnect = manager.getActiveNetworkInfo();
        NetworkInfo mobileConnect = manager.getActiveNetworkInfo();

        if ((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())) {

        } else {
            Intent intent1 = new Intent(context,Offline.class);
            context.startActivity(intent1);
        }

    }

}
