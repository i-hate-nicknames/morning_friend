package com.domain.nvm.morningfriend;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String FILE_NAME = "moring-friend-alarms.log";
    private static final String TAG = "MorningFriendLogger";

    public static void write(Context context, String msg) {
        write(context, System.currentTimeMillis(), msg);
    }

    public static void write(Context context, long timeMillis, String msg) {
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm:ss, dd.MM", java.util.Locale.getDefault());
        String timedMsg = format.format(new Date(timeMillis)) + ": " + msg;
        try {
            OutputStreamWriter outputStreamWriter
                    = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_APPEND));

            outputStreamWriter.write(timedMsg);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    public static String read(Context context) {
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = context.openFileInput(FILE_NAME);
            if (is != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
                is.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        return result.toString();
    }

    public static void clear(Context context) {
        context.deleteFile(FILE_NAME);
    }
}
