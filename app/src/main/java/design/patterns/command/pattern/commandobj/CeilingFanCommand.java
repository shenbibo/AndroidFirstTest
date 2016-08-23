package design.patterns.command.pattern.commandobj;

import design.patterns.command.pattern.Command;
import design.patterns.command.pattern.receiver.CeilingFan;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月3日 下午3:04:49
 * @version
 * @since
 */
public class CeilingFanCommand implements Command
{
    private CeilingFan fan;
    
    private int controlType;
    
    private int preControlType;
    
    public static final int DOWN = 0;
    
    public static final int UP = 1;
    
    
    /**
     * 
     */
    public CeilingFanCommand(CeilingFan fan, int controlType)
    {
        this.fan = fan;
        this.controlType = controlType; 
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        preControlType = fan.getSpeed();
        if(controlType == UP)
        {
            fan.setFanStatus(preControlType + 1);
        }
        else
        {
            fan.setFanStatus(preControlType - 1); 
        }
            
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo()
    {
        fan.setFanStatus(preControlType);
    }

}
