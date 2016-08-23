/*
 * 文 件 名:  CovertHelper.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月6日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package common.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.androidfirsttest.MyApplication;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sWX284798
 * @version  [版本号, 2016年1月6日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CovertHelper
{

    public static int dp2Pix(float dp)
    {
        Context context = MyApplication.getInstance();
        return (int)(dp * getDisplayMetrics(context).density);
    }
    
    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        if(context != null)
        {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }
    
    /**
     * 获取当前屏幕的宽度
     * */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.widthPixels;
    }
}
