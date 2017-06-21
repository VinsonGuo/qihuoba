package com.yjjr.yjfutures.utils;

import android.content.Context;


import com.yjjr.yjfutures.contants.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    public void uncaughtException(Thread thread, Throwable ex) {
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatTime = sdf.format(date);
        String path = Config.getLogDir("crash_" + formatTime + ".log");
        File crashFile = new File(path);

        try {
            crashFile.createNewFile();
            FileOutputStream e = new FileOutputStream(crashFile);
            PrintStream printStream = new PrintStream(e);
            ex.printStackTrace(printStream);
            printStream.close();
        } catch (FileNotFoundException var12) {
            var12.printStackTrace();
        } catch (IOException var13) {
            var13.printStackTrace();
        } catch (SecurityException var14) {
            var14.printStackTrace();
        }

        LogUtils.e(ex);
        System.exit(2);
    }
}
