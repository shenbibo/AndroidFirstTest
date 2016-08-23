package component.data.exchange.activity;

import java.lang.ref.WeakReference;

import com.example.androidfirsttest.R;

import component.data.exchange.fragment.FirstFragment;
import component.data.exchange.fragment.SecondFragment;
import component.data.exchange.fragment.TaskFragment;
import component.data.exchange.fragment.TaskFragment.IStatusChangeListener;
import component.data.exchange.service.MyService;
import component.data.exchange.service.MyService.MyBinder;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Activity 向Fragment传递数据通过Bundle
 * Fragment向Activity传递数据通过Fragment的 onAttach方法，activity实现Fragment定义的接口
 * Fragment向Fragment传递数据通过其依靠的Activity，如果想实现两个Fragment同步更新，则需要另个一个Fragment实现Activity中使用的接口。
 * @author sWX284798
 * @date 2015年12月17日 下午2:52:31
 * @version
 * @since
 */
public class BaseFragmentActivity extends FragmentActivity implements IStatusChangeListener
{
    private static final String TAG = "BaseFragmentActivity";
    
    private static MyHandler fHandler;
    
    private static MyHandler sHandler;
    
    private ViewGroup container1;
    
    private ViewGroup container2;
    
    private IGetStatusValue listener;
    
    TaskFragment inputFargment;
    TaskFragment outputFargment;
    private MyService myService;
    private ServiceConnection conn = new ServiceConnection()
    {
        
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            myService = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MyBinder binder = (MyBinder) service;
            myService = binder.getService();
            Log.d(TAG, "service is connected");
        }
    };
    
    
    private FragmentManager fm = getSupportFragmentManager();
    
    public interface IGetStatusValue
    {
        void putValue(String value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_fragment_activity_layout);
        fHandler = new MyHandler(this, "fHandler");
        sHandler = new MyHandler(this, "sHandler");
        initViews();
        initFragment();
        showFragment(inputFargment, R.id.container1, "inputFragment");
        showFragment(outputFargment, R.id.container2, "outputFragment");
        listener = (IGetStatusValue) outputFargment;
        Intent service = new Intent(this, MyService.class);
        if(bindService(service, conn, Context.BIND_AUTO_CREATE))
        {
            Log.d(TAG, "bindService is return true and myService = " + myService);
        }



        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("BaseFragmentActivity", "start SecondActivity");
                BaseFragmentActivity.this.startActivity(new Intent(BaseFragmentActivity.this, SecondActivity.class));
//                BaseFragmentActivity.this.finish();
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("BaseFragmentActivity", "start FirstActivity");
                BaseFragmentActivity.this.startActivity(new Intent(BaseFragmentActivity.this, FirstActivity.class));
            }
        }.start();
    }
    
    private void initViews()
    {
        
    }
    
    private void initFragment()
    {
        inputFargment = new FirstFragment();
        outputFargment = new SecondFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("firstFragment", "inputFargment");
        inputFargment.setArguments(bundle1);
        
        
        //Activity 向Fragment传递数据通过Bundle
        Bundle bundle2 = new Bundle();
        bundle2.putString("secondFragment", "outputFargment");
        outputFargment.setArguments(bundle2);
        
    }
    
    private void showFragment(Fragment fragment, int containerId, String tag)
    {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment tempFragment = null;;        
        if(tag != null)
        {
            tempFragment = fm.findFragmentByTag(tag);
        }
        
        if(tempFragment != null)
        {
            ft.show(tempFragment);
            //
        }
        else
        {
            ft.replace(containerId, fragment, tag);
            //ft.add(containerId, fragment, tag);
        }
        ft.commit();
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatusValue(String status)
    {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = status;        
        fHandler.sendMessage(msg);
    }
    
    private static class MyHandler extends Handler
    {
        private WeakReference<BaseFragmentActivity> weakRef; 
        
        private String handlerTag;
        
        public MyHandler(BaseFragmentActivity activity, String tag)
        {
            weakRef = new WeakReference<BaseFragmentActivity>(activity);
            handlerTag = tag;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void handleMessage(android.os.Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    String str = (String) msg.obj;
                    
                    Log.d(TAG, "handlerTag = " + handlerTag + ", str = " + str);
                    BaseFragmentActivity activity = weakRef.get();
                    if(activity != null)
                    {
                        activity.listener.putValue(str);
                        activity.myService.setValue(str);
                    }
                    break;
                    
                default:                   
                    break;
                        
            }
        };
    }


    
}
