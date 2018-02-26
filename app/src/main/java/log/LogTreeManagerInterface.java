package log;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
public interface LogTreeManagerInterface {
    void addLogTree(LogTree logTree);

    void addLogTrees(LogTree... logTrees);

    void removeLogTree(LogTree logTree);

    void clear();
}
