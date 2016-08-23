package android.text;

import android.util.Log;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年8月21日 上午10:18:07
 * @version
 * @since
 */
public class TextUtils
{
    private static final String TAG = "TextUtils";

    public static boolean isEmpty(CharSequence str) throws NullPointerException{
        Log.d(TAG, "called isEmpty");
        throw new NullPointerException("");
//        if (str == null || str.length() == 0)
//            return true;
//        else
//            return false;
    }
}
