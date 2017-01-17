package com.egeniq.lpsolver;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple utility class for logging into a TextView.
 * Created by Daniel Zolnai on 2017-01-07.
 */
public class Log {

    // It's not nice to put View classes in static fields, but since this is a simple
    // demonstration app, we can overlook that.
    private static TextView _textView;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss.SSS", Locale.US);


    public static void setLogTextView(TextView textView) {
        _textView = textView;
    }

    public static void log(String message) {
        _textView.append("\n" + message);
    }

    public static void logWithTimeStamp(String message) {
        log(DATE_FORMAT.format(new Date()) + " - " + message);
    }

    public static void clear() {
        _textView.setText("");
    }
}
