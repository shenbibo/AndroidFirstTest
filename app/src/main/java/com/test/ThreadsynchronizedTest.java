package com.test;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年9月23日 上午11:09:27
 * @version
 * @since
 */
public class ThreadsynchronizedTest
{
    private static boolean isStoped = false;

    public static void synchronizedTest()
    {
        new Thread()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                //while()
                super.run();
            }
        }.start();
    }
}
