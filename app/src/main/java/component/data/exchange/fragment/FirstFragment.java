package component.data.exchange.fragment;

import com.example.androidfirsttest.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月17日 下午3:48:55
 * @version
 * @since
 */
public class FirstFragment extends TaskFragment
{

    private EditText inputText;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {        
        super.onCreateView(inflater, container, savedInstanceState);
        //以下两种情况才continer不传入null，
        // 当布局文件的根布局为merge时候，必须要传入container不为null
        // 还有就是如果传入container不为null，则第三个三个参数必须传入false
        //否则会抛出：IllegalStateException("The specified child already has a parent. " +
        //"You must call removeView() on the child's parent first.");
        View contentView = inflater.inflate(R.layout.first_fragment_layout, container, false);
        initView(contentView);
        setViewResponseEvent();
        return contentView;
    }
    
    private void initView(View rootView)
    {
        inputText = (EditText) rootView.findViewById(R.id.inputEditText);
    }
    
    private void setViewResponseEvent()
    {
        inputText.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(listener != null)
                {
                    listener.setStatusValue(s.toString());
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after)
            {
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach()
    {
        
        super.onDetach();
        listener = null;
    }
}
