package component.data.exchange.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月17日 下午2:54:17
 * @version
 * @since
 */
public class TaskFragment extends Fragment
{

    public interface IStatusChangeListener
    {
        void setStatusValue(String value);

    }
    
    protected IStatusChangeListener listener;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Activity activity)
    {
        
        super.onAttach(activity);
        if(activity instanceof IStatusChangeListener)
        {
            listener = (IStatusChangeListener) activity;
        }
    }
    
    public void showFragment(FragmentManager fm, int containerId, String tag)
    {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment tempFragment = null;;        
        if(tag != null)
        {
            tempFragment = fm.findFragmentByTag(tag);
        }
        
        if(tempFragment != null)
        {
            ft.show(tempFragment);
            //
        }
        else
        {
            ft.replace(containerId, this, tag);
            
            //ft.add(containerId, fragment, tag);
        }
        ft.commitAllowingStateLoss();
    }
    
    
}
