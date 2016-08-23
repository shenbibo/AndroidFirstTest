package javacontainer.DelayQueue.bean;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月5日 上午9:42:01
 * @version
 * @since
 */
public class ExamEnd extends Student
{
        
    private Thread teacher;
        
    private DelayQueue<Student> examStudents;
    
    /**
     * 
     */
    public ExamEnd(DelayQueue<Student> str, int examTime, Thread teacher, CountDownLatch counter)
    {
        super("exam time is up ", examTime, counter);
        examStudents = str;
        this.teacher = teacher;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        if(teacher.isAlive())
        {
            teacher.interrupt();
        }
        Student s = null;
        for (Iterator<Student> iterator2 = examStudents.iterator(); iterator2.hasNext();)
        {
            s = iterator2.next();
            s.setForce(true);
            s.run();
        }
        counter.countDown();
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public int compareTo(Delayed another)
//    {
//        if(another == null)
//        {
//            return 1;
//        }
//        if(this == another)
//        {
//            return 0;
//        }
//        if(another instanceof ExamEnd)
//        {
//            ExamEnd s = (ExamEnd) another;
//            //在一个类的内部定义一个该类的对象时，可以直接访问其私有成员。
//            if(s.examTime > examTime)
//            {
//                return -1;
//            }
//            else if (s.examTime == examTime)
//            {
//                return 0;
//            }
//            else
//            {
//                return 1;
//            }
//        }           
//        return getDelay(TimeUnit.NANOSECONDS) >= another.getDelay(TimeUnit.NANOSECONDS) ? 1 : -1;
//        
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public long getDelay(TimeUnit unit)
//    {        
//        return unit.convert(workTime - System.nanoTime(), TimeUnit.NANOSECONDS);
//    }

}
