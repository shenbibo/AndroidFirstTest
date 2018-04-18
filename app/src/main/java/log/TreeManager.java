package log;


import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class TreeManager implements LogInterface, LogTreeManagerInterface {

    private static final int THREAD_CLOSED = -2;
    private static final int REQUEST_THREAD_CLOSE = -1;
    private static final int THREAD_RUNNING = 0;
    private LogcatTree logcatTree = new LogcatTree(Logger.VERBOSE);
    private LogData logData = new LogData(0, Logger.INFO, "LogTest", "this is data test", null, 1234);

    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();
    private final Queue<LogData> MSG_QUEUE = new ConcurrentLinkedQueue<>();
    //private final Queue<LogData> MSG_QUEUE = new LinkedList<>();
    private final Queue<LogData> CACHE_QUEUE = new ConcurrentLinkedQueue<>();

    /**
     * 在内存中日志队列的最大值
     */
    private int maxMemoryLogSize;
    private AtomicBoolean isInitCalled = new AtomicBoolean(false);
    private AtomicInteger memoryLogSize = new AtomicInteger(0);
    /**
     * -2，表示未启动或者已经关闭
     * -1，表示请求关闭
     * 0， 表示已经正常启动
     */
    private AtomicInteger dispatcherThreadState = new AtomicInteger(THREAD_CLOSED);

    private Thread msgDispatcherThread;

    public synchronized void init(int maxMemoryLogSize) {
        if (isInitCalled.get()) {
            return;
        }
        isInitCalled.set(true);

        this.maxMemoryLogSize = maxMemoryLogSize;
        TREES.add(0, null);
    }

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {

        //        LogTree logTree = TREES.get(0);
        //        if (logTree != null) {
        //            logTree.prepareLog(priority, tag, msg, tr);
        //        }
        logcatTree.handleMsgOnCalledThread(priority, tag, msg, tr);

        // 只有logcat一个则不需要封装成对象，或者在数据队列中已经到最大值了
        //if (TREES.size() == 1 /*|| memoryLogSize.get() > maxMemoryLogSize*/) {
        //    return;
        //}

        //System.currentTimeMillis()可能需要发费3-4us, Process.myTid可能需要5-6us
        //LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        LogData logData = new LogData(0, priority, tag, msg, tr, 0);
        //使用从队列中获取原来已经生成的logdata对象的方式并不可取，消耗的时间比直接new一个对象还要消耗的多
        //LogData logData = getLogData();
        //logData.set(0, priority, tag, msg, tr, 0);


        //logData.dataSize = (logData.tag.length() + logData.msg.length()) << 1;
        //memoryLogSize.getAndAdd(logData.dataSize);
        // 单线程时执行ConcurrentLinkedQueue.offer需要13-15us
        //MSG_QUEUE.offer(logData);
        //long startTime = System.nanoTime();
        // LinkedList.add大概需要7-8us
        //MSG_QUEUE.add(logData);
        // 单线程时执行ConcurrentLinkedQueue.offer需要12-16us
        MSG_QUEUE.offer(logData);
        //long finishTime = System.nanoTime();
        //Log.i("LogTest", "MSG_QUEUE.offer time = " + (finishTime - startTime) / 1000);
    }

    @Override
    public synchronized boolean addLogTree(LogTree logTree) {
        return !(logTree == null) && addTree(logTree);

    }

    @Override
    public synchronized boolean addLogTrees(LogTree... logTrees) {
        if (logTrees == null || logTrees.length == 0) {
            return false;
        }

        for (LogTree logTree : logTrees) {
            addTree(logTree);
        }

        return true;
    }

    private void createDispatcherThreadAndStartIfNeed() {
        if (TREES.size() != 1 && msgDispatcherThread == null) {
            msgDispatcherThread = new MsgDispatcherThread();
            //msgDispatcherThread = new MsgDispatcherThread2();
            msgDispatcherThread.start();
            dispatcherThreadState.set(THREAD_RUNNING);
        }
    }

    @Override
    public synchronized boolean removeLogTree(LogTree logTree) {
        if (logTree == null) {
            return false;
        }

        logTree.release();

        boolean removed = removeTree(logTree);

        if (TREES.size() == 1) {
            waitForMsgDispatcherThreadClosed();
        }

        return removed;
    }

    /**
     * 当clear方法调用时，其他add remove 方法直接返回false。
     */
    @Override
    public synchronized boolean clearTrees() {
        release();
        TREES.set(0, null);
        // 删除除了第一个元素外的所有元素
        boolean removed = TREES.removeAll(TREES.subList(1, TREES.size() - 1));
        waitForMsgDispatcherThreadClosed();
        return removed;
    }

    /**
     * 获取内存缓存最新日志，注意必要添加LogCacheTree，否则返回null
     */
    List<byte[]> getMemoryCachedMsg() {
        for (LogTree tree : TREES) {
            if (tree instanceof LogCacheTree) {
                return ((LogCacheTree) tree).getMemoryCachedMsg();
            }
        }
        return null;
    }

    private LogData getLogData() {
        LogData logData = CACHE_QUEUE.poll();
        if (logData == null) {
            logData = new LogData();
        }

        return logData;
    }

    private boolean removeTree(LogTree logTree) {
        if (logTree instanceof LogcatTree) {
            TREES.set(0, null);
            return true;
        }
        return TREES.remove(logTree);
    }

    private void release() {
        for (LogTree logTree : TREES) {
            if (logTree != null) {
                logTree.release();
            }
        }
    }

    private boolean addTree(LogTree logTree) {
        if (logTree instanceof LogcatTree) {
            TREES.set(0, logTree);
            return true;
        }

        boolean added = TREES.add(logTree);
        createDispatcherThreadAndStartIfNeed();

        return added;
    }

    private void waitForMsgDispatcherThreadClosed() {
        if (!isThreadStateRunning()) {
            return;
        }

        // 设置为-1，请求关闭线程，一直等待关闭后才退出
        dispatcherThreadState.set(REQUEST_THREAD_CLOSE);
        for (; ; ) {
            if (isThreadStateClosed()) {
                msgDispatcherThread = null;
                return;
            }
        }
    }

    private boolean isThreadStateRunning() {
        return dispatcherThreadState.get() == THREAD_RUNNING;
    }

    private boolean isThreadStateClosed() {
        return dispatcherThreadState.get() == THREAD_CLOSED;
    }

    private class MsgDispatcherThread extends Thread {

        @Override
        public void run() {
            try {
                for (; isThreadStateRunning(); ) {
                    LogData logData = MSG_QUEUE.poll();

                    if (sleepIfMsgNull(logData)) {
                        continue;
                    }

                    memoryLogSize.getAndAdd(-logData.dataSize);

                    dispatchMsg(logData);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                dispatcherThreadState.set(THREAD_CLOSED);
            }
        }

        private boolean sleepIfMsgNull(LogData logData) {
            if (logData == null) {
                try {
                    Thread.sleep(10);
                    return true;
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                    return true;
                }
            }
            return false;
        }

        private void dispatchMsg(LogData logData) {
            for (LogTree logTree : TREES) {
                if (logTree instanceof LogcatTree) {
                    continue;
                }

                logTree.prepareLog(logData);
            }
        }
    }
}
