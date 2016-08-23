package design.patterns.command.pattern.receiver;

import design.patterns.command.pattern.CommandPatternTest;
import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年11月3日 下午2:42:54
 * @version
 * @since
 */
public class Light
{
    private String place;
    
    private int currentStatus = 0;
    
    public Light(String place)
    {
        this.place = place;
    }
    
    public void turnOn()
    {
        Log.d(CommandPatternTest.TAG, place + " Light is turnOn");
        currentStatus = 1;
    }
    
    public void turnOff()
    {
        Log.d(CommandPatternTest.TAG, place + " Light is turnOff");
        currentStatus = 0;
    }
    
    public int getCurrentStatus()
    {
        return currentStatus;
    }
}
