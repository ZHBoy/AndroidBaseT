package com.ychd.ycwwz.base_library.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author wtl
 * @date 2019-12-19.
 * description  修改状态栏文字颜色
 */
public class StatusTextUtil {

    /**
     * 设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar(Activity pActivity, boolean pIsDark) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (setModeStatusBar_MIUI(pActivity, pIsDark) || setModeStatusBar_Flyme(pActivity, pIsDark)) {
                    return true;
                } else {
                    Window window = pActivity.getWindow();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.TRANSPARENT);
                        window.setNavigationBarColor(Color.WHITE);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        View decorView = window.getDecorView();
                        if (decorView != null) {
                            int vis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//拓展布局到导航栏后面
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏，用户点击屏幕会显示导航栏
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//拓展布局到状态栏后面
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN//隐藏状态栏
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE;

                            if (pIsDark) {
                                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            } else {
                                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            }
                            decorView.setSystemUiVisibility(vis);
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * (MIUI)设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar_MIUI(Activity pActivity, boolean pIsDark) {
        boolean result = false;
        Window window = pActivity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, (pIsDark ? darkModeFlag : 0), darkModeFlag);
                result = true;
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pActivity.getWindow().getDecorView().setSystemUiVisibility(
                            pIsDark ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_VISIBLE);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }


    /**
     * (魅族)设置改变状态栏模式 - 深浅色
     *
     * @param pActivity
     * @param pIsDark
     */
    public static boolean setModeStatusBar_Flyme(Activity pActivity, boolean pIsDark) {
        boolean result = false;
        Window window = pActivity.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (pIsDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }




}
