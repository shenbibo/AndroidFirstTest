package log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import log.FileTree.LogFileConfig;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public class FileTree2 extends LogTree {

    private long curWriteFileLength = 0;
    private LogFileConfig logFileConfig;
    private FileOutputStream fos = null;

    /**
     * 构造器
     *
     * @param priority      日志输出优先级
     * @param logFileConfig 指定日志备份文件，当前写文件路径和单个文件最大字节数
     */
    public FileTree2(int priority, LogFileConfig logFileConfig) {
        super(priority);

        this.logFileConfig = logFileConfig;
        createDirIfNotExists();
        checkAndCreateCurWriteFile();
    }

    @Override
    protected void handleMsg(final LogData logData) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDirIfNotExists() {
        File dir = new File(logFileConfig.logFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("logFileDir " + logFileConfig.logFileDir + " must be a dir");
        }
    }

    private void onMsgAndCheckFileLength(LogData logData) {
        StringBuilder msgBuilder = new StringBuilder(256);

        LogHelper.getLogPrefix(msgBuilder, logData);
        msgBuilder.append(logData.msg)
                  .append(LogHelper.LINE_BREAK);
        if (logData.tr != null) {
            msgBuilder.append(LogHelper.getStackTraceString(logData.tr));
            msgBuilder.append(LogHelper.LINE_BREAK);
        }

        byte[] msgBytes = msgBuilder.toString().getBytes();
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
            if (!file.exists()) {
                curWriteFileLength = 0;
                return;
            }

            if (file.length() < logFileConfig.maxLogFileLength) {
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
