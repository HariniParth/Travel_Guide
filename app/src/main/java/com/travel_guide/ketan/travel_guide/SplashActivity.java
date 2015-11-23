package com.travel_guide.ketan.travel_guide;

/**
 * Created by Harini on 11/7/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends Activity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView t1=(TextView)findViewById(R.id.text_1);
        TextView t2=(TextView)findViewById(R.id.text_2);
        TextView t3=(TextView)findViewById(R.id.text_3);
        TextView t4=(TextView)findViewById(R.id.text_4);
        showDelayed(t1, 1000);
        showDelayed(t2, 3000);
        showDelayed(t3, 5000);
        showDelayed(t4, 7000);

        ImageView img_1 = (ImageView) findViewById(R.id.snow_1);
        ImageView img_2 = (ImageView) findViewById(R.id.snow_2);
        ImageView img_3 = (ImageView) findViewById(R.id.snow_3);
        ImageView img_4 = (ImageView) findViewById(R.id.snow_4);
        ImageView img_5 = (ImageView) findViewById(R.id.snow_5);
        ImageView img_6 = (ImageView) findViewById(R.id.snow_6);
        ImageView img_7 = (ImageView) findViewById(R.id.snow_7);
        ImageView img_8 = (ImageView) findViewById(R.id.snow_8);
        ImageView img_9 = (ImageView) findViewById(R.id.snow_9);

        showDelayed_trans(img_1, 500);
        showDelayed_trans(img_2, 1000);
        showDelayed_trans(img_3, 1500);
        showDelayed_trans(img_4, 2000);
        showDelayed_trans(img_5, 2500);
        showDelayed_trans(img_6, 3000);
        showDelayed_trans(img_7, 3500);
        showDelayed_trans(img_8, 4000);
        showDelayed_trans(img_9, 4500);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(13000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashActivity.this,FirstActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    public void showDelayed(final View v, int delay){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(2);
                v.startAnimation(anim);
                v.setVisibility(View.VISIBLE);
            }
        }, delay);
    }

    public void showDelayed_trans(final View v, int delay){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                        0.0f, 1600.0f);
                animation.setDuration(5000);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                v.setVisibility(View.VISIBLE);
                v.startAnimation(animation);
            }
        }, delay);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
