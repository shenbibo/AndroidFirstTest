package com.animation.attributeanimation;

import com.animation.bean.Point;
import com.animation.typeadapter.ColorEvaluator;
import com.animation.typeadapter.DecelerateAccelerateInterpolator;
import com.animation.typeadapter.PointEvaluator;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月24日 上午10:35:37
 * @version
 * @since
 */
public class MyView extends View
{

    private Paint mPaint;
    
    private String color;
    
    private Point mPoint;
    
    private static final float RADIOUS = 50f; 
    
    /**
     * @param context
     */
    public MyView(Context context)
    {
        super(context);
        
    }
    
    public MyView(Context context, AttributeSet set)
    {
        this(context, set, 0);
    }
    
    public MyView(Context context, AttributeSet set, int defStyleAttr)
    {
        super(context, set, defStyleAttr);
        init();
    }
    
    private void init()
    {
        //设置抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setARGB(180, 32, 46, 78);
        //mPaint.setColor(0XAC345678);
        mPaint.setColor(Color.BLUE);
    }
    
    public void setColor(String color)
    {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        //刷新界面会导致onDraw方法被调用
        invalidate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        //do nothing
        super.onDraw(canvas);
        if(mPoint == null)
        {
//            mPoint = new Point(getWidth()/2, getHeight()/2);
//            drawCircle(canvas);
            startPropertyAnimation();
        }
        else
        {
            drawCircle(canvas);
        }
        
    }
    
    private void drawCircle(Canvas canvas)
    {
        canvas.drawCircle(mPoint.getX(), mPoint.getY(), RADIOUS, mPaint);
    }
    
    private void startPropertyAnimation()
    {
        
        //首先进行平移动画
        Point centerPoint = new Point(getWidth()/2, getHeight()/2);
        Point leftStartPos = new Point(RADIOUS, getHeight()/2);
        ValueAnimator startAnimation = ValueAnimator.ofObject(new PointEvaluator(), centerPoint, leftStartPos);
        startAnimation.addUpdateListener(new AnimatorUpdateListener()
        {
            
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        startAnimation.addListener(new AnimatorListener()
        {
            
            @Override
            public void onAnimationStart(Animator animation)
            {
            }
            
            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
            
            @Override
            public void onAnimationEnd(Animator animation)
            {
                setCircleAnimation();
            }
            
            @Override
            public void onAnimationCancel(Animator animation)
            {
            }
        });
        startAnimation.setDuration(3000);
        startAnimation.start();
        

    }
    
    
    private void setCircleAnimation()
    {
        Point centerPoint = new Point(getWidth()/2, getHeight()/2);
        Point leftStartPos = new Point(RADIOUS, getHeight()/2);
        
//        Point firstPoint = new Point(RADIOUS, RADIOUS);
//        Point secondPoint = new Point(getWidth() - RADIOUS, getHeight() - RADIOUS);
        
        ValueAnimator valueAnimation = ValueAnimator.ofObject(new PointEvaluator(true, centerPoint, RADIOUS), 
                leftStartPos, leftStartPos);
        valueAnimation.addUpdateListener(new AnimatorUpdateListener()
        {
            
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        
//        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(this, "color", 
//                new ColorEvaluator(), "#00FFFFFF", "#FF000000", "#55555555");
        
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(this, "color", 
                new ColorEvaluator(), "#00FFeedd", "#00123456", "#00f4321e");
        
        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimation).with(objectAnimator);
        set.setInterpolator(new DecelerateAccelerateInterpolator());
        set.setDuration(15000);
        set.start();
    }

}
