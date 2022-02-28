package com.example.tts;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private Button begin;
    private TextView safe;
    private ImageView dun;
    private TextView cheliu;
    private TextView renliu;
    private TextView max;

    private int renliu1;
    private int cheliu1;
    private int max1;

    private Animation scaleAnimation;

    //计时所用变量
    private int seconds=0;
    private Handler handler;

    private Boolean doing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpeechUtils.getInstance(MainActivity.this).speakText("开机");
        scaleAnimation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale);
        setContentView(R.layout.activity_main);
        begin=(Button)findViewById(R.id.begin);
        safe=(TextView)findViewById(R.id.safe);
        dun=(ImageView)findViewById(R.id.dun);
        cheliu=(TextView)findViewById(R.id.cheliu);
        renliu=(TextView)findViewById(R.id.renliu);
        max=(TextView)findViewById(R.id.max);

        dun.setImageResource(R.drawable.unknow);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doing==false){
                    doing=true;
                    begin();
                    SpeechUtils.getInstance(MainActivity.this).speakText("检测启动");
                }else{
                    doing=false;
                    stop();
                    SpeechUtils.getInstance(MainActivity.this).speakText("检测关闭");
                }
            }
        });
    }
    private void begin(){
        begin.setText("关闭检测");
        check();
        dun.startAnimation(scaleAnimation);
        scaleAnimation.setRepeatCount(Animation.INFINITE);

    }

    private void stop(){
        begin.setText("启动检测");
        scaleAnimation.setRepeatCount(0);
        stopTimer();
        dun.setImageResource(R.drawable.unknow);
        safe.setText("检测未开启");
        safe.setTextColor(Color.parseColor("#707070"));

        renliu.setText("人流量：0");
        cheliu.setText("车流量：0");
        max.setText("周围最大速度：0 km/h");
    }

    private void check(){
        runTimer();
    }

    //计时器开始计时
    private void runTimer(){
        handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                seconds++;
                Random r=new Random();


                String ren="人流量：";
                String che="车流量：";
                String m="周围最大速度：";

                renliu1=r.nextInt(20);
                cheliu1=r.nextInt(15);
                max1=r.nextInt(40);
                max1+=40;


                ren+=String.valueOf(renliu1);
                che+=String.valueOf(cheliu1);
                m+=String.valueOf(max1);
                m+=" km/h";

                if (doing){
                    renliu.setText(ren);
                    cheliu.setText(che);
                    max.setText(m);
                    if(max1>70){
                        dun.setImageResource(R.drawable.unsafe);
                        safe.setText("不安全");
                        safe.setTextColor(Color.parseColor("#D81F08"));
                        SpeechUtils.getInstance(MainActivity.this).speakText("周围车速过快，请小心驾驶");
                    }
                    else if(cheliu1>10){
                        safe.setText("不安全");
                        safe.setTextColor(Color.parseColor("#D81F08"));
                        dun.setImageResource(R.drawable.dangerche);
                        SpeechUtils.getInstance(MainActivity.this).speakText("注意前方车辆");
                    }
                    else if(renliu1>15){
                        safe.setText("不安全");
                        safe.setTextColor(Color.parseColor("#D81F08"));
                        dun.setImageResource(R.drawable.dangerren);
                        SpeechUtils.getInstance(MainActivity.this).speakText("注意前方行人");
                    }else{
                        dun.setImageResource(R.drawable.safe);
                        safe.setText("安全");
                        safe.setTextColor(Color.parseColor("#8BD600"));
                    }
                }

                System.out.println(seconds);
                if(!doing){
                    seconds=0;
                    handler=null;
                }
                if(doing){
                    handler.postDelayed(this,3000);
                }
            }
        });
    }

    //计时器停止计时
    private void stopTimer(){
        seconds=0;
        handler=null;
    }
}