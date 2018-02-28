package log;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

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
        logFileParam.maxFileLength = 1024 * 1024;
        logFileParam.maxMemoryLogLength = 1024 * 1024;
        logFileParam.maxMsgCachedCount = 2000;
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
}
