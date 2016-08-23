package javacontainer.DelayQueue;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

import javacontainer.DelayQueue.bean.Cache;
import javacontainer.DelayQueue.bean.ExamEnd;
import javacontainer.DelayQueue.bean.Student;
import javacontainer.DelayQueue.bean.Teacher;
import android.util.Log;

/**
 * 关于延迟队列的测试，参考网上DEMO http://www.cnblogs.com/sunzhenchao/p/3515085.html<p>
 * Delayed 元素的一个无界阻塞队列，只有在延迟期满时才能从中提取元素。<br>
 * 该队列的头部 是延迟期满后保存时间最长的 Delayed 元素。如果延迟都还没有期满，则队列没有头部，并且 poll 将返回 null。<br>
 * 当一个元素的 getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于等于 0 的值时，将发生到期。<br>
 * 即使无法使用 take 或 poll 移除未到期的元素，也不会将这些元素作为正常元素对待。<br>
 * 例如，size 方法同时返回到期和未到期元素的计数。此队列不允许使用 null 元素。<br>
 * 
 * @author sWX284798
 * @date 2015年11月4日 下午5:21:46
 * @version
 * @since
 */
public class DelayQueueTest
{
    public static final String TAG = "DelayQueueTest"; 
    
    public static void test()
    {
        //examSceneTest();
        overdueCacheDeleteTest();
    }
    
    /**
     * 场景一考试场景。<p>
     * 该场景来自于http://ideasforjava.iteye.com/blog/657384，模拟一个考试的日子，考试时间为120分钟，
     * 30分钟后才可交卷，当时间到了，或学生都交完卷了考试结束。<br>
     * 这个场景中几个点需要注意：<br>
     * 考试时间为120分钟，30分钟后才可交卷，初始化考生完成试卷时间最小应为30分钟<br>
     * 对于能够在120分钟内交卷的考生，如何实现这些考生交卷<br>
     * 对于120分钟内没有完成考试的考生，在120分钟考试时间到后需要让他们强制交卷<br>
     * 在所有的考生都交完卷后，需要将控制线程关闭<br>
     * 实现思想：用DelayQueue存储考生（Student类），每一个考生都有自己的名字和完成试卷的时间，<br>
     * Teacher线程对DelayQueue进行监控，收取完成试卷小于120分钟的学生的试卷。<br>
     * 当考试时间120分钟到时，先关闭Teacher线程，然后强制DelayQueue中还存在的考生交卷。<br>
     * 每一个考生交卷都会进行一次countDownLatch.countDown()，当countDownLatch.await()不再阻塞说明所有考生都交完卷了，而后结束考试。
     * */
    public static void examSceneTest()
    {
       new Thread(new Runnable()
       {

        @Override
        public void run()
        {
            int studentNum = 20;
            DelayQueue<Student> delayed = new DelayQueue<Student>();
            CountDownLatch counter = new CountDownLatch(studentNum + 1); 
            Random r = new Random();
            for(int i = 0; i < studentNum; i++)
            {
                delayed.put(new Student(i + "", 30 + r.nextInt(120), counter));
            }
            Thread teacherThread = new Thread(new Teacher(delayed));
            delayed.put(new ExamEnd(delayed, 120, teacherThread, counter));

            Log.d(TAG, "exam is start !!!!");
            teacherThread.start();
            try
            {
                counter.await();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d(TAG, "exam is end !!!!");
        }
           
       }).start();
    }
    
    /**
     * 模拟业务场景过期缓存删除。<p>
     * 该场景来自于http://www.cnblogs.com/jobs/archive/2007/04/27/730255.html，向缓存添加内容时，<br>
     * 给每一个key设定过期时间，系统自动将超过过期时间的key清除。<br>
     * 这个场景中几个点需要注意：<br>
     *  当向缓存中添加key-value对时，如果这个key在缓存中存在并且还没有过期，需要用这个key对应的新过期时间<br>
     *  为了能够让DelayQueue将其已保存的key删除，需要重写实现Delayed接口添加到DelayQueue的DelayedItem的hashCode函数和equals函数<br>
     *  当缓存关闭，监控程序也应关闭，因而监控线程应当用守护线程
     * */
    public static void overdueCacheDeleteTest()
    {
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int cacheNum = 10;
                Random r = new Random();
                Log.d(TAG, "cache test is begin");
                Cache<String, Integer> cache = new Cache<String, Integer>();
                int liveTime = 0;
                for(int i = 0; i < cacheNum; i++)
                {
                    liveTime = r.nextInt(3000);
                    cache.put(i + "", i, liveTime);
                }
                
                try
                {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cache.getDaemonThread().interrupt();
            }
            
        }).start();
    }

}
