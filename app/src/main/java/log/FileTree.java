package log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class FileTree extends LogTree {
    //    /**
    //     * 本地化日志文件每个最大字符数
    //     */
    //    private static final int MAX_LENGTH = 1024 * 1024;
    //
    //    /**
    //     * 最大内存日志缓存大小
    //     */
    //
    //    private static final String LOG_DIR =
    //        Environment.getExternalStorageDirectory() + File.separator + "HstLogs";
    //
    //    private static final String LOG_FILE_NAME_1 = "applogs_1.txt";
    //
    //    private static final String LOG_FILE_NAME_0 = "applogs_0.txt";

    private final Object LOCK = new Object();
    //    private Queue<String> msgQueue = new ConcurrentLinkedQueue<>();
    private Queue<LogData> msgQueue = new ConcurrentLinkedQueue<>();
    private LogFileParam logFileParam;

    private final long MAX_FILE_LENGTH;
    private long fileLength = 0;

    private final long MAX_MEMORY_LOG_LENGTH;
    private AtomicLong memoryLogLength = new AtomicLong(0);
    private final int MAX_MSG_COUNT;
    private AtomicInteger msgCount = new AtomicInteger(0);

    /**
     * 变焦对输出流进行释放：
     * 设置为 1，表示正常打印日志，
     * 设置为 0，则表示需要停止日志打印
     * 设置为-1，则表示输出流已经关闭完成
     */
    private AtomicInteger releaseCount = new AtomicInteger(0);

    /**
     * 构造器
     *
     * @param priority     日志输出优先级
     * @param logFileParam 指定日志备份文件，当前写文件路径和单个文件最大字节数
     */
    public FileTree(int priority, LogFileParam logFileParam) {
        super(priority);

        this.logFileParam = logFileParam;
        MAX_FILE_LENGTH = logFileParam.maxFileLength;
        MAX_MEMORY_LOG_LENGTH = logFileParam.maxMemoryLogLength;
        MAX_MSG_COUNT = logFileParam.maxMsgCachedCount;
        createDirIfNotExists();

        new MsgWriteThread().start();
    }

    //    /**
    //     * 2018-02-26 16:53:25.123 D/Tag(pid-tid)：msg + '\n' + LogHelper.getStackTraceString(Throwable tr)
    //     * // TODO 需要测试这一步对时间消耗和包装一个对象分别赋值日志数据，到子线程中再进行组装
    //     */
    //    @Override
    //    public void handleMsg(String logPrefix, int priority, String tag, String msg, Throwable tr) {
    //        long starTime1 = System.nanoTime();
    //        // 如果缓存的长度已经大于最大长度则直接忽略掉这条日志
    //        if (memoryLogLength.get() > MAX_MEMORY_LOG_LENGTH) {
    //            System.out.println("memory cache out of range, drop this msg = " + msg);
    //            return;
    //        }
    //
    //        StringBuilder msgBuilder = new StringBuilder(256);
    //
    //        msgBuilder.append(logPrefix)
    //                  .append(msg)
    //                  .append(LogHelper.LINE_BREAK);
    //
    //        if (tr != null) {
    //            msgBuilder.append(LogHelper.getStackTraceString(tr));
    //            msgBuilder.append(LogHelper.LINE_BREAK);
    //        }
    //        long finishTime1 = System.nanoTime();
    //        Log.i("TimeTest", "stringBuilder time = " + (finishTime1 - starTime1) / 1000 + "us");
    //
    //        long starTime2 = System.nanoTime();
    //        String msgTemp = msgBuilder.toString();
    //        memoryLogLength.getAndAdd(msgTemp.getBytes().length);
    //        long finishTime2 = System.nanoTime();
    //        Log.i("TimeTest", "toStringAndGetBytesTime time = " + (finishTime2 - starTime2) / 1000 + "us");
    //
    //        long starTime3 = System.nanoTime();
    //        msgQueue.offer(msgTemp);
    //        long finishTime3 = System.nanoTime();
    //        Log.i("TimeTest", "msgQueue.offer time = " + (finishTime1 - starTime1) / 1000 + "us");
    //        //        synchronized (LOCK) {
    //        //            LOCK.notify();
    //        //        }
    //
    //    }

    @Override
    protected void handleMsg(final LogData logData) {
        if (msgCount.get() > MAX_MSG_COUNT) {
            return;
        }

        msgCount.getAndIncrement();

        msgQueue.offer(logData);
    }

    @Override
    protected void release() {
        releaseCount.set(0);
        //        synchronized (LOCK) {
        //            LOCK.notify();
        //        }
        for (; ; ) {
            if (releaseCount.get() < 0) {
                return;
            }
        }
    }

    private void createDirIfNotExists() {
        File dir = new File(logFileParam.logFilePath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return;
            }
            throw new IllegalStateException("make dir " + logFileParam.logFilePath + " fail");
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("logFilePath " + logFileParam.logFilePath + " must be a dir");
        }
    }

    private class MsgWriteThread extends Thread {
        private FileOutputStream fos = null;

        MsgWriteThread() {
            checkAndCreateCurWriteFile();
        }

        @Override
        public void run() {
            if (fos == null) {
                System.out.println("fos is null return and stop the thread");
                return;
            }

            releaseCount.set(1);
            for (; releaseCount.get() > 0; ) {
                pollMsgAndCheckFileLength();
                waitForNewMsg();
            }

            closeAndSetMsgStreamNull();
            releaseCount.set(-1);
        }

        private void pollMsgAndCheckFileLength() {
            //                        String msg;
            //                        byte[] msgBytes;
            //                        while ((msg = msgQueue.poll()) != null) {
            //                            msgBytes = msg.getBytes();
            //                            long length = msgBytes.length;
            //                            // 移除出一条日志就从内存缓存中减去该长度
            //                            memoryLogLength.getAndAdd(-length);
            //                            try {
            //                                fos.write(msgBytes);
            //                                fileLength += length;
            //                                if (fileLength >= MAX_FILE_LENGTH) {
            //                                    checkAndCreateCurWriteFile();
            //                                }
            //                            } catch (IOException exception) {
            //                                exception.printStackTrace();
            //                            }
            //                        }
            LogData logData;
            byte[] msgBytes;
            while ((logData = msgQueue.poll()) != null) {
                msgCount.getAndDecrement();

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
                    fileLength += length;
                    if (fileLength >= MAX_FILE_LENGTH) {
                        checkAndCreateCurWriteFile();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }

        private void waitForNewMsg() {
            if (releaseCount.get() <= 0) {
                return;
            }

            //            // 若没有要处理的日志则挂起该线程
            //            synchronized (LOCK) {
            //                try {
            //                    LOCK.wait();
            //                } catch (InterruptedException exception) {
            //                    exception.printStackTrace();
            //                }
            //            }

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
            File file = new File(logFileParam.curWriteFile);
            try {
                if (file.exists()) {
                    if (file.length() >= MAX_FILE_LENGTH) {
                        // 先关闭针对当前文件的写流
                        closeAndSetMsgStreamNull();

                        System.out.println("rename the fileName, create new file");
                        File backupFile = new File(logFileParam.backupFile);
                        backupFile.delete();
                        file.renameTo(backupFile);
                        fileLength = 0;
                    } else {
                        fileLength = file.length();
                    }
                } else {
                    fileLength = 0;
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                fileLength = 0;
            } finally {
                createMsgStreamIfNull();
            }
        }

        private void createMsgStreamIfNull() {
            if (fos != null) {
                return;
            }

            try {
                fos = new FileOutputStream(logFileParam.curWriteFile, true);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static class LogFileParam {
        /**
         * this must end with File.sperator
         */
        public String logFilePath;
        /**
         * include the logFilePath + fileName
         */
        public String backupFile;
        public String curWriteFile;
        public long maxFileLength;
        public long maxMemoryLogLength;
        public int maxMsgCachedCount;
    }
}
