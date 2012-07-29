/***************************************************************************************************
 * Copyright 2011 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.daily;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Base class for all activities, contains common shared code.
 * 
 */
public class AbstractReadingActivity extends Activity {
    
    /**
     * Checks if is online.
     *
     * @return true, if is online
     */
    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
