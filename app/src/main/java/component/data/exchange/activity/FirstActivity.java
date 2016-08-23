package component.data.exchange.activity;

import com.example.androidfirsttest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Acitivity之间的解耦和的数据交互方式之一是：使用startActivityForResult启动其他Activity，被启动的Activity在finish前调用setResult方法。
 * 
 * @author sWX284798
 * @date 2015年12月17日 上午10:38:29
 * @version
 * @since
 */
public class FirstActivity extends Activity
{

    private static final String TAG = "FirstActivity";
    
    private static final int REQUEST_CODE = 0; 
    
    /**
     * 跳转其他的界面
     * */
    private Button enterButton;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_layout);
        initView();
        setViewResponseEvent();
    }
    
    
    private void initView()
    {
        enterButton = (Button) findViewById(R.id.skipButton);
    }
    
    private void setViewResponseEvent()
    {
        enterButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(FirstActivity.this, SecondActivity.class);
                FirstActivity.this.startActivityForResult(i, REQUEST_CODE);
                
                 //不注释掉该行则在SecondActivity中即使设置了setResult方法也无法显示该Activity，
                //因为Activity已经销毁
                //FirstActivity.this.finish();  
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        
        super.onActivityResult(requestCode, resultCode, data);
        
        Log.d(TAG, "requestCode = " +  requestCode + ", resultCode = " + resultCode);
        if(data != null)
        {
            Log.d(TAG, "str = " + data.getStringExtra("str"));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause()
    {
        
        super.onPause();
        Log.d(TAG, "FirstActivity onPaused called");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop()
    {
        
        super.onStop();
        Log.d(TAG, "firstActivity onStop called");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy()
    {
        
        super.onDestroy();
        Log.d(TAG, "firstActivity onDestory called");
    }
}
