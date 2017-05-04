package com.hengchongkeji.constantcharge.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by henry on 2015/1/30. 捕获为未知异常信息，在程序崩溃前记录信息到sd卡，同时友好提示用户
 */
public class MyAppExceptions extends Exception implements
        Thread.UncaughtExceptionHandler {
    private static final long serialVersionUID = -6262909398048670705L;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private String message;

    private MyAppExceptions() {
        super();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public MyAppExceptions(String message, Exception excp) {
        super(message, excp);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取APP异常崩溃处理对象
     * 
     * @return
     */
    public static MyAppExceptions getAppExceptionHandler() {
        return new MyAppExceptions();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // if (!handleException(ex) && mDefaultHandler != null) {
        // message = ex.toString();
        // mDefaultHandler.uncaughtException(thread, ex);
        // }
        if (mDefaultHandler != null) {
            //2015年8月7日 更新，可以把错误的行数打印出来。 ---by L、
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            CatchErrorLog.getInstance().saveErrorLog(
                    "Application caught final exception", result);
            // message = ex.toString();
            // message = ex.getMessage();
            // CatchErrorLog.getInstance().saveErrorLog("Application caught final exception",
            // message);
            mDefaultHandler.uncaughtException(thread, ex);// 不caught让他崩溃
        }
    }

//    private boolean handleException(Throwable ex) {
//        final Activity activity = AppManager.getAppManager().currentActivity();
//
//        if (activity == null) {
//            return false;
//        }
//
//        new Thread() {
//            @Override
//            public void run() {
//                CatchErrorLog.getInstance().saveErrorLog(
//                        activity.getClass().getName(), message);
//                Looper.prepare();
//                Toast.makeText(activity, "程序要崩了", Toast.LENGTH_SHORT).show();
//                new AlertDialog.Builder(activity)
//                        .setTitle("提示")
//                        .setCancelable(false)
//                        .setMessage("亲，程序马上崩溃了...")
//                        .setNeutralButton("没关系",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                            int which) {
//                                        AppManager.getAppManager().exitApp(
//                                                activity);
//                                    }
//                                }).create().show();
//                Looper.loop();
//            }
//        }.start();
//
//        return true;
//    }
}
