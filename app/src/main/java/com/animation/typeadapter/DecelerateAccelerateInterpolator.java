package com.animation.typeadapter;

import android.animation.TimeInterpolator;

/**
 * 
 * 先减速再匀速最后加速的动画补间器。
 * @author sWX284798
 * @date 2015年12月26日 下午3:28:33
 * @version
 * @since
 */
public class DecelerateAccelerateInterpolator implements TimeInterpolator
{

    /**
     * {@inheritDoc}
     */
    @Override
    public float getInterpolation(float input)
    {
        float result;
        if(input < 0.5f)
        {
            result = (float)(Math.sin(Math.PI * input)) / 2; 
        }
        else
        {
            result = (float)(2 - Math.sin(Math.PI * input)) / 2;
        }
        return result;
    }

}
