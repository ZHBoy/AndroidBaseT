package com.ychd.ycwwz.base_library.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ychd.ycwwz.base_library.base.AppManager;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类 来接管程序,并记录 发送错误报告..
 * 註冊方式
 * CrashHandler crashHandler = CrashHandler.getInstance();
 * 注册crashHandler
 * crashHandler.init(getApplicationContext());
 * 发送以前没发送的报告(可选)
 * crashHandler.sendPreviousReportsToServer();
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler mCrashHandler;
    /**
     * 程序的Context对象
     */
    private Context mContext;

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */

    private CrashHandler(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance(Context context) {
        if (mCrashHandler == null) {
            synchronized (CrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new CrashHandler(context);
                }
            }
        }
        return mCrashHandler;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            if (AppManager.Companion.getInstance().getStackNumber()!=1){
                //自定义处理逻辑
                Intent intent = new Intent();
                intent.setData(Uri.parse("ycwwz://homepage"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            // 如果用户没有处理 则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}