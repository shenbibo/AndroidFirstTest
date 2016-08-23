package component.data.exchange.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.androidfirsttest.R;
import component.data.exchange.activity.BaseFragmentActivity.IGetStatusValue;

/**
 * 
 * 
 * @author sWX284798
 * @date 2015年12月17日 下午3:49:21
 * @version
 * @since
 */
public class SecondFragment extends TaskFragment implements IGetStatusValue
{

    private EditText outputText;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {        
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = inflater.inflate(R.layout.second_fragment, null);
        initView(contentView);
        setViewResponseEvent();
        return contentView;
    }
    
    private void initView(View rootView)
    {
        outputText = (EditText) rootView.findViewById(R.id.outputEditText);
    }
    
    private void setViewResponseEvent()
    {
//        outputText.addTextChangedListener(new TextWatcher()
//        {
//            
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if(listener != null)
//                {
//                    listener.setStatusValue(s.toString());
//                }
//            }
//            
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                    int after)
//            {
//            }
//            
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//            }
//        });
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void putValue(String value)
    {
        outputText.setText(value);
    }
}
