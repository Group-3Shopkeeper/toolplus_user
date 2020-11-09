package com.e.toolplus.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
    public static boolean isConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConnect = manager.getNetworkInfo(manager.TYPE_WIFI);
        NetworkInfo mobileConnect = manager.getNetworkInfo(manager.TYPE_MOBILE);

        if((wifiConnect != null && wifiConnect.isConnected()) || (mobileConnect != null && mobileConnect.isConnected())){
            return true;
        }
        else
            return false;
    }
}
