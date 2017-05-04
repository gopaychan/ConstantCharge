package com.hengchongkeji.constantcharge.common;

import com.hengchongkeji.constantcharge.BuildConfig;
import com.hengchongkeji.constantcharge.ChargeApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henry on 2015/1/29. 根据MyApplication的是否调试状态来决定是否记录捕获到的错误日志 单例模式启动
 */
public class CatchErrorLog {
    // private boolean isDebug = true;
    private static CatchErrorLog mInstance;
    // private static final String INTERFACE_LOG_FILE_PATH =
    // SDCardUtils.getFileFolder("catchlog");//文件保存路径
    private static final String INTERFACE_LOG_FILE_PATH = FileHelper
            .getFileSavePath(ChargeApplication.getApplicationComponent().context(), "catchlog");
    private static final int LOG_EXITES_DAY = 7;// 文件保存天数
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

    private CatchErrorLog() {
    }

    public static CatchErrorLog getInstance() {
        if (mInstance == null) {
            mInstance = new CatchErrorLog();
        }
        return mInstance;
    }

    /**
     * 保存错误日志
     * 
     * @param from
     *            来自哪里的错误日志
     * @param e
     *            异常
     */
    public void saveErrorLog(String from, Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
            writeLogtoFile(from + "\n" + e.getMessage());
        }
    }

    public void saveErrorLog(String from, String info) {
        if (BuildConfig.DEBUG) {
            writeLogtoFile(from + "\n" + info);
        }
    }

    public void saveErrorLog(String from, String info, Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
            writeLogtoFile(from + "\n" + info);
            writeLogtoFile(from + "\n" + e.getMessage());
        }
    }

    /**
     * 打开日志文件并写入日志
     * 
     * @return
     **/
    private void writeLogtoFile(String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = myLogSdf.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "\n"
                + "*************************" + "\n" + text;
        File path = new File(INTERFACE_LOG_FILE_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(INTERFACE_LOG_FILE_PATH, needWriteFiel + ".txt");
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeStringToLocate(String text) {
        try {
            File path = new File(INTERFACE_LOG_FILE_PATH);
            if (!path.exists()) {
                path.mkdirs();
            }
            FileWriter writer = new FileWriter(new File(path, "writeStr.txt"),
                    true);
            BufferedWriter bufWriter = new BufferedWriter(writer);
            bufWriter.write(text);
            bufWriter.newLine();
            bufWriter.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
