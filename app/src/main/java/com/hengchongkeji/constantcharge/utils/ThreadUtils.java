package com.hengchongkeji.constantcharge.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by gopayChan on 2017/4/16.
 */

public class ThreadUtils {
    private ThreadUtils() {

    }

    public static void runOnMainThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }
}
