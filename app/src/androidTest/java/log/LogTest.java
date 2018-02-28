package log;

import android.os.Environment;
import android.os.Process;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import log.FileTree.LogFileParam;


/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/28
 */
@RunWith(AndroidJUnit4.class)
public class LogTest {
    private static final String TAG = "LogTest";

    @BeforeClass
    public static void init() {
        FileTree.LogFileParam logFileParam = new LogFileParam();
        logFileParam.logFilePath = Environment.getExternalStorageDirectory()
            + File.separator + "logTest" + File.separator;

        logFileParam.backupFile = logFileParam.logFilePath + "applog0.txt";
        logFileParam.curWriteFile = logFileParam.logFilePath + "applog1.txt";
        logFileParam.maxFileLength = 1024 * 1024 * 3;
        logFileParam.maxMsgCachedCount = 10000;
        Logger.init(new FileTree(Logger.VERBOSE, logFileParam));
    }

    @Test
    public void testPrintlnLogInLooper() {
        int count = 10000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Logger.info(TAG, "this is test file tree, log num = " + i);
        }
        long finishTime = System.currentTimeMillis();

        Log.i(TAG, "println count = " + count + " msg, take time = " + (finishTime - startTime));
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintlnLogInLooper2() {
        int count = 10000;
        String[] msgs = new String[count];
        for (int i = 0; i < count; i++) {
            msgs[i] = "this is test file tree, log num = " + i;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Logger.info(TAG, msgs[i]);
        }
        long finishTime = System.currentTimeMillis();

        Log.i(TAG, "Logger.info println, count = " + count + " msg, take time = " + (finishTime - startTime));

        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Log.i(TAG, msgs[i]);
        }
        long finishTime1 = System.currentTimeMillis();
        Log.i(TAG, "Log.i println count = " + count + " msg, take time = " + (finishTime1 - startTime1));
    }

    @Test
    public void testPrintlnLogInThread() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            new PrintLogThread(true).start();
        }

        Thread.sleep(100);
        long startTime = System.currentTimeMillis();
        synchronized (LOCK) {
            LOCK.notifyAll();
        }

        countDownLatch.await();
        long finishTime = System.currentTimeMillis();
        long loggerTime = finishTime - startTime;

        for (int i = 0; i < THREAD_COUNT; i++) {
            new PrintLogThread(false).start();
        }

        Thread.sleep(100);
        long startTime1 = System.currentTimeMillis();
        synchronized (LOCK) {
            LOCK.notifyAll();
        }

        countDownLatch2.await();
        long finishTime1 = System.currentTimeMillis();
        long logTime = finishTime1 - startTime1;

        Log.i(TAG, "loggerTime = " + loggerTime + ", logTime = " + logTime);

        Thread.sleep(60000);

    }

    private static final int THREAD_COUNT = 5;
    private static final Object LOCK = new Object();
    private static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(THREAD_COUNT);

    private class PrintLogThread extends Thread {

        private boolean isLogger;

        PrintLogThread(boolean isLogger) {
            this.isLogger = isLogger;
        }

        @Override
        public void run() {
            synchronized (LOCK) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int count = 3000;
            String[] msgs = new String[count];
            int threadId = Process.myTid();
            for (int i = 0; i < count; i++) {
                msgs[i] = "this is test file tree, threadId = " + threadId + ", log num = " + i;
            }
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                if (isLogger) {
                    Logger.info(TAG, msgs[i]);
                } else {
                    Log.i(TAG, msgs[i]);
                }
            }
            long finishTime = System.currentTimeMillis();
            String function = isLogger ? "Logger.info" : "Log.i";
            Log.i(TAG, function + " print,  threadId = " + threadId
                + "take time = " + (finishTime - startTime));

            if (isLogger) {
                countDownLatch.countDown();
            } else {
                countDownLatch2.countDown();
            }


        }
    }
}
