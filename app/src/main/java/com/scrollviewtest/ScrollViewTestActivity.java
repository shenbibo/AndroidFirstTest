package com.scrollviewtest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfirsttest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一句话注释。
 * <p>
 * 详细内容。
 *
 * @author sky on 2017/7/13
 */

public class ScrollViewTestActivity extends AppCompatActivity {

    @BindView(R.id.text_view1)
    TextView textView1;
    @BindView(R.id.text_view2)
    TextView textView2;
    @BindView(R.id.text_view3)
    TextView textView3;
    @BindView(R.id.text_view4)
    TextView textView4;
    @BindView(R.id.text_view5)
    TextView textView5;
    @BindView(R.id.text_view6)
    TextView textView6;
    @BindView(R.id.text_view7)
    TextView textView7;
    @BindView(R.id.text_view8)
    TextView textView8;
    @BindView(R.id.text_view9)
    TextView textView9;
    @BindView(R.id.text_view10)
    TextView textView10;
    @BindView(R.id.text_view11)
    TextView textView11;
    @BindView(R.id.text_view12)
    TextView textView12;
    @BindView(R.id.text_view13)
    TextView textView13;
    @BindView(R.id.text_view14)
    TextView textView14;
    @BindView(R.id.text_view15)
    TextView textView15;
    @BindView(R.id.text_view16)
    TextView textView16;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollview_test_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text_view1, R.id.text_view2, R.id.text_view3, R.id.text_view4,
            R.id.text_view5, R.id.text_view6, R.id.text_view7, R.id.text_view8,
            R.id.text_view9, R.id.text_view10, R.id.text_view11, R.id.text_view12,
            R.id.text_view13, R.id.text_view14, R.id.text_view15, R.id.text_view16})
    public void onClick(View v) {
        TextView view = (TextView) v;
        Toast.makeText(this, view.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            container.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            container.setOrientation(LinearLayout.VERTICAL);
        }
    }
}
