package com.example.smartstudent.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.view.View;

//说明
//KeyboardUtils.showKeyboard(this, editText);  // 传入当前上下文和要聚焦的 EditText
//KeyboardUtils.hideKeyboard(this, currentFocusView);  // 传入当前焦点的视图（如 EditText）
//boolean isKeyboardVisible = KeyboardUtils.isKeyboardVisible(this);



public class KeyboardUtils {

    /**
     * 显示软键盘
     * @param context 上下文
     * @param editText 需要获取焦点的 EditText
     */
    public static void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            editText.requestFocus();  // 确保 EditText 获得焦点
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT); // 显示软键盘
        }
    }

    /**
     * 隐藏软键盘
     * @param context 上下文
     * @param view 当前获得焦点的 View
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  // 隐藏软键盘
        }
    }

    /**
     * 检查软键盘是否显示
     * @param context 上下文
     * @return true：软键盘显示，false：软键盘未显示
     */
    public static boolean isKeyboardVisible(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.isActive();
    }
}
