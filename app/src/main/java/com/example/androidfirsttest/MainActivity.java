package com.example.androidfirsttest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.gc.test.view.GCtestAtivity;
import com.handler.delay.test.HandlerDelayTestActivity;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import rxjava.view.RxJavaTestActivity;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private Button middleButton;
    private Button leftTopButton2;
    

    private static final int testValue = 1;
    
    private int testCase()
    {
        Log.d(TAG, testValue + "_123456");
        return testValue;
    }
    
    private class testClass
    {
        /**
         * @return 
         * 
         * @Description: TODO
         * @param     
         * @return void    
         * @throws
         */
        public int getWrappedValue()
        {
            // TODO Auto-generated method stub
            new Thread()
            {
                /**
                 * @see java.lang.Thread#run()
                 */
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    int tempValue = testValue + 1;
                    int i = testCase();
                    Log.d(TAG, Log.getStackTraceString(new Throwable()));
                }
            }.start();

            return 1;
        }
    }
    
    private String str = "123456";
    private Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case 0:
                    createFileTest();
                    break;

            case 1:
                    //MainActivity.this.finish();
                    break;
            default:
                    break;
            }
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		middleButton = (Button) findViewById(R.id.alphaBtn);


		middleButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                
//                MockAPITest.testTextUtilClass();
//                TestTextUtils.callTextUtils();
                //DbTest.testDbisClosed();
                //ClassExtendsTest.testClass();
                //MainActivity.this.startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                //GZIPUtils.Test();
                
                //以系统一样包名，名称的类的方式试图模拟系统API
                //MockAPITest.testException();
                
                //命令模式测试
                //CommandPatternTest.test();
                
                //delayQueue与CountDownLatch测试
               // DelayQueueTest.test();
                
                //网络获取图片测试
                //NetworkTest.ImageGetTest();
                
                //启动显示下载列表的Activity.
//                Intent i = new Intent(MainActivity.this, DownloadControlActivity.class);
//                MainActivity.this.startActivity(i);
                
                //Activity之间跳转的数据交互
//                Intent i = new Intent(MainActivity.this, FirstActivity.class);
//                MainActivity.this.startActivity(i);
                
                //Fragment与Activity之间数据交互
//                Intent i = new Intent(MainActivity.this, BaseFragmentActivity.class);
//                MainActivity.this.startActivity(i);
                
                //测试补间、帧动画
//                Intent i = new Intent(MainActivity.this, ViewAnimationActivity.class);
//                MainActivity.this.startActivity(i);
                //activity之间切换的效果动画
                //MainActivity.this.overridePendingTransition(R.anim.set_animation_test, R.anim.set_animation_test);
                
                //测试属性动画              
//                Intent i = new Intent(MainActivity.this, PropertyAnimationActivity.class);
//                MainActivity.this.startActivity(i);
//                Intent i = new Intent("android.intent.action.VIEW");
//                i.setData(Uri.parse("market://details?id=com.intsig.BizCardReader"));
//                MainActivity.this.startActivity(i);
                //com.intsig.BizCardReader
                //com.intsig.BCRLite

                //ComponentIteration.test();
                
                //空指针堆栈异常验证
                //NullPointExceptionTest.main(null);
                
                //启动测试toolbar的activity
//                Intent i = new Intent(MainActivity.this, ToolBarTestActivity.class);
//                MainActivity.this.startActivity(i);
                
                //GC测试
//                Intent i = new Intent(MainActivity.this, GCtestAtivity.class);
//                MainActivity.this.startActivity(i);
                
                //Android ipc机制测试
//                Intent i = new Intent(MainActivity.this, IpcTestActivity.class);
//                MainActivity.this.startActivity(i);

                //测试TableLayout
//                Intent i = new Intent(MainActivity.this, TableLayoutTestActivity.class);
//                MainActivity.this.startActivity(i);

                //打开摄像头测试
//                Intent i = new Intent(MainActivity.this, CameraTestActivity.class);
//                MainActivity.this.startActivity(i);

                //发送延时消息，抓取堆栈查看是否会停留在MessageQueue.nativePollOnce()方法中,并且是否会导致主线程阻塞
//                  Intent i = new Intent(MainActivity.this, HandlerDelayTestActivity.class);
//                  MainActivity.this.startActivity(i);

                // Rxjava测试界面
                  Intent i = new Intent(MainActivity.this, RxJavaTestActivity.class);
                  MainActivity.this.startActivity(i);

            }
        });
		leftTopButton2 = (Button) findViewById(R.id.scaleBtn);
		leftTopButton2.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                  //tryCatchTest();
                //DbTest.startTest(MainActivity.this);
                //网络获取普通数据测试
                //NetworkTest.textGetTest();
                
                //获取本地普通文件测试
                //NetworkTest.getLocalFileTest();
                
                //网络下载文件测试
                //NetworkTest.downloadFileTest();
                //NetworkTest.downloadFileTest();
                
                //启动显示下载列表的Activity.
//                Intent i = new Intent(MainActivity.this, DownloadControlActivity.class);
//                MainActivity.this.startActivity(i);
                GCtestAtivity.getActivityMemberObjStatu();
            }
        });
		
		//startService(new Intent(this, MyNotificationListenerService.class));
		
	}
	
	public void exceptionInfoPrintTest()
	{
	    int i = 0;
        try
        {
            i = 0;
            int b = 3 / i;
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.d(TAG, "e.toString() = " + e.toString());
            Log.d(TAG, "e.getMessage() = " + e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.d(TAG, " e.printStackTrace(pw); = " + sw.toString());
            Log.d(TAG, e.getLocalizedMessage());
        }
	}
	
	public void startActivityTest()
	{
//      Intent intent = new Intent("com.huawei.appmarket.ext.public");
//      intent.putExtra("openStr", "{\"openId\": \"2\"}");
////      intent.setClassName("com.huawei.appmarket", 
////              "com.huawei.appmarket.service.externalapi.view.ThirdApiActivity");
//      MainActivity.this.startActivity(intent);
	}
	
	private void tryCatchTest()
	{
	    new Thread()
	    {
	        /**
	         * {@inheritDoc}
	         */
	        @Override
	        public void run()
	        {
	            
	            TryCatchTest test = new TryCatchTest(1000000);
	            test.test();
	        }
	    }.start();
        
	}
	
   /**
     * 
     * 
     * @param     
     * @return void    
     * @throws
     */
    private void readOneCharTest()
    {
        //每次读取一个字符 测试 start
//      Utils.writeFile(MainActivity.this);
//      long timeStart = System.currentTimeMillis();
//      String data1 = Utils.readFilenNormal(MainActivity.this);
//      long timeFinish = System.currentTimeMillis();
//      Log.d(TAG, "data1.length = " + data1.length());
//      Log.d(TAG, "normal time = " + (timeFinish - timeStart));
//      timeStart = System.currentTimeMillis();
//      String data2 = Utils.readFileAllData(MainActivity.this);
//      timeFinish = System.currentTimeMillis();
//      Log.d(TAG, "allData time = " + (timeFinish - timeStart));
//      if(data1.equals(data2))
//      {
//          Log.d(TAG, "data1 = data2");
//      }
//      else 
//      {
//          Log.d(TAG, "data1 != data2, data1 = " + data1 + " data2 = " + data2);
//      }
//      
//      timeStart = System.currentTimeMillis();
//      String data3 = Utils.readFileOneLineByOneChar(MainActivity.this); 
//      timeFinish = System.currentTimeMillis();
//      Log.d(TAG, "readLine time = " + (timeFinish - timeStart));
//      if(data1.equals(data3))
//      {
//          Log.d(TAG, "data1 = data3");
//      }
//      else
//      {
//          Log.d(TAG, "data1 != data3, data1 = " + data1 + " data3 = " + data3);    
//      }
      /////每次读取一个字符测试，结束////////////////////////////////////////////////////////
    }
	
	/**
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause()
	{
	    // TODO Auto-generated method stub
	    Log.d(TAG, "onPause");
	    super.onPause();
	    //finish();
	}
	
	/**
	 * @see android.support.v7.app.ActionBarActivity#onStop()
	 */
	@Override
	protected void onStop()
	{
	    // TODO Auto-generated method stub
	    Log.d(TAG, "onStop called");
	    super.onStop();
	}
	
	private void createFileTest()
	{
	    OutputStream out = null;
        byte[] buffer = {1,2,3,4,5};
        
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            Log.d(TAG, "cannot find path :" + Environment.getExternalStorageState());
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() 
                + File.separator + "test.txt";
        //path = "/storage/sdcard1" + File.separator + "test.txt";
        Log.d(TAG, path);
        File file = new File(path);
      
        if(file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            
        }
        
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(file));
            int cout = 0;
            while (cout < 21000)
            {
                out.write(buffer);
                cout++;
                Thread.sleep(1);
            }
            
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(out != null)
                out.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onResume()
	{
	    
	    super.onResume();
	    //ViewServer.doGet(this).setFocusedWindow(this);
	}
	
	/**
	 * @see android.support.v7.app.ActionBarActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
	    // TODO Auto-generated method stub
	    Log.d(TAG, "onDestroy");
	    super.onDestroy();
	    //ViewServer.doGet(this).removeWindow(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up middleButton, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
