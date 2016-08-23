package com.widget.test.toolbar;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * @author sWX284798
 *
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter
{
    private final String TAG = "MyFragmentAdapter";
    
    private List<Fragment> myList;

    /**
     * @param fm
     * @param list 如果为null，则结果是未定义的。
     */
    public MyFragmentAdapter(FragmentManager fm, List<Fragment> list)
    {
        // TODO Auto-generated constructor stub
        super(fm);
        myList = list;
    }

    /**
     * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int arg0)
    {
        // TODO Auto-generated method stub
        Log.d(TAG, "perform getItem");
        return myList.get(arg0);
    }

    /**
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return myList.size();
    }

}
