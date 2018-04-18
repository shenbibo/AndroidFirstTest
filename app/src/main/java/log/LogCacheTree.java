package log;


import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 日志缓存树，可以缓存最新大小的日志到内存或者到本地
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class LogCacheTree extends LogTree {

    private long curWriteFileLength = 0;
    private LogCacheConfig logFileConfig;

    private FileOutputStream fos = null;

    private int curMsgCacheSize = 0;
    private Queue<byte[]> msgCacheQueue;


    /**
     * 构造器
     *
     * @param priority      日志输出优先级
     * @param logFileConfig 指定日志备份文件，当前写文件路径和单个文件最大字节数
     */
    public LogCacheTree(int priority, LogCacheConfig logFileConfig) {
        super(priority);

        this.logFileConfig = logFileConfig;

        createDirIfNeed();
        checkAndCreateCurWriteFileIfNeed();
        createMsgCacheQueueIfNeed();
    }

    @Override
    protected void handleMsgOnSubThread(final LogData logData) {
        if (isReleaseCalled()) {
            return;
        }

        onMsgAndCheckFileLength(logData);
    }

    /**
     * 调用该方法，会等待子线程释放完成后，才返回。
     */
    @Override
    protected void release() {
        super.release();
        closeAndSetMsgStreamNull();
    }

    private void createMsgCacheQueueIfNeed() {
        if (isMsgMemoryCacheDisable()) {
            return;
        }

        msgCacheQueue = new ConcurrentLinkedQueue<>();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDirIfNeed() {
        if (isFileCacheDisable()) {
            return;
        }

        File dir = new File(logFileConfig.logFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("logFileDir " + logFileConfig.logFileDir + " must be a dir");
        }
    }

    private boolean isMsgMemoryCacheDisable() {
        return logFileConfig.maxLogMemoryCacheSize < 1;
    }

    private boolean isFileCacheDisable() {
        return logFileConfig.logFileDir == null;
    }

    private void onMsgAndCheckFileLength(LogData logData) {
        if (isFileCacheDisable() && isMsgMemoryCacheDisable()) {
            return;
        }

        StringBuilder msgBuilder = buildMsg(logData);
        byte[] msgBytes = msgBuilder.toString().getBytes();
        long length = msgBytes.length;

        writeLogToFileIfNeed(msgBytes, length);
        writeLogToMemoryCacheIfNeed(msgBytes, length);
    }

    private void writeLogToMemoryCacheIfNeed(byte[] msgBytes, long length) {
        if (isMsgMemoryCacheDisable()) {
            return;
        }

        // 循环确定是否队列的数据已经满了，如果没有满则直接添加，否则移除队头数据，知道满足要求
        for (; ; ) {
            if (curMsgCacheSize + length > logFileConfig.maxLogMemoryCacheSize) {
                byte[] bytes = msgCacheQueue.poll();
                if (bytes != null) {
                    curMsgCacheSize -= bytes.length;
                    continue;
                }
                // this could not happen
                break;
            }

            msgCacheQueue.offer(msgBytes);
            curMsgCacheSize += length;
            break;
        }
    }

    /**
     * 获取缓存的最新的在内存中的日志，返回一个日志列表，日志从前往后
     */
    List<byte[]> getMemoryCachedMsg() {
        if (msgCacheQueue == null) {
            return null;
        }

        return new ArrayList<>(msgCacheQueue);
    }

    private void writeLogToFileIfNeed(byte[] msgBytes, long length) {
        if (isFileCacheDisable()) {
            return;
        }

        try {
            fos.write(msgBytes);
            curWriteFileLength += length;
            if (isCurFileSizeExceed()) {
                checkAndCreateCurWriteFileIfNeed();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private boolean isCurFileSizeExceed() {
        return curWriteFileLength >= logFileConfig.maxLogFileLength;
    }

    @NonNull
    private StringBuilder buildMsg(LogData logData) {
        StringBuilder msgBuilder = new StringBuilder(256);

        LogHelper.getLogPrefix(msgBuilder, logData);
        msgBuilder.append(logData.msg)
                  .append(LogHelper.LINE_BREAK);
        if (logData.tr != null) {
            msgBuilder.append(LogHelper.getStackTraceString(logData.tr));
            msgBuilder.append(LogHelper.LINE_BREAK);
        }
        return msgBuilder;
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
    private void checkAndCreateCurWriteFileIfNeed() {
        if (isFileCacheDisable()) {
            return;
        }

        File file = new File(logFileConfig.curWriteFile);
        try {
            if (!file.exists()) {
                curWriteFileLength = 0;
                return;
            }

            if (!isCurFileSizeExceed()) {
                curWriteFileLength = file.length();
                return;
            }

            // 先关闭针对当前文件的写流
            closeAndSetMsgStreamNull();
            File backupFile = new File(logFileConfig.backupFile);
            backupFile.delete();
            file.renameTo(backupFile);
            curWriteFileLength = 0;
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
