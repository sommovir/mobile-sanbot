package it.cnr.mobilebot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DURATION = 4000;

    Animation topAnim, bottomAnim, sfondoAnim;

    TextView mainText;
    TextView subText;
    ImageView sfondoView;

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        sfondoAnim = AnimationUtils.loadAnimation(this,R.anim.background_animation);

        mainText = findViewById(R.id.text_main);
        subText = findViewById(R.id.text_sub);
        sfondoView = findViewById(R.id.sfondo);

        mainText.setAnimation(topAnim);
        subText.setAnimation(bottomAnim);
        sfondoView.setAnimation(sfondoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, FaceActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN_DURATION);
    }
}