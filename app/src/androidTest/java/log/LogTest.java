package log;

import android.os.Environment;
import android.os.Process;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/28
 */
@RunWith(AndroidJUnit4.class)
public class LogTest {
    private static final String TAG = "LogTest";
    private static final boolean DEBUG = true;

    @BeforeClass
    public static void init() {
        LogCacheTree.LogCacheConfig logFileConfig = new LogCacheTree.LogCacheConfig();
        logFileConfig.logFileDir = Environment.getExternalStorageDirectory()
            + File.separator + "logTest" + File.separator;

        logFileConfig.backupFile = logFileConfig.logFileDir + "applog0.txt";
        logFileConfig.curWriteFile = logFileConfig.logFileDir + "applog1.txt";
        logFileConfig.maxLogFileLength = 1024 * 1024 * 3;
        logFileConfig.maxLogMemoryCacheSize = 1024 * 512;
        //Logger.init(new FileTree(Logger.VERBOSE, logFileConfig));
        //Logger.init(new LogcatTree(Logger.VERBOSE), new FileTree(Logger.VERBOSE, logFileConfig));
        Logger.init(1024 * 1024, new LogcatTree(Logger.VERBOSE), new LogCacheTree(Logger.VERBOSE, logFileConfig));
    }

    @Test
    public void testPrintlnLogInLooper() {
        //        LogCacheConfig logFileConfig = new LogCacheConfig();
        //        logFileConfig.logFileDir = Environment.getExternalStorageDirectory()
        //            + File.separator + "logTest" + File.separator;
        //        logFileConfig.backupFile = logFileConfig.logFileDir + "applog0.txt";
        //        logFileConfig.curWriteFile = logFileConfig.logFileDir + "applog1.txt";
        //        logFileConfig.maxLogFileLength = 1024 * 1024 * 3;
        //        logFileConfig.maxMemoryLogSize = 1024 * 1024 * 5;
        //        logFileConfig.maxMsgCachedCount = 10000;
        //
        //        LogcatTree logcatTree = new LogcatTree(Logger.VERBOSE);
        //        TreeManager treeManager = new TreeManager();
        //        treeManager.init(1024 * 1024);
        //        treeManager.addLogTrees(new LogcatTree(Logger.VERBOSE), new FileTree(Logger.VERBOSE, logFileConfig));
        int count = 100;
        String[] msgs = new String[count];
        for (int i = 0; i < count; i++) {
            msgs[i] = "this is test file tree, log num = " + i;
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            long startTime1 = System.nanoTime();
            //Logger.info(TAG, "this is test file tree, log num = " + i);
            //logcatTree.handleMsgOnCalledThread(Logger.INFO, TAG,  "this is test file tree, log num = " + i,  null);
            //logcatTree.handleMsgOnCalledThread(Logger.INFO, TAG,  msgs[i],  null);
            // Logger里面直接调用LogcatTree.handleMsgOnCalledThread 大约需要21us
            Logger.info(TAG, msgs[i]);
            //treeManager.handleMsgOnCalledThread(Logger.INFO, TAG, msgs[i], null);
            long finishTime1 = System.nanoTime();
            Log.i(TAG, i + " msg take time = " + (finishTime1 - startTime1) / 1000);
            //SystemClock.sleep(500);
        }
        long finishTime = System.currentTimeMillis();

        Log.i(TAG, "println count = " + count + " msg, take time = " + (finishTime - startTime));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "now print memory cached logs");
        //        List<byte[]> bytes = Logger.getMemoryCachedMsg();
        //        for (byte[] bytes1 : bytes) {
        //            Logger.info(TAG, "cached msg = " + new String(bytes1));
        //        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintlnLogInLooper2() {
        int count = 1000;
        String[] msgs = new String[count];
        for (int i = 0; i < count; i++) {
            msgs[i] = "this is test file tree, log num = " + i;
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            Logger.info(TAG, msgs[i]);
        }
        long finishTime = System.nanoTime();

        long startTime1 = System.nanoTime();
        for (int i = 0; i < count; i++) {
            Log.i(TAG, msgs[i]);
        }
        long finishTime1 = System.nanoTime();

        long startTime2 = System.nanoTime();
        for (int i = 0; i < count; i++) {
            i(TAG, msgs[i]);
        }
        long finishTime2 = System.nanoTime();

        Log.i(TAG, "Logger.info println, count = " + count + " msg, take time = " + (finishTime - startTime) / 1000);
        Log.i(TAG, "Log.i println count = " + count + " msg, take time = " + (finishTime1 - startTime1) / 1000);
        Log.i(TAG, "i println count = " + count + " msg, take time = " + (finishTime2 - startTime2) / 1000);

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPrintlnLogInLooper3() {
        int count = 10;
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
            long startTime2 = System.nanoTime();
            Log.i(TAG, msgs[i]);
            long finishTime2 = System.nanoTime();
            Log.i(TAG, "Log.i = " + i + " time = " + (finishTime2 - startTime2) / 1000);
        }
        long finishTime1 = System.currentTimeMillis();
        Log.i(TAG, "Log.i println count = " + count + " msg, take time = " + (finishTime1 - startTime1));
    }

    @Test
    public void testPrintlnLogInThread() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            new PrintLogThread(true).start();
        }
        new PrintMemoryCacheLogThread().start();
        Thread.sleep(100);
        long startTime = System.currentTimeMillis();
        synchronized (LOCK) {
            LOCK.notifyAll();
        }

        countDownLatch.await();
        long finishTime = System.currentTimeMillis();
        long loggerTime = finishTime - startTime;

        //        for (int i = 0; i < THREAD_COUNT; i++) {
        //            new PrintLogThread(false).start();
        //        }

        Thread.sleep(100);

        long startTime1 = System.currentTimeMillis();
        synchronized (LOCK) {
            LOCK.notifyAll();
        }

        //        countDownLatch2.await();
        long finishTime1 = System.currentTimeMillis();
        long logTime = finishTime1 - startTime1;

        Log.i(TAG, "loggerTime = " + loggerTime + ", logcatTime = " + logTime);

        Thread.sleep(600000);

    }

    @Test
    public void buildMessageTest() {
        for (int i = 0; i < 1000; i++) {
            long startTime2 = System.nanoTime();
            i(TAG, "normalTest");
            long finishTime2 = System.nanoTime();
            Log.i(TAG, "i = " + i + " time = " + (finishTime2 - startTime2) / 1000);
        }

        for (int i = 0; i < 1000; i++) {
            long startTime2 = System.nanoTime();
            Logger.info(TAG, "normalTest");
            long finishTime2 = System.nanoTime();
            Log.i(TAG, "Logger.info = " + i + " time = " + (finishTime2 - startTime2) / 1000);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.i(tag, buildMessage(msg));
        }
    }

    /**
     * Building Message
     *
     * @param msg The message you would like logged.
     * @return Message String
     */
    private static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder().append(caller.getClassName()).append(".").append(caller.getMethodName()).append(
            "().").append(caller.getLineNumber()).append(": ").append(msg).toString();
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

            int count = 1000;
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
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    private class PrintMemoryCacheLogThread extends Thread {
        @Override
        public void run() {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("/mnt/sdcard/logFile.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for (int k = 0; k < 1000; k++) {
                Log.i(TAG, "start print memory cached logs index = " + k);
                List<byte[]> bytes = Logger.getMemoryCachedMsg();
                //StringBuilder stringBuilder = new StringBuilder();
                for (byte[] bytes1 : bytes) {
                    Logger.info(TAG, "cached msg = " + new String(bytes1));
                    //Log.i(TAG, "cached msg = " + new String(bytes1));
                    //                    stringBuilder.append(new String(bytes1));
                    //                    try {
                    //                        fos.write(bytes1);
                    //                    } catch (IOException e) {
                    //                        e.printStackTrace();
                    //                    }
                }
                //Log.i(TAG, stringBuilder.toString());
                Log.i(TAG, "finish print memory cached logs index = " + k);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
