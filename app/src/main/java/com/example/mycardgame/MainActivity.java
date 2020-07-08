package com.example.mycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    int count = 0;
    private Timer timer;
    Animation animation;
    private Button b;
    private TimerTask timerTask;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        timer = new Timer();
        animation = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fadeout);
        imageView = findViewById(R.id.tVHeader2);
        //b = findViewById(R.id.nextBtn);

        /** לזכור לשחרר
         //i  = new Intent(this,GameActivity.class);
         **/
        i = new Intent(this, StartActivity.class);


        timerTask = new TimerTask() {
            @Override
            public void run() {
                //imageView.startAnimation(animation);

                startActivity(i);
                finish();
                //Toast.makeText(MainActivity.this, "liad is bord", Toast.LENGTH_SHORT).show();
                // startThread();
            }
        };
        timer.schedule(timerTask,2000);

        //startThread();


    }
    public void startThread()
    {
        ExThread exThread = new ExThread(b);
        try {
            exThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void btnNextClick(View view) {
//        Intent intent = new Intent(view.getContext(),GameActivity.class);
//        StartActivity(intent);

//        Intent i = new Intent(view.getContext(),GameActivity.class);
//        StartActivity(i);


//        Animation animation = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.blink_anim);
//        imageView.startAnimation(animation);

//        Animation []animationArry = new Animation[11];
//        animationArry = initArry(animationArry);
//        doAnimation(animationArry,count);
//        count++;
//        if(count == 10)
//            count = 0;

    }
    public  Animation[] initArry( Animation []animationArry)
    {
        animationArry[0] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.blink_anim);
        animationArry[1] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fadein);
        animationArry[2] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.bounce);
        animationArry[3] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.fadeout);
        animationArry[4] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        animationArry[5] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.mixed_anim);
        animationArry[6] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.righttoleft);
        animationArry[7] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.rotate);
        animationArry[8] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.sample_anim);
        animationArry[9] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.zoomin);
        animationArry[10] = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.zoomout);
        return animationArry;
    }
    private void doAnimation(Animation []animationArry,int count)
    {
        imageView.startAnimation(animationArry[count]);
    }
    class ExThread extends Thread{
        Button b;
        public ExThread(Button b)
        {
            this.b = b;
        }
        @Override
        public void run() {
            b.setVisibility(View.VISIBLE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(MainActivity.this, "liad is bord", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
