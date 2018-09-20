package com.example.movies.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Rania on 12/15/2016.
 */

public abstract class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * Returns whether there is a valid internet connection or no connection
     *
     * @param context
     * @return true if there is internet connection false otherwise
     */
    public static boolean isInternetConnectionExist(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * This Method is responsible for hiding the Soft keyboard on Demand.
     *
     * @param activity    The Current Running activity.
     * @param focusLayout If You need to change the focus to any other view after hiding the keyboard.
     */
    public static void hideSoftKeyboard(Activity activity, View focusLayout) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                if (focusLayout != null) {
                    focusLayout.requestFocus();
                }
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
