package handler;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


/**
 * [function]
 * [detail]
 * Created by Sky on 2017/3/16.
 */

public class UiHelper {
    /**
     * EditText获取焦点则将光标设置在最末尾的位置
     */
    public static void setEditTextSelectionToEnd(EditText editText) {
        if (editText != null && editText.hasFocus()) {
            editText.setSelection(editText.getText().toString().length());
        }
    }

    /**
     * activity是否是活动状态
     */
    public static boolean isActivityActive(Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    /** view 是否可见 */
    public static boolean viewVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isShowing(Dialog dialog) {
        return dialog != null && dialog.isShowing();
    }

    /** 将子view从其父View中移除 */
    public static void removeViewFromParent(View view) {
        if (view == null) {
            return;
        }

        ViewGroup curViewGroup = (ViewGroup) view.getParent();
        if (curViewGroup != null) {
            curViewGroup.removeView(view);
        }
    }

    /**
     * 获取view在屏幕上的位置
     */
    public static int[] getViewPosOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static void setWindowFullScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setWindowNormal(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
