package com.widget.test.toolbar.fragment;

import com.example.androidfirsttest.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author sWX284798
 *
 */
public class ThirdFragment extends BaseFragment
{
    
    private Button button1;
    private Button button2;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.third_fragment_layout, null);
        button1 = (Button) rootView.findViewById(R.id.getList);
        button2 = (Button) rootView.findViewById(R.id.addStudent);
        setViewEvents();
        return rootView;
    }
    
    private void setViewEvents()
    {
        
    }
}
