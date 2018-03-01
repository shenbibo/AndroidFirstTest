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
    private Queue<LogData> msgQueue = new ConcurrentLinkedQueue<>();
    private LogFileParam logFileParam;

    private final long MAX_FILE_LENGTH;
    private long fileLength = 0;

    private final long MAX_MEMORY_LOG_LENGTH;
    private AtomicLong memoryLogLength = new AtomicLong(0);

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
        createDirIfNotExists();

        new MsgWriteThread().start();
    }

    @Override
    protected void handleMsg(final LogData logData) {
        if (memoryLogLength.get() > MAX_MEMORY_LOG_LENGTH) {
            return;
        }

        // 注意此处是一个估值在Android中String默认采用utf-8,此处大小乘以2倍，实际字符可能没有这么多
        logData.dataSize = (logData.tag.length() + logData.msg.length()) << 1;
        memoryLogLength.getAndAdd(logData.dataSize);

        msgQueue.offer(logData);
    }

    @Override
    protected void release() {
        releaseCount.set(0);
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
            LogData logData;
            byte[] msgBytes;
            while ((logData = msgQueue.poll()) != null) {
                // 需要减去大小
                memoryLogLength.getAndAdd(-logData.dataSize);

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
