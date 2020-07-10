package com.zhboy.ycwwz.base_library.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :软键盘封装
 **/
public class SoftInputUtil {

    /**
     * 显示软键盘
     *
     * @param context
     */
    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 显示软键盘
        if (imm != null)
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
     * 显示软键盘
     *
     * @param context
     */
//    public static void showSoftInput(Context context, View view) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 显示软键盘
//        if (imm != null)
//            imm.showSoftInput(view, 0);
//    }

    /*
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
//    public static void hideSoftInput(Context context, View view) {
//        InputMethodManager immHide = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); // 隐藏软键盘
//        if (immHide != null)
//            immHide.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    /**
     * 判断在什么情况下隐藏软键盘，点击EditText视图显示软键盘
     *
     * @param view  Incident event
     * @param event
     * @return
     */
    public static boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view instanceof EditText) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left
                    + view.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // if the focus is EditText,ignore it;
        return false;
    }

    /**
     * 获取剪切板的内容
     *
     * @param context
     * @return
     */
    public static String getClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        String content = item.getText().toString();
        return content;
    }
}
