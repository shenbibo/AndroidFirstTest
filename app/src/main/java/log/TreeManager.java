package log;


import android.os.Process;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
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

    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();
    private final Queue<LogData> MSG_QUEUE = new ConcurrentLinkedQueue<>();
    private final BlockingQueue<LogData> MSG_QUEUE_2 = new LinkedBlockingQueue<>();
    private final Object LOCK = new Object();

    /**
     * 在内存中日志队列的最大值
     */
    private int maxMemoryLogSize;
    private AtomicBoolean isClearCalled = new AtomicBoolean(false);
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
        if (/*isClearCalled.get() || */isInitCalled.get()) {
            return;
        }
        isInitCalled.set(true);

        this.maxMemoryLogSize = maxMemoryLogSize;
        TREES.add(0, null);
    }

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {
        //        if (TREES.isEmpty()) {
        //            return;
        //        }

        //long startTime = System.nanoTime();
        // new一个对象大约需要87us，使用set方法大概70us
        // LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        // logData.set(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());
        //long finishTime = System.nanoTime();
        //Log.i("Logger", "create logDataTime = " + (finishTime - startTime) / 1000);

        //long startTime2 = System.nanoTime();
        //                for (LogTree tree : TREES) {
        //                    tree.prepareLog(logData);
        //                }
        // for 循环比较耗时
        //        for (LogTree tree : TREES) {
        //            tree.prepareLog(priority, tag, msg, tr);
        //        }
        //        for(int i = 0; i < TREES.size(); i++){
        //            TREES.get(i).prepareLog(logData);
        //        }
        // logcatTree.prepareLog(logData);
        //logcatTree.handleMsg(priority, tag, msg, tr);

        //        for (int i = 0; i < TREES.size(); i++){
        //           TREES.get(i).prepareLog(priority, tag, msg, tr);
        //        }
        //long finishTime2 = System.nanoTime();
        //Log.i("Logger", "loop time = " + (finishTime2 - startTime2) / 1000);

        LogTree logTree = TREES.get(0);
        if (logTree != null) {
            logTree.prepareLog(priority, tag, msg, tr);
        }

        // 只有logcat一个则不需要封装成对象，或者在数据队列中已经到最大值了
        if (TREES.size() == 1 || memoryLogSize.get() > maxMemoryLogSize) {
            return;
        }

        LogData logData = new LogData(System.currentTimeMillis(), priority, tag, msg, tr, Process.myTid());

        logData.dataSize = (logData.tag.length() + logData.msg.length()) << 1;
        memoryLogSize.getAndAdd(logData.dataSize);

        MSG_QUEUE.offer(logData);
        //MSG_QUEUE_2.offer(logData);
    }

    @Override
    public synchronized boolean addLogTree(LogTree logTree) {
        return !(/*isClearCalled.get() || */logTree == null) && addTree(logTree);

    }

    @Override
    public synchronized boolean addLogTrees(LogTree... logTrees) {
        if (/*isClearCalled.get() || */logTrees == null || logTrees.length == 0) {
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
        if (/*isClearCalled.get() ||*/ logTree == null) {
            return false;
        }

        logTree.release();

        boolean removed = removeTree(logTree);

        if (TREES.size() == 1) {
            waitForMsgDispatcherThreadClosed();
        }

        return removed;
    }

    private boolean removeTree(LogTree logTree) {
        if (logTree instanceof LogcatTree) {
            TREES.set(0, null);
            return true;
        }
        return TREES.remove(logTree);
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

    class MsgDispatcherThread extends Thread {

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

    class MsgDispatcherThread2 extends Thread {

        @Override
        public void run() {
            try {

                for (; isThreadStateRunning(); ) {
                    LogData logData = null;
                    try {
                        logData = MSG_QUEUE_2.take();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    if (logData == null) {
                        continue;
                    }

                    //Log.i("LogTest3", logData.msg);

                    memoryLogSize.getAndAdd(-logData.dataSize);

                    for (LogTree logTree : TREES) {
                        if (logTree instanceof LogcatTree) {
                            continue;
                        }

                        logTree.prepareLog(logData);
                    }
                }
            } finally {
                dispatcherThreadState.set(THREAD_CLOSED);
            }
        }

    }
}
