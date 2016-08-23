package javacontainer.DelayQueue.bean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import javacontainer.DelayQueue.DelayQueueTest;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月5日 上午9:41:44
 * @version
 * @since
 */
public class Teacher implements Runnable
{

    private DelayQueue<Student> students;
    
    
    /**
     * 
     */
    public Teacher(DelayQueue<Student> students)
    {
        this.students = students;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                Runnable run = (Runnable) students.take();
                if(run != null)
                {
                    run.run();
                }
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e)
        {
            //counter.countDown();
            Log.d(DelayQueueTest.TAG, "exam time is end , get the student's test paper now !!!!!!!!!!");
        }
        
    }

}
