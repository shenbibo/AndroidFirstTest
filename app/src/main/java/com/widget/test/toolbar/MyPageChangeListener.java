package com.widget.test.toolbar;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

/**
 * @author sWX284798
 *
 */
public class MyPageChangeListener implements OnPageChangeListener
{
    private static final String TAG = "MyPageChangeListener";
    
    
    public interface OnDataChangedListener
    {
        void setChangedData(int position, float positionOffset, int positionOffsetPixels);
        void getChangedData();
    }

    private OnDataChangedListener listener; 
    
    /**
     * 
     */
    public MyPageChangeListener(OnDataChangedListener l)
    {
        // TODO Auto-generated constructor stub
        listener = l;
    }

    /**
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int arg0)
    {
        // TODO Auto-generated method stub
        //Log.d(TAG, "onPageScrollStateChanged " + String.valueOf(arg0));
    }

    /**
     * 当从左向右滑动时，position=当前正在滑动的pager的下标，positionOffset由0-1变化，positionOffsetPixels逐渐增大，
     * 当滑动到下一个pager，position + 1，其他为0。<p>
     * 当从右向左滑动时，position=当前正在滑动的pager的下标 - 1 ,positionOffset由1-0变化,positionOffsetPixels逐渐减小，
     * 当滑动到下一个pager，position不变，其他为0。
     * 
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        // TODO Auto-generated method stub
        Log.d(TAG, "onPageScrolled " + String.valueOf(position) + " " + String.valueOf(positionOffset) 
                + " " + String.valueOf(positionOffsetPixels));
        if(listener != null)
        {
            listener.setChangedData(position, positionOffset, positionOffsetPixels);
        }
        
        
    }

    /**
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int arg0)
    {
        // TODO Auto-generated method stub
        Log.d(TAG, "onPageSelected " + String.valueOf(arg0));
//        if(listener != null)
//        {
//            listener.setChangedData(arg0);
//        }
        //curIndex = arg0;
    }

}
