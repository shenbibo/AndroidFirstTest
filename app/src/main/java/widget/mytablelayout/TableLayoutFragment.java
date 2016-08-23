package widget.mytablelayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.androidfirsttest.R;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TableLayoutFragment extends Fragment implements MyTableLayout.KeyEventListener{

    private  static final String TAG = "TableLayoutFragment";

    private TableLayout tableLayout;

    private int curSelectedIndex = 0;

    private int oldSelectedIndex = 0;

    private Drawable oldDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablelayout, null);
        tableLayout = (TableLayout) view.findViewById(R.id.tablelayout);
//        tableLayout.setFocusable(true);
//        //注意如果注释掉该行，会导致view无法接收到keyEvent
//        tableLayout.setFocusableInTouchMode(true);
//        if(tableLayout.requestFocus())
//        {
//            Log.d(TAG, "tableLayout get the focus");
//        }
//        else
//        {
//            Log.d(TAG, "tableLayout not get the focus");
//        }
//
//
//        tableLayout.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d(TAG, "get the key event");
//                if((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
//                        && event.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    dealKeyCodeRightEvent();
//                    return true;
//                }
//                else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    dealKeyCodeLeftEvent();
//                    return true;
//                }
//                return  false;
//
//            }
//        });
//        oldDrawable = tableLayout.getChildAt(curSelectedIndex).getBackground();
//        tableLayout.getChildAt(curSelectedIndex).setBackgroundColor(Color.RED);

        return view;
    }

    @Override
    public boolean dealKeyDownEvent(ViewGroup view, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dealKeyUpEvent(ViewGroup view, int keyCode, KeyEvent event) {
        return false;
    }

    private boolean dealKeyCodeLeftEvent()
    {
        moveItemIndexAndSetBackground(-1);
        return true;
    }

    private boolean dealKeyCodeRightEvent()
    {
        moveItemIndexAndSetBackground(1);
        return true;
    }

    private boolean dealKeyCodeDownEvent()
    {
        return true;
    }

    private boolean dealKeyCodeUpEvent()
    {
        return true;
    }

    private boolean dealKeyCodeCenterEvent()
    {
        return true;
    }

    private void moveItemIndexAndSetBackground(int addIndex)
    {
        int childCount = tableLayout.getChildCount();
        View tempView = tableLayout.getChildAt(oldSelectedIndex);
        tempView.setBackgroundDrawable(oldDrawable);
        tempView.invalidate();
        if(addIndex > 0 )
        {
            curSelectedIndex = (curSelectedIndex + addIndex > (childCount - 1)) ? (childCount - 1) : (curSelectedIndex + addIndex);
        }
        else
        {
            curSelectedIndex = (curSelectedIndex + addIndex < 0) ? 0 : (curSelectedIndex + addIndex);
        }
        oldSelectedIndex = curSelectedIndex;
        tempView = tableLayout.getChildAt(curSelectedIndex);
        oldDrawable = tempView.getBackground();
        tempView.setBackgroundColor(Color.RED);
        tempView.invalidate();

    }

}
