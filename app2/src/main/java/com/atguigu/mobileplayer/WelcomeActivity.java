package com.atguigu.mobileplayer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //延迟两秒进入主页面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startMainActivity();

            }
        },2000);
    }
    private boolean isEnterMainActivity = false;
    //设置触摸事件
    private void startMainActivity() {
        if(!isEnterMainActivity){
            isEnterMainActivity = true;
            //进入主页面
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            //关闭启动页面（SplashActivity）
            finish();
        }

    }

    //只要触摸屏幕就直接进入主页面
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //把延迟进入主页面的消息给移除
        handler.removeCallbacksAndMessages(null);
    }
}
