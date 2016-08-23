package com.example.androidfirsttest;

import android.R.integer;
import android.util.Log;

/**
 * 测试try catch 对性能的影响。
 * 
 * @author sWX284798
 * @date 2015年8月17日 上午11:29:49
 * @version
 * @since
 */
public class TryCatchTest
{
    private final static String TAG = "TryCatchTest";
    
    private int performCounts = 0;

    public TryCatchTest(int performCounts)
    {
        this.performCounts = performCounts;        
    }
    
    public void test()
    {
        haveTryCatchTest();
        noTryCatchTest();
        oneTryCatchTest();
    }
    
    private void haveTryCatchTest()
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < performCounts; i++)
        {
            try
            {
                testedMethod(i);  //该方法越复杂，耗费的时间越多
            }
            catch (NumberFormatException e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        long finishTime = System.currentTimeMillis();
        Log.d(TAG, "haveTryCatchTest = " + (finishTime - startTime));
        
    }
    
    private void noTryCatchTest()
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < performCounts; i++)
        {

            testedMethod(i);

        }
        long finishTime = System.currentTimeMillis();
        Log.d(TAG, "noTryCatchTest = " + (finishTime - startTime));
    }
    
    private void oneTryCatchTest()
    {
        long startTime = System.currentTimeMillis();
        try
        {
            for (int i = 0; i < performCounts; i++)
            {

                testedMethod(i);   

            }
        }
        catch (NumberFormatException e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        long finishTime = System.currentTimeMillis();
        Log.d(TAG, "oneTryCatchTest TIME = " + (finishTime - startTime));
    }
    
    private void testedMethod(int counts) throws NumberFormatException
    {
        //int counts = 0;
        //for(int i = 0; i < 1000; i++)
        {
            counts = Integer.valueOf(String.valueOf(++counts));
        }
    }
}
