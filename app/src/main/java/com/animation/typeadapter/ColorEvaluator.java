package com.animation.typeadapter;

import java.util.List;

import common.tools.StringUtlis;

import android.animation.TypeEvaluator;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月23日 下午3:05:37
 * @version
 * @since
 */
public class ColorEvaluator implements TypeEvaluator<String>
{

    private static final String FLG_COLOR = "#";
    
//    int alphaStart;
//    int alphaEnd;
//    int redStart;
//    int redEnd;
//    int greenStart;
//    int greenEnd;
//    int blueStart;
//    int blueEnd;
//    int alphaOffset;
//    int redOffset;
//    int greenOffset;
//    int blueOffset;
//    
//    public ColorEvaluator(String startValue, String endValue)
//    {
//        //获取颜色值ARGB颜色值
//        alphaStart = Integer.parseInt(startValue.substring(1, 3), 16);
//        alphaEnd = Integer.parseInt(endValue.substring(1, 3), 16);
//        redStart = Integer.parseInt(startValue.substring(3, 5), 16);
//        redEnd = Integer.parseInt(endValue.substring(3, 5), 16);
//        greenStart = Integer.parseInt(startValue.substring(5, 7), 16);
//        greenEnd = Integer.parseInt(endValue.substring(5, 7), 16);
//        blueStart = Integer.parseInt(startValue.substring(7), 16);
//        blueEnd = Integer.parseInt(endValue.substring(7), 16);
//        //获取偏移量
//        alphaOffset = Math.abs(alphaStart - alphaEnd);
//        redOffset = Math.abs(redStart - redEnd);
//        greenOffset = Math.abs(greenStart - greenEnd);
//        blueOffset = Math.abs(blueStart - blueEnd);
//    }
    
    /**
     * {@inheritDoc}
     * 颜色的样式必须为#0123456
     */
    @Override
    public String evaluate(float fraction, String startValue, String endValue)
    {
        
      
        
        //获取颜色值ARGB颜色值
//        int alphaStart = Integer.parseInt(startValue.substring(1, 3), 16);
//        int alphaEnd = Integer.parseInt(endValue.substring(1, 3), 16);
        int redStart = Integer.parseInt(startValue.substring(3, 5), 16);
        int redEnd = Integer.parseInt(endValue.substring(3, 5), 16);
        int greenStart = Integer.parseInt(startValue.substring(5, 7), 16);
        int greenEnd = Integer.parseInt(endValue.substring(5, 7), 16);
        int blueStart = Integer.parseInt(startValue.substring(7), 16);
        int blueEnd = Integer.parseInt(endValue.substring(7), 16);
        //获取偏移量
        //int alphaOffset = Math.abs(alphaStart - alphaEnd);
        int redOffset = Math.abs(redStart - redEnd);
        int greenOffset = Math.abs(greenStart - greenEnd);
        int blueOffset = Math.abs(blueStart - blueEnd);
        
        //获取转换过程中的当前颜色值
        //int curAlpha = getCurColor(fraction, alphaStart, alphaEnd, alphaOffset);
        int curRed = getCurColor(fraction, redStart, redEnd, redOffset);
        int curBlue = getCurColor(fraction, blueStart, blueEnd, blueOffset);
        int curGreen = getCurColor(fraction, greenStart, greenEnd, greenOffset);
        //转换成正常的颜色表示值
        
        return FLG_COLOR /*+ StringUtlis.getHexString(curAlpha)*/ + StringUtlis.getHexString(curRed)
                + StringUtlis.getHexString(curGreen) + StringUtlis.getHexString(curBlue);
    }
    
    
    /**
     * offsetValue = math.abs(endColor - startColor);
     * */
    private int getCurColor(float fraction, int startColor, int endColor, int offsetValue)
    {
        int curColor = 0;
        if(startColor > endColor)
        {
            curColor = (int) (startColor - fraction * offsetValue);
            if(curColor < endColor)
            {
                curColor = endColor;
            }
        }
        else
        {
            curColor = (int) (startColor + fraction * offsetValue);
            if(curColor > endColor)
            {
                curColor = endColor;
            }
        }
        return curColor;
    }

}
