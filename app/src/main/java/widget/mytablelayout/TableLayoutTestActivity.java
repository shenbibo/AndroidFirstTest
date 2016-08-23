package widget.mytablelayout;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.example.androidfirsttest.R;

import common.tools.UiHelperUtils;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TableLayoutTestActivity extends AppCompatActivity {

    private static final String TAG = "TableLayoutTestActivity";

    private FragmentManager fm = getSupportFragmentManager();

    private int container = R.id.container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.golable_container_layout);
        Fragment fragment = new TableLayoutFragment();
        UiHelperUtils.showFragment(fm, fragment, container, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "Activity.onKeyDown called, keyCode = " + keyCode + " event = " + event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "Activity.onKeyUp called, keyCode = " + keyCode + " event = " + event.getAction());
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "event.keyCode = " + event.getKeyCode());

        int keyCode = event.getKeyCode();
/*        if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            return false;
        }*/
        return super.dispatchKeyEvent(event);
    }
}
