package com.animation.bean;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月23日 下午3:00:37
 * @version
 * @since
 */
public class Point
{

    float x; 
    float y;
    
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setX(float x)
    {
        this.x = x;
    }
    
    public void setY(float y)
    {
        this.y = y;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
}
