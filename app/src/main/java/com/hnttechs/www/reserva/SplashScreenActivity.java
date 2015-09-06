package com.hnttechs.www.reserva;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dell on 8/20/15.
 */
public class SplashScreenActivity extends Activity{



    static ImageView first_text,second_text,third_text;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        first_text = (ImageView) findViewById(R.id.first_text);
        second_text = (ImageView) findViewById(R.id.second_text);
        third_text = (ImageView) findViewById(R.id.third_text);

        CountDown _tik;
        _tik=new CountDown(3000,3000,this,GridViewMain.class);// It delay the screen for 1 second and after that switch to YourNextActivity
        _tik.start();

        StartAnimations();

    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.first_animation);
        anim.reset();
        first_text.clearAnimation();
        first_text.startAnimation(anim);


        anim = AnimationUtils.loadAnimation(this, R.anim.second_animation);
        anim.reset();
        second_text.clearAnimation();
        second_text.startAnimation(anim);


        anim = AnimationUtils.loadAnimation(this, R.anim.third_animation);
        anim.reset();
        third_text.clearAnimation();
        third_text.startAnimation(anim);

    }
}
