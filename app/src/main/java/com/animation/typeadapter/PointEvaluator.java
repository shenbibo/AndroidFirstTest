package com.animation.typeadapter;

import com.animation.bean.Point;

import android.animation.TypeEvaluator;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月23日 下午3:03:33
 * @version
 * @since
 */
public class PointEvaluator implements TypeEvaluator<Point>
{

    private boolean isCircleAnimation = false;
    
    private Point centerPoint;
    
    private float radius;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue)
    {
        if(!isCircleAnimation)
        {
            float curX = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
            float curY = startValue.getY() + fraction * (endValue.getY() - startValue.getY());        
            return new Point(curX, curY);
        }
        else
        {
            float centerX = centerPoint.getX();
            float centerY = centerPoint.getY();
            float x = (float) (centerX - (centerX - radius) * Math.cos(Math.PI * 2 * fraction));
            float y = (float) (centerY - (centerX - radius) * Math.sin(Math.PI * 2 * fraction));
            return new Point(x, y);
        }
        
    }

    public PointEvaluator()
    {
        
    }
    
    public PointEvaluator(boolean isCircleAnimation, Point point, float raduis)
    {
        this.isCircleAnimation = isCircleAnimation;
        centerPoint = point;
        this.radius = raduis;
    }

}
