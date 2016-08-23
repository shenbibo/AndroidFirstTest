package design.patterns.command.pattern.commandobj;

import design.patterns.command.pattern.Command;

/**
 * 
 * 一个空的命令类，什么都不做，这样比直接返回null，更好。
 * @author sWX284798
 * @date 2015年11月3日 下午3:49:01
 * @version
 * @since
 */
public class NullCommand implements Command
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo()
    {
    }

}
