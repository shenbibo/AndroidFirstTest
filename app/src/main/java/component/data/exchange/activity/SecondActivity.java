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
 * 
 * 
 * @author sWX284798
 * @date 2015年12月17日 上午10:48:07
 * @version
 * @since
 */
public class SecondActivity extends Activity
{
    
    private static final String TAG = "SecondActivity";
    
    public static final int RESPONSE_CODE = 1;

    private Button backButton;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);
        initView();
        setViewResponseEvents();
    }
    
    private void initView()
    {
        backButton = (Button) findViewById(R.id.backButton);
    }
    
    private void setViewResponseEvents()
    {
        backButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("str", "testStr");
                SecondActivity.this.setResult(RESPONSE_CODE, intent);
                SecondActivity.this.finish();  //注释掉该行则无法跳转回去到FirstActivity
            }
        });
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
        Log.d(TAG, "SecondActivity onStop called");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy()
    {
        
        super.onDestroy();
        Log.d(TAG, "SecondActivity onDestory called");
    }
}
