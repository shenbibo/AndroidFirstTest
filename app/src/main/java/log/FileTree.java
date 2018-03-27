package log;


import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class FileTree extends LogTree {
    private static final int DEAL_LOG_CLOSED = 0;
    private static final int REQUEST_CLOSE_LOG = 1;
    private static final int DEAL_LOG_STARTED = 2;


    private Queue<LogData> msgQueue = new ConcurrentLinkedQueue<>();
    private LogFileConfig logFileConfig;

    private long maxFileLength;
    private long curWriteFileLength = 0;

    private long maxMemoryLogSize;
    private AtomicLong memoryLogSize = new AtomicLong(0);

    /**
     * 0，表示日志处理已经关闭，并且资源已经释放
     * 1，表示当前请求关闭日志处理
     * 2，当前正常接收处理日志
     */
    private AtomicInteger logDealState = new AtomicInteger(0);

    /**
     * 设置为true则表示采集线程已经完整释放操作
     */
    private AtomicBoolean isStopped = new AtomicBoolean(true);

    /**
     * 构造器
     *
     * @param priority      日志输出优先级
     * @param logFileConfig 指定日志备份文件，当前写文件路径和单个文件最大字节数
     */
    public FileTree(int priority, LogFileConfig logFileConfig) {
        super(priority);

        this.logFileConfig = logFileConfig;
        maxFileLength = logFileConfig.maxLogFileLength;
        maxMemoryLogSize = logFileConfig.maxMemoryLogSize;
        createDirIfNotExists();

        new MsgWriteThread().start();
    }

    @Override
    protected void handleMsg(final LogData logData) {
        //long startTime2 = System.nanoTime();
        //        if (memoryLogSize.get() > maxMemoryLogSize) {
        //            return;
        //        }
        //        //long finishTime2 = System.nanoTime();
        //        //Log.i("Logger", "checkSize Time = " + (finishTime2 - startTime2) / 1000);
        //
        //        //long startTime = System.nanoTime();
        //        // 注意此处是一个估值在Android中String默认采用utf-8,此处大小乘以2倍，实际字符可能没有这么多
        //        logData.dataSize = (logData.tag.length() + logData.msg.length()) << 1;
        //        memoryLogSize.getAndAdd(logData.dataSize);
        //        //long finishTime = System.nanoTime();
        //        //Log.i("Logger", "memoryLogSize Time = " + (finishTime - startTime) / 1000);
        //
        //        //long startTime1 = System.nanoTime();
        //        msgQueue.offer(logData);
        //long finishTime1 = System.nanoTime();
        //Log.i("Logger", "mmsgQueue.offer Time = " + (finishTime1 - startTime1) / 1000);
    }

    @Override
    protected void handleMsg(int priority, String tag, String msg, Throwable tr) {
        LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());

        // 初次耗时约17us，后续可能是4us
        long startTime2 = System.nanoTime();
        if (memoryLogSize.get() > maxMemoryLogSize) {
            return;
        }
        long finishTime2 = System.nanoTime();
        Log.i("Logger", "checkSize Time = " + (finishTime2 - startTime2) / 1000);

        // 初次耗时可能是24us，后续可能是12
        long startTime = System.nanoTime();
        // 注意此处是一个估值在Android中String默认采用utf-8,此处大小乘以2倍，实际字符可能没有这么多
        logData.dataSize = (logData.tag.length() + logData.msg.length()) << 1;
        memoryLogSize.getAndAdd(logData.dataSize);
        long finishTime = System.nanoTime();
        Log.i("Logger", "memoryLogSize Time = " + (finishTime - startTime) / 1000);

        // 初次耗时可能是29，后续可能是18us，如果是连续循坏调用时间接近3us
        long startTime1 = System.nanoTime();
        msgQueue.offer(logData);
        long finishTime1 = System.nanoTime();
        Log.i("Logger", "mmsgQueue.offer Time = " + (finishTime1 - startTime1) / 1000);
    }

    /**
     * 调用该方法，会等待子线程释放完成后，才返回。
     */
    @Override
    protected void release() {
        if (logDealState.get() <= REQUEST_CLOSE_LOG) {
            return;
        }

        logDealState.set(REQUEST_CLOSE_LOG);
        for (; ; ) {
            if (logDealState.get() < REQUEST_CLOSE_LOG) {
                return;
            }
        }
    }

    private void createDirIfNotExists() {
        File dir = new File(logFileConfig.logFileDir);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return;
            }
            throw new IllegalStateException("make dir " + logFileConfig.logFileDir + " fail");
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("logFileDir " + logFileConfig.logFileDir + " must be a dir");
        }
    }

    private class MsgWriteThread extends Thread {
        private FileOutputStream fos = null;

        MsgWriteThread() {
            checkAndCreateCurWriteFile();
        }

        @Override
        public void run() {
            try {
                if (fos == null) {
                    System.out.println("fos is null return and stop the thread");
                    return;
                }

                logDealState.set(DEAL_LOG_STARTED);
                for (; logDealState.get() > REQUEST_CLOSE_LOG; ) {
                    pollMsgAndCheckFileLength();
                    waitForNewMsg();
                }

                closeAndSetMsgStreamNull();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                logDealState.set(DEAL_LOG_CLOSED);
            }
        }

        private void pollMsgAndCheckFileLength() {
            LogData logData;
            byte[] msgBytes;
            while ((logData = msgQueue.poll()) != null) {
                // 需要减去大小
                memoryLogSize.getAndAdd(-logData.dataSize);

                StringBuilder msgBuilder = new StringBuilder(256);

                LogHelper.getLogPrefix(msgBuilder, logData);
                msgBuilder.append(logData.msg)
                          .append(LogHelper.LINE_BREAK);

                if (logData.tr != null) {
                    msgBuilder.append(LogHelper.getStackTraceString(logData.tr));
                    msgBuilder.append(LogHelper.LINE_BREAK);
                }

                msgBytes = msgBuilder.toString().getBytes();
                long length = msgBytes.length;

                try {
                    fos.write(msgBytes);
                    curWriteFileLength += length;
                    if (curWriteFileLength >= logFileConfig.maxLogFileLength) {
                        checkAndCreateCurWriteFile();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }

        private void waitForNewMsg() {
            if (logDealState.get() <= 0) {
                return;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        private void closeAndSetMsgStreamNull() {
            try {
                OutputStream tempOs = fos;
                fos = null;
                if (tempOs != null) {
                    tempOs.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        /**
         * 校正当前要写的文件：
         * 1、判断本地文件“curWriteFile”文件是否存在，不存在则创建文件
         * 2、若存在则判断其大小是否大于等于MAX_FILE_LENGTH，则将fileLength设置为该文件的长度
         * 3、若满足，则将文件重命名为“backupFile”，创建新的文件
         */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void checkAndCreateCurWriteFile() {
            File file = new File(logFileConfig.curWriteFile);
            try {
                if (file.exists()) {
                    if (file.length() >= logFileConfig.maxLogFileLength) {
                        // 先关闭针对当前文件的写流
                        closeAndSetMsgStreamNull();

                        System.out.println("rename the fileName, create new file");
                        File backupFile = new File(logFileConfig.backupFile);
                        backupFile.delete();
                        file.renameTo(backupFile);
                        curWriteFileLength = 0;
                    } else {
                        curWriteFileLength = file.length();
                    }
                } else {
                    curWriteFileLength = 0;
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                curWriteFileLength = 0;
            } finally {
                createMsgStreamIfNull();
            }
        }

        private void createMsgStreamIfNull() {
            if (fos != null) {
                return;
            }

            try {
                fos = new FileOutputStream(logFileConfig.curWriteFile, true);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static class LogFileConfig {
        public String logFileDir;
        /**
         * include the logFileDir + fileName
         */
        public String backupFile;
        public String curWriteFile;
        public long maxLogFileLength;
        public long maxMemoryLogSize;
        public int maxMsgCachedCount;
    }
}
