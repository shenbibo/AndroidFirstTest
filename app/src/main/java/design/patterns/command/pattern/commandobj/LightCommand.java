package design.patterns.command.pattern.commandobj;

import design.patterns.command.pattern.Command;
import design.patterns.command.pattern.receiver.Light;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月3日 下午3:15:21
 * @version
 * @since
 */
public class LightCommand implements Command
{
    
    private Light light;
    
    private boolean lightStatus;
    
    private int preStatus;
    
    /**
     * 
     */
    public LightCommand(Light light, boolean wantedStatus)
    {
        this.light = light;
        lightStatus = wantedStatus;
    }
    
    /**
     * @hide
     * */
    
    public void setLightStatus(boolean wantedStatus)
    {
        lightStatus = wantedStatus;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        preStatus = light.getCurrentStatus();
        if(lightStatus && preStatus <= 0)
        {           
            light.turnOn();
        }
        else if(!lightStatus && preStatus > 0)
        {
            light.turnOff();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undo()
    {
        if(preStatus > 0)
        {
            light.turnOn();
        }
        else
        {
            light.turnOff();
        }
    }

}
