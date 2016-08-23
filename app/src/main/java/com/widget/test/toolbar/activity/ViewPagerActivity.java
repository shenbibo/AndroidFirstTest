/*
 * 文 件 名:  ViewPagerActivity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月6日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.widget.test.toolbar.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.androidfirsttest.R;
import com.widget.test.toolbar.FragmentFactory;
import com.widget.test.toolbar.MyFragmentAdapter;
import com.widget.test.toolbar.MyPageChangeListener;
import com.widget.test.toolbar.fragment.FirstFragment;
import com.widget.test.toolbar.fragment.SecondFragment;
import com.widget.test.toolbar.fragment.ThirdFragment;
import com.widget.test.toolbar.view.ColumnLinearLayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 */
public class ViewPagerActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    
    //private UiCallBack callBack;
    
    private ViewPager pager;
    
    private OnPageChangeListener listener;
    
    private ColumnLinearLayout columnLinearLayout;
    
    /** {@inheritDoc} */
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity_layout);
        initViews();
    }
    
    private void initViews()
    {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        pager = (ViewPager) findViewById(R.id.viewPager);
//        toolbar.setTitle("");  //设置标题
//        toolbar.setSubtitle(""); //设置副标题
//        toolbar.setLogo(null); //设置logo图标
        //此处必须要使用具体的类型LinearLayout，不能使用View, 否则在传入addView()方法时，会导致运行时的类型擦除
//        LinearLayout columnView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.viewpager_column_layout, new LinearLayout(this));
//        toolbar.addView(columnView);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(null);
//        TextView textView = new TextView(this);
//        textView.setText("你大爷的");
//        toolbar.addView(textView);
        columnLinearLayout= new ColumnLinearLayout(this, 3);
        columnLinearLayout.setViewPager(pager);
        toolbar.addView(columnLinearLayout);
        initPagerView();
        
    }
    
    private void initPagerView()
    {
        
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(FragmentFactory.newInstance(FirstFragment.class));
        fragments.add(FragmentFactory.newInstance(SecondFragment.class));
        fragments.add(FragmentFactory.newInstance(ThirdFragment.class));
        pager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
        listener = new MyPageChangeListener(columnLinearLayout);
        pager.addOnPageChangeListener(listener);
        
    }
    
    /** {@inheritDoc} */
     
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        //callBack.doChanged();
    }
    
    /** {@inheritDoc} */
     
    @Override
    public void finish()
    {
        // TODO Auto-generated method stub
        //在finish的时候移除状态监听器
        if(pager != null)
        {
            pager.removeOnPageChangeListener(listener);
        }
        super.finish();
    }
}
