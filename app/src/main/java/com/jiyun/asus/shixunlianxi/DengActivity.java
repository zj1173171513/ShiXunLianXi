package com.jiyun.asus.shixunlianxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class DengActivity extends AppCompatActivity implements View.OnClickListener, UMAuthListener {

    private ImageView mImg_Tou;
    private TextView mDeng_Text;
    private Button mDeng_Zhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deng);
        initView();
    }

    private void initView() {
        mImg_Tou = (ImageView) findViewById(R.id.mImg_Tou);
        mDeng_Text = (TextView) findViewById(R.id.mDeng_Text);
        mDeng_Zhu = (Button) findViewById(R.id.mDeng_Zhu);

        mDeng_Zhu.setOnClickListener(this);
        mDeng_Text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mDeng_Zhu:
                Intent in = new Intent(DengActivity.this,DataActivity.class);
                startActivity(in);
                break;
            case R.id.mDeng_Text:
                UMShareAPI.get(this).getPlatformInfo(DengActivity.this, SHARE_MEDIA.QQ, this);
                break;
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        String profile_image_url = map.get("profile_image_url");
//        Log.e("TAG",profile_image_url);
//        Toast.makeText(this, ""+profile_image_url, Toast.LENGTH_SHORT).show();
        Glide.with(this).load(profile_image_url).into(mImg_Tou);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
