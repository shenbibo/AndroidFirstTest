package com.animation.attributeanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidfirsttest.R;
import component.data.exchange.fragment.TaskFragment;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月22日 下午2:52:15
 * @version
 * @since
 */
public class BasePropertyAnimationFragment extends TaskFragment implements
        OnClickListener
{
    
    private static final String TAG = "BasePropertyAnimationFragment";

    private Button proScaleBtn;

    private Button proAlphaBtn;

    private Button proRotateBtn;

    private Button proTranslateBtn;

    private Button proSetBtn;

    private TextView textView;
    
    
    public static BasePropertyAnimationFragment newInstance()
    {
        return new BasePropertyAnimationFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(
                R.layout.base_property_animation_fragment_layout, container,
                false);
        initViews(rootView);
        return rootView;

    }

    private void initViews(View rootView)
    {
        proAlphaBtn = (Button) rootView.findViewById(R.id.propertyAlphaBtn);
        proAlphaBtn.setOnClickListener(this);
        proRotateBtn = (Button) rootView.findViewById(R.id.propertyRotateBtn);
        proRotateBtn.setOnClickListener(this);
        proScaleBtn = (Button) rootView.findViewById(R.id.propertyScaleBtn);
        proScaleBtn.setOnClickListener(this);
        proSetBtn = (Button) rootView.findViewById(R.id.propertySetBtn);
        proSetBtn.setOnClickListener(this);
        proTranslateBtn = (Button) rootView
                .findViewById(R.id.propertyTranslateBtn);
        proTranslateBtn.setOnClickListener(this);
        textView = (TextView) rootView.findViewById(R.id.testText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.propertyAlphaBtn:
                controlAlphaAnimation();
                break;
            case R.id.propertyRotateBtn:
                controlRotateAnimation();
                break;
            case R.id.propertyScaleBtn:
                controlScaleYAnimation();
                break;
            case R.id.propertySetBtn:
                controlSetAnimation();
                break;
            case R.id.propertyTranslateBtn:
                controlTransalteXAnimation();
                break;
            default:
                break;
        }
    }

    private void controlAlphaAnimation()
    {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView,
                "alpha", 1f, 0f, 0.8f, 0.2f, 1f);
        alphaAnimator.setDuration(5000);
        alphaAnimator.start();
    }

    private void controlRotateAnimation()
    {
        //旋转动画中注意传递进去的值都被认为是绝对值，而不是相对值，如-90f表示的是先逆时钟转回到起始位置再逆时钟90度，而不是在270的基础上逆时钟90度，变成180度。
        //如果是同向的旋转则是取的相对的值，若从180到540，实际旋转的度数为540-180
        //实际旋转的度数=目标旋转度数 - 上一次的旋转度数最终值   ，如果为负数则表示逆时钟旋转
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView,
                "rotation", 0f, 270f, -90f, 180f, 540f, 490f, 520f, -30f, 0f);
        //设置旋转的核心
        textView.setPivotX(0);
        textView.setPivotY(0);
        alphaAnimator.setDuration(20000);
        alphaAnimator.setInterpolator(new LinearInterpolator());
        alphaAnimator.start();
    }

    private void controlScaleYAnimation()
    {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView,
                "scaleY", 1f, 0f, 0.8f, 0.2f, 1f);
        alphaAnimator.setDuration(6000);
        alphaAnimator.start();
    }

    private void controlTransalteXAnimation()
    {
        float initPos = proTranslateBtn.getTranslationX();
        Log.d(TAG, "intPos = " + initPos);
        //平移动画中传递进去的值被认为是相对值，0表示该View的起始点，-200表示向左移动200, 200表示相对原来的位置向右移动200
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView,
                "translationX", 0, -200, 200f, -initPos);
        alphaAnimator.setDuration(8000);
        alphaAnimator.start();
        Log.d(TAG, "finishPos = " + proTranslateBtn.getTranslationX());
    }

    private void controlSetAnimation()
    {
        // 制作一个先向下竖直移动且伴随着TextView逐渐放大的过程。
        ObjectAnimator firstTranslateYAnim = ObjectAnimator.ofFloat(textView,
                "translationY", 0f, 500f);
        ObjectAnimator firstRotationAnim = ObjectAnimator.ofFloat(textView,
                "rotation", 0f, 720f);
        ObjectAnimator firstPivotXAnim = ObjectAnimator.ofFloat(textView,
                "pivotX", 0f, 100f);
        

        // ObjectAnimator firstTranslateYAnim = ObjectAnimator.ofFloat(textView,
        // "translationY", 0f, 500f);
        // ObjectAnimator firstTranslateYAnim = ObjectAnimator.ofFloat(textView,
        // "translationY", 0f, 500f);
        // ObjectAnimator firstTranslateYAnim = ObjectAnimator.ofFloat(textView,
        // "translationY", 0f, 500f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(6000);
        set.play(firstTranslateYAnim).with(firstRotationAnim).with(firstPivotXAnim);
        set.start();

    }
}
