package design.patterns.command.pattern.commandobj;

import design.patterns.command.pattern.Command;

/**
 * 
 * 批量命令执行
 * @author sWX284798
 * @date 2015年11月3日 下午4:30:38
 * @version
 * @since
 */
public class MacroCommand implements Command
{
    private Command[] commands;

    /**
     * 
     */
    public MacroCommand(Command[] cs)
    {
        if(cs == null)
        {
            commands = new Command[0]; 
        }
        else
        {
            commands = cs;
        }        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        for(Command c : commands)
        {
            c.execute();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo()
    {
        for (Command command : commands)
        {
            command.undo();            
        }
    }

}
