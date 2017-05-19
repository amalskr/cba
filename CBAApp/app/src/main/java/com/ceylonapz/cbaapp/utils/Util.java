package com.ceylonapz.cbaapp.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by amalskr on 2017-05-19.
 */
public class Util {

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
