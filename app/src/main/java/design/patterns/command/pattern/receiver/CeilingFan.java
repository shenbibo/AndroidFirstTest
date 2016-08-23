package design.patterns.command.pattern.receiver;

import design.patterns.command.pattern.CommandPatternTest;
import android.util.Log;

/**
 * 吊扇
 * 
 * @author sWX284798
 * @date 2015年11月3日 下午2:52:18
 * @version
 * @since
 */
public class CeilingFan
{

    public final static int HIGH = 3;
    
    public final static int MEDIUM = 2;
    
    public final static int LOW = 1;
    
    public final static int OFF = 0;
    
    private int currentSpeed = OFF;
    
    private String location;
    
    /**
     * 
     */
    public CeilingFan(String location)
    {
        this.location = location;
    }
    
    public void setFanStatus(int status)
    {
        currentSpeed = (status > HIGH || status < OFF) ? OFF : status;
        Log.d(CommandPatternTest.TAG, location + " CeilingFan have set cuttentSpeed = " + currentSpeed);
    }
    
    public int getSpeed()
    {
        return currentSpeed;
    }
    
}
