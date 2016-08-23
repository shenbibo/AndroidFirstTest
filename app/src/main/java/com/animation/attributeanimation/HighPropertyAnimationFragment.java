package com.animation.attributeanimation;

import com.example.androidfirsttest.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import component.data.exchange.fragment.TaskFragment;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月23日 下午2:59:40
 * @version
 * @since
 */
public class HighPropertyAnimationFragment extends TaskFragment
{

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {        
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.property_animation_layout, container, false);
        return rootView;
    }
    
    public static HighPropertyAnimationFragment newInstance()
    {
        return new HighPropertyAnimationFragment();
    }
}
