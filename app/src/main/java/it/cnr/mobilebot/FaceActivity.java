package it.cnr.mobilebot;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Locale;

public class FaceActivity extends AppCompatActivity {

    private final int REQ_CODE = 100;
    private ImageView occhiView;
    private ImageView palliniView;
    private ImageView contorno_occhiView;
    private ImageView sopraccigliaView;
    private ImageView occhioSXView;
    private ImageView occhioDXView;
    private ImageView occhioSXView_OVER;
    private ImageView occhioDXView_OVER;

    Animation shake,tristi_contorno,ciglia_tremanti,cuoricino_SX,cuoricino_DX,cuoricino_SX_RED,cuoricino_DX_RED,cuoricino_contorno,
            animation_cry_ciglia, animation_blackdown,animation_fast_fade, animation_fast_fade_inverted;

    MQTTManager manager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        tristi_contorno = AnimationUtils.loadAnimation(this,R.anim.contorno_tristi);
        ciglia_tremanti = AnimationUtils.loadAnimation(this,R.anim.ciglia_tremolio);
        cuoricino_SX = AnimationUtils.loadAnimation(this,R.anim.cuoricino_sx);
        cuoricino_SX_RED = AnimationUtils.loadAnimation(this,R.anim.cuoricino_sx_over);
        cuoricino_DX = AnimationUtils.loadAnimation(this,R.anim.cuoricino_dx);
        cuoricino_DX_RED = AnimationUtils.loadAnimation(this,R.anim.cuoricino_dx_over);
        cuoricino_contorno = AnimationUtils.loadAnimation(this,R.anim.cuoricini_contorno);
        animation_cry_ciglia = AnimationUtils.loadAnimation(this,R.anim.cry_ciglia);
        animation_blackdown = AnimationUtils.loadAnimation(this,R.anim.blackdown);
        animation_fast_fade = AnimationUtils.loadAnimation(this,R.anim.fast_fade);
        animation_fast_fade_inverted = AnimationUtils.loadAnimation(this,R.anim.fast_fade_inverted);


        occhiView = findViewById(R.id.occhi);
        palliniView = findViewById(R.id.pallini);
        contorno_occhiView = findViewById(R.id.contorno_occhi);
        sopraccigliaView = findViewById(R.id.sopracciglie);
        occhioSXView = findViewById(R.id.occhio_sx);
        occhioDXView = findViewById(R.id.occhio_dx);
        occhioSXView_OVER = findViewById(R.id.occhio_sx_OVER);
        occhioDXView_OVER = findViewById(R.id.occhio_dx_OVER);


        occhiView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                    //CODICE PROVA
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parla pure");
                    try {
                        startActivityForResult(intent, REQ_CODE);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry your device not supported",
                                Toast.LENGTH_SHORT).show();
                    }

                return false;
                // END PROVA
            }
        });

        occhiView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(true){
                    piangi();
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //lastDown = System.currentTimeMillis();

                    manager.publish("ciao bello");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   // lastDuration = System.currentTimeMillis() - lastDown;
                }


                // change the background color

//                android:layout_width="646dp"
//                android:layout_height="369dp"
//                android:contentDescription="occhibelli"
//                android:scaleType="fitCenter"
//                android:src="@drawable/occhi_aperti"

               // intristiscitiAnimosamente();
                clear();
                occhiView.setImageResource(R.drawable.occhi_chiusi);

                new Handler().postDelayed(new Runnable() {
                    @Override
                   public void run() {
                        occhiView.setImageResource(R.drawable.occhi_aperti);

                    }
                }, 200);
                //occhiView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return false;
            }
        });


    //MQTT PART

        manager = new MQTTManager(this.getApplicationContext());

    }

    private void clear(){
        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(android.R.color.transparent);
        sopraccigliaView.setImageResource(android.R.color.transparent);
        contorno_occhiView.setImageResource(android.R.color.transparent);
        occhioDXView.setImageResource(android.R.color.transparent);
        occhioSXView.setImageResource(android.R.color.transparent);
        occhioDXView_OVER.setImageResource(android.R.color.transparent);
        occhioSXView_OVER.setImageResource(android.R.color.transparent);

    }

    private void lacrimevoliLacrime(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("LACRIMEVOLE SPETTACOLO");
                palliniView.setImageResource(android.R.color.transparent);
                occhiView.setImageResource(R.drawable.cry_lacrime);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        occhiView.setImageResource(android.R.color.transparent);
                        palliniView.setImageResource(R.drawable.cry_lacrime_inverted);
                        lacrimevoliLacrime();


                    }
                }, 150);
            }
        },150);

    }


    private void piangi(){
        System.out.println("\t\t\t\tSTO PIANGENDO ");
        clear();
        sopraccigliaView.setImageResource(R.drawable.nparola);
        sopraccigliaView.setAnimation(animation_blackdown);

        //ViewCompat.setTranslationZ(sopraccigliaView,3);

        float sopraccigliaZ =  ViewCompat.getTranslationZ(sopraccigliaView);
        System.out.println("SOPRACCIGLIA Z = "+sopraccigliaZ);

        float occhiViewZ =  ViewCompat.getTranslationZ(occhiView);
        System.out.println("occhiViewZ Z = "+occhiViewZ);

        float palliniViewZ =  ViewCompat.getTranslationZ(palliniView);
        System.out.println("palliniViewZ Z = "+palliniViewZ);

        ViewCompat.setTranslationZ(contorno_occhiView,4);
        ViewCompat.setTranslationZ(sopraccigliaView,3);
        ViewCompat.setTranslationZ(palliniView,2);
        ViewCompat.setTranslationZ(occhiView,1);



        animation_blackdown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                lacrimevoliLacrime();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //sopraccigliaView.clearAnimation();






                //palliniView.setImageResource(R.drawable.cry_lacrime_inverted);
                //palliniView.setAnimation(animation_fast_fade_inverted);
                //occhiView.setAnimation(animation_fast_fade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        occhiView.setImageResource(R.drawable.cry_lacrime);
        contorno_occhiView.setImageResource(R.drawable.cry_ciglia);
        contorno_occhiView.setAnimation(animation_cry_ciglia);

        animation_cry_ciglia.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Animation.AnimationListener listante = this;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation_cry_ciglia.setAnimationListener(listante);
                        contorno_occhiView.startAnimation(animation_cry_ciglia);

                    }
                }, 1000);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void innamorati(){

        System.out.println("\t\t\t\tMI STO INNAMORANDO DAVVERO ");
        clear();

        LinearLayout cuoriLayout = (LinearLayout) findViewById(R.id.occhi_over_layout);
        cuoriLayout.bringToFront();
        cuoriLayout.invalidate();

        occhiView.setImageResource(R.drawable.occhi_love_contorno);
        //palliniView.setImageResource(R.drawable.occhi_love_pupille);
        occhioSXView.setImageResource(R.drawable.occhio_cuore_sx);
        occhioSXView_OVER.setImageResource(R.drawable.occhio_cuore_sx_red);
        occhioDXView_OVER.setImageResource(R.drawable.occhio_dx_red);
        occhioDXView.setImageResource(R.drawable.occhio_dx);

        occhioSXView.setAnimation(cuoricino_SX);
        occhioDXView.setAnimation(cuoricino_DX);
        occhioSXView_OVER.setAnimation(cuoricino_SX_RED);
        occhioDXView_OVER.setAnimation(cuoricino_DX_RED);


        //occhiView.setAnimation(shake);

       // palliniView.setAnimation(shake);

    }

    private void intristiscitiAnimosamente(){
        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(R.drawable.occhi_tristi_pallini);
        palliniView.setAnimation(shake);
        sopraccigliaView.setImageResource(R.drawable.occhi_tristi_sopracciglia);
        sopraccigliaView.setAnimation(ciglia_tremanti);

        contorno_occhiView.setImageResource(R.drawable.occhi_tristi_contorno_occhi);
        contorno_occhiView.setAnimation(tristi_contorno);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //textView.setText(result.get(0));
                    Toast.makeText(getApplicationContext(),
                            ""+result.get(0),
                            Toast.LENGTH_SHORT).show();

                    //manager.publish(""+result.get(0));

                    manager.publish("ciao bello");


                    if((""+result.get(0)).equals("sono triste")){

                        intristiscitiAnimosamente();

                }else if((""+result.get(0)).equals("sono felice")){
                        innamorati();
                    }

//                    else{
//                        intristiscitiAnimosamente();
//                    }
//                }else{
//                    intristiscitiAnimosamente();
                }
                break;
            }
        }
    }
}