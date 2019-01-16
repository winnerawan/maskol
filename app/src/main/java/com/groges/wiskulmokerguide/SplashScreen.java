package com.groges.wiskulmokerguide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.view.Window.FEATURE_NO_TITLE;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        animate();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
                finish();
            }
        }, 3000);
    }


    private void animate() {
        Object localObject = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        ((ObjectAnimator)localObject).setInterpolator(new AccelerateDecelerateInterpolator());
        ((ObjectAnimator)localObject).setDuration(1500L);
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        localObjectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        localObjectAnimator1.setDuration(1500L);
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        localObjectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        localObjectAnimator2.setDuration(1500L);
        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.play((Animator)localObject).with(localObjectAnimator1).with(localObjectAnimator2);
        localAnimatorSet.setStartDelay(1500L);
        localAnimatorSet.start();
        findViewById(R.id.imagelogo).setAlpha(1.0F);
        localObject = AnimationUtils.loadAnimation(this, R.anim.fade);
        findViewById(R.id.imagelogo).startAnimation((Animation)localObject);
    }
}
