package com.e.toolplus.utility;

import android.content.IntentFilter;

public class InternetIntentFilter {

    public static IntentFilter getIntentFilter(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        return intentFilter;
    }
}
