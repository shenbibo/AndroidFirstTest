package com.widget.test.toolbar.fragment;

import com.example.androidfirsttest.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author sWX284798
 *
 */
public class FirstFragment extends BaseFragment
{
     private TextView  tv;
     private Button   button;


     
     @Override
     public void onCreate(Bundle savedInstanceState)
     {
         super.onCreate(savedInstanceState);
     }
     
     @Override
     public  View onCreateView(LayoutInflater inflater, ViewGroup container,  
             Bundle savedInstanceState)
     {
         View rootView = inflater.inflate(R.layout.first_fragment_layout, null);
         tv = (TextView) rootView.findViewById(R.id.textView1);
         button = (Button) rootView.findViewById(R.id.getList);
         setViewEvents();
         return rootView;
     }
     
     private void setViewEvents()
     {
         
     }
}
