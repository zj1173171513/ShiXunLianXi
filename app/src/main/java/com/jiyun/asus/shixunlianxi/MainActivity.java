package com.jiyun.asus.shixunlianxi;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImg;
    private Button mBut_Main;
    private TextView mDao;
    private int count = 5;
    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count--;
            mDao.setText(count+"");
            if (count == 0){
                Intent intent = new Intent(MainActivity.this,DengActivity.class);
                startActivity(intent);
            }else {
                handler.postDelayed(runnable,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImg, "Rotation", 360);
        animator.setDuration(5000);
        animator.start();

        handler.postDelayed(runnable,1000);
    }

    private void initView() {
        mImg = (ImageView) findViewById(R.id.mImg);
        mBut_Main = (Button) findViewById(R.id.mBut_Main);
        mDao = (TextView) findViewById(R.id.mDao);

        mBut_Main.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBut_Main:
                Intent intent = new Intent(MainActivity.this,DengActivity.class);
                startActivity(intent);
                break;
        }
    }
}
