package com.animation.attributeanimation;

import com.example.androidfirsttest.R;
import component.data.exchange.fragment.TaskFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月22日 下午2:50:59
 * @version
 * @since
 */
public class PropertyAnimationActivity extends FragmentActivity
{

    private RelativeLayout rl;
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
//        rl = new RelativeLayout(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
//                LayoutParams.MATCH_PARENT);
//        rl.setLayoutParams(params);
        setContentView(R.layout.property_animation_activity_layout);
        //BasePropertyAnimationFragment fragment = BasePropertyAnimationFragment.newInstance();
        TaskFragment fragment = HighPropertyAnimationFragment.newInstance();
        fragment.showFragment(getSupportFragmentManager(), R.id.rootContainer, "PropertyAnimationFragment");
        
    }
}
