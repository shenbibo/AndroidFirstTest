package com.camera.test;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.example.androidfirsttest.R;

/**
 * com.camera.test
 * [function]
 * [detail]
 * Created by Sky on 2016/6/1.
 * modify by
 */
public class CameraTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button scaleBtn;
    private Button alphaBtn;
    private CheckedTextView checkedTextView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        scaleBtn = (Button) findViewById(R.id.scaleBtn);
        alphaBtn = (Button) findViewById(R.id.alphaBtn);
        checkedTextView1 = (CheckedTextView) findViewById(R.id.checkedTextView1);

        scaleBtn.setOnClickListener(this);
        alphaBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.scaleBtn:
            openCamera();
            break;
        case R.id.alphaBtn:

            break;
        }
    }

    private Camera mCamera;

    /**
     * 打开摄像头
     */
    private void openCamera(){
        Log.i("CameraTestActivity", "open the camera ,count = " + Camera.getNumberOfCameras());
                if (Camera.getNumberOfCameras() < 2) { // 只有一个
                    mCamera = Camera.open(0);
                } else { // 2个
                    mCamera = Camera.open(0);
                }
    }
}
