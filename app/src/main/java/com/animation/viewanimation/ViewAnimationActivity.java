package com.animation.viewanimation;

import com.example.androidfirsttest.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月21日 下午2:34:44
 * @version
 * @since
 */
public class ViewAnimationActivity extends Activity implements OnClickListener
{
    private static final String TAG = "ViewAnimationActivity";

    private Button translateBtn;
    
    private Button alphaBtn;
    
    private Button scaleBtn;
    
    private Button rotateBtn;
    
    private Button setBtn;
    
    private Button frameBtn;
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_animation_activity_layout);
        //TranslateAnimation translateAnimation = new 
        initView();
    }
    
    private void initView()
    {
        translateBtn = (Button) findViewById(R.id.translateButton);
        translateBtn.setOnClickListener(this);
        scaleBtn = (Button) findViewById(R.id.scaleBtn);
        scaleBtn.setOnClickListener(this);
        rotateBtn =(Button) findViewById(R.id.rotateBtn);
        rotateBtn.setOnClickListener(this);
        alphaBtn = (Button) findViewById(R.id.alphaBtn);
        alphaBtn.setOnClickListener(this);
        setBtn = (Button) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(this);
        frameBtn = (Button) findViewById(R.id.frameBtn);
        frameBtn.setOnClickListener(this);
        //frameBtn.setPivotX(pivotX);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.translateButton:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation_test);
                translateBtn.startAnimation(animation);               
                break;

            case R.id.scaleBtn:
                Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scale_animation_test);
                scaleBtn.startAnimation(animation1);               
                break;
                
            case R.id.alphaBtn:
                Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_test);
                alphaBtn.startAnimation(animation2);               
                break;
                
            case R.id.rotateBtn:
                Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_test);
                rotateBtn.startAnimation(animation3);               
                break;
                
            case R.id.setBtn:
                Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.set_animation_test);
                animation4.setAnimationListener(new AnimationListener()
                {
                    
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        Log.d(TAG, "animation start");
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                        Log.d(TAG, "animation repeat");
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        Log.d(TAG, "animation end");
                    }
                });
                setBtn.startAnimation(animation4);               
                break;
                
            case R.id.frameBtn:
                AnimationDrawable drawable = (AnimationDrawable) frameBtn.getBackground();
                
                drawable.start();
                
            default:
                break;
        }
    }
}
