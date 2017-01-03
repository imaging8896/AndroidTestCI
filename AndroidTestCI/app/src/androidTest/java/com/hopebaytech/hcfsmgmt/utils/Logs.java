package com.hopebaytech.hcfsmgmt.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import hcfs.test.config.Path;

/**
 * @author Aaron
 *         Created by Aaron on 2016/5/24.
 */
public class Logs {

    public static final String TAG = "API";
    public static final int LOG_LEVEL = Log.DEBUG;
    public static final File logFile = new File(Path.SDCARD_PATH + File.separator + "API_log");

    static {
        try {
            if(logFile.exists())
                logFile.delete();
            logFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void i(String className, String funcName, String logMsg) {
        log(Log.INFO, className, funcName, logMsg);
    }

    public static void w(String className, String funcName, String logMsg) {
        log(Log.WARN, className, funcName, logMsg);
    }

    public static void e(String className, String funcName, String logMsg) {
        log(Log.ERROR, className, funcName, logMsg);
    }

    public static void d(String className, String funcName, String logMsg) {
        log(Log.DEBUG, className, funcName, logMsg);
    }

    public static void i(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.INFO, className, innerClassName, funcName, logMsg);
    }

    public static void w(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.WARN, className, innerClassName, funcName, logMsg);
    }

    public static void e(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.ERROR, className, innerClassName, funcName, logMsg);
    }

    public static void d(String className, String innerClassName, String funcName, String logMsg) {
        log(Log.DEBUG, className, innerClassName, funcName, logMsg);
    }

    private static void log(int logLevel, String className, String funcName, String logMsg) {
        if (logLevel >= LOG_LEVEL) {
            if (logMsg == null) {
                logMsg = "";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.TAIWAN);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            writeFileLine(dateFormat.format(timestamp) + " : " + className + "(" + funcName + "): " + logMsg);
            if (logLevel == Log.DEBUG) {
                Log.d(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.INFO) {
                Log.i(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.WARN) {
                Log.w(TAG, className + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.ERROR) {
                Log.e(TAG, className + "(" + funcName + "): " + logMsg);
            }
        }
    }

    private static void log(int logLevel, String className, String innerClassName, String funcName, String logMsg) {
        if (logLevel >= LOG_LEVEL) {
            if (logMsg == null) {
                logMsg = "";
            }
            if (logLevel == Log.DEBUG) {
                Log.d(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.INFO) {
                Log.i(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.WARN) {
                Log.w(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            } else if (logLevel == Log.ERROR) {
                Log.e(TAG, className + "->" + innerClassName + "(" + funcName + "): " + logMsg);
            }
        }
    }

    //TODO Use @BeforeClass to fast these
    private static void writeFileLine(String logMsg) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
            out.println(logMsg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
