package log;

import java.util.List;

/**
 * 日志框架类
 *
 * @author sky on 2018/2/26
 */
public final class Logger {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method；use Log.wtf.
     */
    public static final int ASSERT = 7;

    //private static LogImpl logImpl;

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void verbose(String tag, String msg) {
        println(VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void verbose(String tag, String msg, Throwable tr) {
        println(VERBOSE, tag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void debug(String tag, String msg) {
        println(DEBUG, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void debug(String tag, String msg, Throwable tr) {
        println(DEBUG, tag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void info(String tag, String msg) {
        println(INFO, tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void info(String tag, String msg, Throwable tr) {
        println(INFO, tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void warn(String tag, String msg) {
        println(WARN, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void warn(String tag, String msg, Throwable tr) {
        println(WARN, tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static void warn(String tag, Throwable tr) {
        println(WARN, tag, "", tr);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void error(String tag, String msg) {
        println(ERROR, tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void error(String tag, String msg, Throwable tr) {
        println(ERROR, tag, msg, tr);
    }

    /**
     * 打印日志
     */
    public static void println(int priority, String tag, String msg, Throwable tr) {
        //long startTime = System.nanoTime();
        //logImpl.handleMsg(priority, tag, msg, tr);
        //Log.println(priority, tag, msg);
        //long finishTime = System.nanoTime();
        //Log.i("LogTest", "execute time = " + (finishTime - startTime) / 1000);
        treeManager.handleMsg(priority, tag, msg, tr);
    }

    /**
     * 打印日志2
     */
    public static void println(int priority, String tag, String msg) {
        //logImpl.handleMsg(priority, tag, msg);
        treeManager.handleMsg(priority, tag, msg);
    }

    private static TreeManager treeManager;

    /**
     * 初始化
     */
    public static void init(int maxMemoryLogCount, LogTree... logTrees) {
        //        if (logImpl == null) {
        //            logImpl = new LogImpl();
        //            logImpl.init(maxMemoryLogCount);
        //            logImpl.addLogTrees(logTrees);
        //        }
        if (treeManager == null) {
            treeManager = new TreeManager(maxMemoryLogCount);
            treeManager.addLogTrees(logTrees);
        }
    }

    /**
     * 获取内存缓存最新日志，注意必要添加LogCacheTree，并且LogCacheConfig.maxLogMemoryCacheSize > 0, 否则返回空列表
     */
    public static List<byte[]> getMemoryCachedMsg() {
        //return logImpl.getMemoryCachedMsg();
        return treeManager.getMemoryCachedMsg();
    }

    /**
     * 获取日志辅助管理对象
     */
    public static LogTreeManager getLogManager() {
        return treeManager;
    }

    //    /**
    //     * 日志辅助管理对象
    //     */
    //    public static final class LogImpl implements LogTreeManager, HandleLog {
    //        private TreeManager treeManager = new TreeManager();
    //
    //        public void init(int maxMemoryLogSize) {
    //            treeManager.init(maxMemoryLogSize);
    //        }
    //
    //        @Override
    //        public boolean addLogTree(LogTree logTree) {
    //            return treeManager.addLogTree(logTree);
    //        }
    //
    //        @Override
    //        public boolean addLogTrees(LogTree... logTrees) {
    //            return treeManager.addLogTrees(logTrees);
    //        }
    //
    //        @Override
    //        public boolean removeLogTree(LogTree logTree) {
    //            return treeManager.removeLogTree(logTree);
    //        }
    //
    //        @Override
    //        public boolean clearTrees() {
    //            return treeManager.clearTrees();
    //        }
    //
    //        @Override
    //        public void handleMsg(int priority, String tag, String msg) {
    //            treeManager.handleMsg(priority, tag, msg);
    //        }
    //
    //        @Override
    //        public void handleMsg(int priority, String tag, String msg, Throwable tr) {
    //            treeManager.handleMsg(priority, tag, msg, tr);
    //        }
    //
    //        List<byte[]> getMemoryCachedMsg() {
    //            return treeManager.getMemoryCachedMsg();
    //        }
    //    }
}
