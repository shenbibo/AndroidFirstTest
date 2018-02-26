package log;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
final class TreeManager implements LogInterface, LogTreeManagerInterface {
    private final CopyOnWriteArrayList<LogTree> TREES = new CopyOnWriteArrayList<>();

    @Override
    public void handleMsg(int priority, String tag, String msg, Throwable tr) {
        String time = null;
        for (LogTree tree : TREES) {
            if(tree instanceof LogcatTree){
                tree.prepareLog(priority, tag, msg, tr);
                continue;
            }

            if(time == null){
                time = LogHelper.formatCurTime();
            }

            tree.handleMsgWithTime(time, priority, tag, msg, tr);
        }
    }

    @Override
    public void addLogTree(LogTree logTree) {
        TREES.add(logTree);
    }

    @Override
    public void addLogTrees(LogTree... logTrees) {
        for (LogTree logTree : logTrees) {
            addLogTree(logTree);
        }
    }

    @Override
    public void removeLogTree(LogTree logTree) {
        TREES.remove(logTree);
    }

    @Override
    public void clear() {
        TREES.clear();
    }
}
