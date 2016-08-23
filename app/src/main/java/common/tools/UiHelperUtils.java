package common.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2016/4/13.
 */
public class UiHelperUtils {

    public static Fragment showFragment(FragmentManager fm, Fragment fragment, int containerId, String tag)
    {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment tempFragment = null;
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
            ft.replace(containerId, fragment, tag);
            //ft.add(containerId, fragment, tag);
        }
        ft.commit();
        return fragment;
    }
}
