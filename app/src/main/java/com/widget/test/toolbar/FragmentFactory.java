package com.widget.test.toolbar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.widget.test.toolbar.MyPageChangeListener.OnDataChangedListener;
import com.widget.test.toolbar.fragment.BaseFragment;
import com.widget.test.toolbar.fragment.FirstFragment;
import com.widget.test.toolbar.fragment.SecondFragment;
import com.widget.test.toolbar.fragment.ThirdFragment;

import android.support.v4.app.Fragment;

/**
 * @author sWX284798
 *
 */
public class FragmentFactory
{

    @Deprecated
    public static Fragment newInstance(Class<? extends BaseFragment> obj, OnDataChangedListener l)
    {
        if (obj == FirstFragment.class)
        {
            return new FirstFragment();
        }
        else if(obj == SecondFragment.class)
        {
            return new SecondFragment();
        }
        else
        {
            return new ThirdFragment();
        }
    }
    
    public static Fragment newInstance(Class<? extends BaseFragment> clazz)
    {
        Fragment fragment = null;
        if(clazz == null)
        {
            return null;
        }
        Constructor<? extends BaseFragment> constructor = null;
        try
        {
            //以下代码等同于调用clazz.newInstance();
            constructor = clazz.getConstructor();
            fragment = constructor.newInstance();
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fragment;
    }

}
