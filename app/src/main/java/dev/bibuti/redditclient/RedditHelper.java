package dev.bibuti.redditclient;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RedditHelper {

    private static final String TAG = "RedditHelper";

    public static void log(String msg) {
        Log.d(TAG, "log() called with: msg = [" + msg + "]");
    }

    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String TAG, Object msg) {
        Log.d(TAG, msg.toString());
    }

    public static void d(String TAG, Objects msg) {
        Log.d(TAG, msg.toString());
    }

    public static String numberWithSuffix(Long num) {

        if (num == null) {
            return "0";
        }

        if (num < 1000) {
            return num.toString();
        } else {
            int exp = (int) (Math.log(num) / Math.log(1000));
            DecimalFormat format = new DecimalFormat("0.#");
            String value = format.format(num / Math.pow(1000, exp));
            return String.format(Locale.getDefault(), "%s %c", value, "kMGTPE".charAt(exp - 1));
        }
    }

    public static String convertUnixToDate(Long unixDate) {
        Date date = new Date(unixDate * 1000);
        return new SimpleDateFormat("MMM dd, yyyy hh:mma", Locale.getDefault()).format(date);
    }

}
