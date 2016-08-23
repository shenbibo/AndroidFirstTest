package com.widget.test.toolbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.androidfirsttest.R;

/**
 * @author sWX284798
 *
 */
public class SecondFragment extends BaseFragment
{

    
    private CheckBox cb;
    
    private Button button;
    

    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)
    {
        View  rootView = inflater.inflate(R.layout.second_fragment_layout, null);
        cb = (CheckBox) rootView.findViewById(R.id.checkBox1);
        button = (Button) rootView.findViewById(R.id.getList);
        setViewEvents();
        return rootView;
    }
    
    private void setViewEvents()
    {
        
    }
}
