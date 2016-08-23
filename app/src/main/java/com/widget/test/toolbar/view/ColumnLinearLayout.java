/*
 * 文 件 名:  ColumnLinearLayout.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月6日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.widget.test.toolbar.view;

import com.example.androidfirsttest.R;
import com.widget.test.toolbar.MyPageChangeListener.OnDataChangedListener;

import common.tools.CovertHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 */
public class ColumnLinearLayout extends LinearLayout implements OnDataChangedListener,OnClickListener
{

    private static final String TAG = "ColumnLinearLayout";
    
    private int columnSize;
    
    private LinearLayout column;
    
    private ImageView pointer;
    
    private int tabLength;
    
    private int pointerWidth;
    
    private ViewPager pager;
    
    private int baseLeftPos;
    
    private int baseRightPos;
    
    /** <默认构造函数>
     */
    public ColumnLinearLayout(Context context, int columnSize)
    {
        super(context);
        // TODO Auto-generated constructor stub
        this.columnSize = columnSize; 
        init(context);
    }

    public void setViewPager(ViewPager pager)
    {
        this.pager = pager;
    }
    
    private void init(Context context)
    {
        //自定义布局属性
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.VERTICAL);        
        //View rootView = LayoutInflater.from(context).inflate(R.layout.viewpager_column_layout, this);
        initViews(context, params);
        //setOnClickListener(this);
    }
    
    private void initViews(Context context, LinearLayout.LayoutParams params)
    {
        tabLength = CovertHelper.getScreenWidth(context) / columnSize;
        Log.d(TAG, "tabLength = " + tabLength);
        ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(tabLength, ViewGroup.LayoutParams.MATCH_PARENT);
        column = new LinearLayout(context);
        //column.setLayoutParams(params);
        column.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < columnSize; i++)
        {
            column.addView(generateTextView(context, textParams, "第" + i + "页"));
        }
        addView(column);
        
        pointer = new ImageView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp.
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flg_point);
        pointerWidth = bitmap.getWidth();
        pointer.setImageBitmap(bitmap);
        pointer.setLayoutParams(lp);               
        addView(pointer);

        //使用动画也可以达到移动的效果
//        Animation animation = new TranslateAnimation(0, tabLength / 2, 0, 0);//平移动画  
//        animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态  
//        animation.setDuration(0);//动画持续时间2ms
//        pointer.startAnimation(animation);//是用ImageView来显示动画的    
        baseLeftPos = (tabLength - pointerWidth) / 2 ;
        baseRightPos = (tabLength + pointerWidth) / 2;
    }
    
    private TextView generateTextView(Context context, ViewGroup.LayoutParams params, String text)
    {
        TextView textView = new TextView(context);
        //text.setHeight(LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setTextSize(20);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        textView.setPadding(0, CovertHelper.dp2Pix(4), 0, CovertHelper.dp2Pix(4));
        return textView;
    }

       
    
    /** {@inheritDoc} */
     
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        ////使用left和right方法时，需要同时设置，且在其他onLayout之前的方法中调用可能会无效
        //使用动画也可以达到移动的效果
//        pointer.setLeft((tabLength - pointerWidth) / 2 );
//        pointer.setRight((tabLength + pointerWidth) / 2);
        scrollPointerToCurrentPager();
    }

    /** {@inheritDoc} */
     
    @Override
    public void setChangedData(int pos, float value, int offsetPix)
    {
        // TODO Auto-generated method stub
        scrollPointerByViewPagerMove(pos, value);
        
    }

    /** {@inheritDoc} */
     
    @Override
    public void getChangedData()
    {
        // TODO Auto-generated method stub
        
    }
    
    private void scrollPointerToCurrentPager()
    {
        int position = pager.getCurrentItem();
        scrollPointerByViewPagerMove(position, 0);
//        pointer.setLeft((tabLength - pointerWidth) / 2 + position * tabLength);
//        pointer.setRight((tabLength + pointerWidth) / 2 + position * tabLength);
    }
    
    private void scrollPointerByViewPagerMove(int pos, float value)
    {
        int newLeftPos = (int) ((pos + value) * tabLength + baseLeftPos);
        int newRightPos = (int) ((pos + value) * tabLength + baseRightPos);
        pointer.setLeft(newLeftPos);
        pointer.setRight(newRightPos);
        
    }

    /** {@inheritDoc} */
     
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Log.d(TAG, "v.className = " + v.getClass().getName());
        int childCounts = column.getChildCount();
        for(int i = 0; i < childCounts; i++)
        {
            if(v == column.getChildAt(i))
            {
                pager.setCurrentItem(i);
                break;
            }
        }
        
    }
}
