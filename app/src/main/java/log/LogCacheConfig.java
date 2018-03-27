package log;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/3/27
 */
public class LogCacheConfig {
    /**
     * 日志文件的存放路径，如果为空，则表示不缓存到本地磁盘中
     * */
    public String logFileDir;
    /**
     * include the logFileDir + fileName
     */
    public String backupFile;
    public String curWriteFile;
    public long maxLogFileLength;

    /**
     * 缓存到最新内存中的日志大小，如果为0，则表示不缓存在内存中
     * */
    public int maxLogMemoryCacheSize;
}
