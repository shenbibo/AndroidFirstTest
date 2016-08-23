package widget.mytablelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;


/**
 * Created by Administrator on 2016/4/13.
 */
public class MyTableLayout extends TableLayout {

    private static final String TAG = "MyTableLayout";

    public interface KeyEventListener{

        boolean dealKeyDownEvent(ViewGroup view, int keyCode, KeyEvent event);
        boolean dealKeyUpEvent(ViewGroup view, int keyCode, KeyEvent event);
    }


    private KeyEventListener listener;

    public void setKeyEventListener(KeyEventListener listener)
    {
        this.listener = listener;
    }


    public MyTableLayout(Context context) {
        super(context);
    }

    public MyTableLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "onKeyDown called, keyCode = " + keyCode + "event = " + event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp called, keyCode = " + keyCode + "event = " + event.getAction());
        return super.onKeyUp(keyCode, event);
    }
}
