/*
 * 文 件 名:  ToolBarTestActivity.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  sWX284798
 * 修改时间:  2016年1月5日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.widget.test.toolbar.activity;


import com.example.androidfirsttest.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 */
public class ToolBarTestActivity extends AppCompatActivity
{
    
    private Button covertBtn;
    
    /** {@inheritDoc} */
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_test_activity_layout);
        initToolbar();
        
    }
    
    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("Title");  //设置标题
        toolbar.setSubtitle("Sub Title"); //设置副标题
        toolbar.setLogo(R.drawable.ic_launcher); //设置logo图标
        setSupportActionBar(toolbar); 
        
        toolbar.setNavigationIcon(R.drawable.icon04); //设置最左边的导航图标
        
        //添加自定义的view
//        Button button = new Button(this);
//        button.setText("toolbarBtn");
//        button.setWidth(0);
//        button.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "toolbarBtn", Toast.LENGTH_SHORT).show();
//            }
//        });
//        toolbar.addView(button);
        covertBtn = (Button) findViewById(R.id.next);
        setViewControlEvents();
    }
    
    private void setViewControlEvents()
    {
        covertBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent i = new Intent(ToolBarTestActivity.this, ViewPagerActivity.class);
                ToolBarTestActivity.this.startActivity(i);
            }
        });
    }

    /** {@inheritDoc} */
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /** {@inheritDoc} */
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        switch (id)
        {
        case R.id.setting:
            Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
            return true;
            
        case R.id.refresh:
            Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
            return true;
            
        case R.id.share:
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
            return true;
            
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
