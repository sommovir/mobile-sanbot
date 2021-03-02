package it.cnr.mobilebot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;
import it.cnr.mobilebot.logic.ConnectionEventListener;
import it.cnr.mobilebot.logic.EventManager;

public class FaceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ConnectionEventListener {


    private boolean stopme = false;
    private boolean test = false;
    private final int REQ_CODE = 100;
    private ImageView occhiView;
    private ImageView palliniView;
    private ImageView contorno_occhiView;
    private ImageView sopraccigliaView;
    private ImageView occhioSXView;
    private ImageView occhioDXView;
    private ImageView occhioSXView_OVER;
    private ImageView occhioDXView_OVER;
    private ImageView connectionView;
    private boolean first = true;
    private String real_ip = null;
    TextToSpeech tts = null;
    Animation shake,tristi_contorno,ciglia_tremanti,cuoricino_SX,cuoricino_DX,cuoricino_SX_RED,cuoricino_DX_RED,cuoricino_contorno,
            animation_cry_ciglia, animation_blackdown,animation_fast_fade, animation_fast_fade_inverted, shake_vertical,outrage_anim,
            server_online_animazione;

    MQTTManager manager = null;
    MediaPlayer mp_ciglia =null;
    MediaPlayer mp_button =null;

    Handler questionHandler = new Handler();
    Handler cryHandler = new Handler();

    private TextView button_reconnect = null;

    private AlertDialog tableDialog = null;


    private boolean bastaIndugi = false;

    private String initialMessage = "Buonasera, benvenuto nella chat-bot dei laboratori di Televita! Come posso aiutarla?";

    private Map<String,Boolean> colorCellMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mp_ciglia = MediaPlayer.create(FaceActivity.this, R.raw.ciglia);
        mp_button = MediaPlayer.create(FaceActivity.this, R.raw.button_click);
        EventManager.getInstance().addConnectionEventListener(this);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        int uiOptionsFull = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

/*

        WebView webView = (WebView) findViewById(R.id.youtube);

        if(webView == null){
            System.out.println("MEGA NULLONE");
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.journaldev.com");

        decorView.setSystemUiVisibility(uiOptionsFull);
        */

    //    WebView webView = (WebView) findViewById(R.id.youtube);
      //  webView.setVisibility(View.INVISIBLE);

        button_reconnect= findViewById(R.id.button_mainButton_reconnect);

        /*
        VideoDialog videoDialogFragment = (VideoDialog) getSupportFragmentManager()
                .findFragmentById(R.id.videoContainer);


        if(videoDialogFragment == null){
            System.out.println("IL CULONE DELLE SCALDABAGNO");
        }



        View viewThatContainsVideoView = findViewById(R.id.videoContainer); //TODO

        if(viewThatContainsVideoView == null){
            System.out.println("LA GRANDE MINCHIA");
        }


        VideoView videoView = (VideoView) viewThatContainsVideoView.findViewById(R.id.videoView);


        if(videoView == null){
            System.out.println("EH NO EH !!!!!! NON SI FANNO QUESTE COSE");
        }
        String videoPath = "android.resource://"+getPackageName() + "/"+R.raw.peggio;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

         */


        tts = new TextToSpeech(this, this);

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        shake_vertical = AnimationUtils.loadAnimation(this,R.anim.shake_vertical);
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
        outrage_anim = AnimationUtils.loadAnimation(this,R.anim.outrage_anim);
        server_online_animazione = AnimationUtils.loadAnimation(this,R.anim.connection_online);



        occhiView = findViewById(R.id.occhi);
        palliniView = findViewById(R.id.pallini);
        contorno_occhiView = findViewById(R.id.contorno_occhi);
        sopraccigliaView = findViewById(R.id.sopracciglie);
        occhioSXView = findViewById(R.id.occhio_sx);
        occhioDXView = findViewById(R.id.occhio_dx);
        occhioSXView_OVER = findViewById(R.id.occhio_sx_OVER);
        occhioDXView_OVER = findViewById(R.id.occhio_dx_OVER);

        connectionView = findViewById(R.id.imageView_ServerStatus);



        button_reconnect.bringToFront();
        button_reconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    if (mp_button.isPlaying()) {
                        mp_button.pause();
                        mp_button.seekTo(0);
                    } mp_button.start();
                } catch(Exception e) { e.printStackTrace(); }

                System.out.println("main button has been pressed");
                Resources res = getApplicationContext().getResources();
                final Drawable d1 = ResourcesCompat.getDrawable(res, R.drawable.reconnect, null);
                final Drawable d2 = ResourcesCompat.getDrawable(res, R.drawable.reconnect_green, null);

                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d1,d2});
                button_reconnect.setBackground(transitionDrawable);
                transitionDrawable.startTransition(150);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d2,d1});
                        button_reconnect.setBackground(transitionDrawable);
                        transitionDrawable.startTransition(250);

                    }
                }, 150);

                manager.connect();


            }
        });
        button_reconnect.setVisibility(View.INVISIBLE);



        final TextView button1 = findViewById(R.id.button_mainButton);
        button1.bringToFront();
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    if (mp_button.isPlaying()) {
                        mp_button.pause();
                        mp_button.seekTo(0);
                    } mp_button.start();
                } catch(Exception e) { e.printStackTrace(); }

                System.out.println("main button has been pressed");
                Resources res = getApplicationContext().getResources();
                final Drawable d1 = ResourcesCompat.getDrawable(res, R.drawable.button_bar_start_setting, null);
                final Drawable d2 = ResourcesCompat.getDrawable(res, R.drawable.button_bar_start_setting_c, null);

                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d1,d2});
                button1.setBackground(transitionDrawable);
                transitionDrawable.startTransition(150);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d2,d1});
                        button1.setBackground(transitionDrawable);
                        transitionDrawable.startTransition(250);

                    }
                }, 150);




            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                speakText(initialMessage);

            }
        }, 1000);

        final TextView button2 = findViewById(R.id.button_mainButton2);
        button2.bringToFront();
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (mp_button.isPlaying()) {
                        mp_button.pause();
                        mp_button.seekTo(0);
                    } mp_button.start();
                } catch(Exception e) { e.printStackTrace(); }

                System.out.println("repeat button has been pressed");
                Resources res = getApplicationContext().getResources();
                final Drawable d1 = ResourcesCompat.getDrawable(res, R.drawable.button_bar_repeat_r, null);
                final Drawable d2 = ResourcesCompat.getDrawable(res, R.drawable.button_bar_repeat_click_r, null);

                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d1,d2});
                button2.setBackground(transitionDrawable);
                transitionDrawable.startTransition(150);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{d2,d1});
                        button2.setBackground(transitionDrawable);
                        transitionDrawable.startTransition(250);

                    }
                }, 150);



                manager.repeat();
            }
        });

        final TextView button3 = findViewById(R.id.button_mainButton3);
        button3.bringToFront();
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),"Questa funzione non è al momento disponibile", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
        final TextView button4 = findViewById(R.id.button_mainButton4);
        button4.bringToFront();
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"Questa funzione non è al momento disponibile", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP |Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });



        connectionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("TOUCH TOUCH");

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        getApplicationContext().getString(R.string.ip_file), Context.MODE_PRIVATE);

                if(sharedPref != null){
                    real_ip = sharedPref.getString(getApplicationContext().getString(R.string.IP_KEY), "not found");
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(FaceActivity.this);
// Add the buttons
                builder.setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FaceActivity.this);
                        builder.setTitle("Cambia IP");

// Set up the input
                        final EditText input = new EditText(FaceActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                        input.setText("", TextView.BufferType.EDITABLE);
                        builder.setView(input);

// Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();
                                manager.updateIP(m_Text);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });
                builder.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
// Set other dialog properties
                builder.setTitle("Server IP");
                String messageOnline = EventManager.getInstance().isServerOnline() ? "Online" : "Offline";
                builder.setMessage("IP: "+MQTTManager.ip+"\nStatus: "+messageOnline);

// Create the AlertDialog
                final AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        //dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
                    }
                });



                dialog.show();
                return false;
            }
        });








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





                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //lastDown = System.currentTimeMillis();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   // lastDuration = System.currentTimeMillis() - lastDown;
                    try {
                        if (mp_ciglia.isPlaying()) {
                            mp_ciglia.pause();
                            mp_ciglia.seekTo(0);
                        } mp_ciglia.start();
                    } catch(Exception e) { e.printStackTrace(); }
                }


                // change the background color

//                android:layout_width="646dp"
//                android:layout_height="369dp"
//                android:contentDescription="occhibelli"
//                android:scaleType="fitCenter"
//                android:src="@drawable/occhi_aperti"

               // intristiscitiAnimosamente();
                stopCry();
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



        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getApplicationContext().getString(R.string.ip_file), Context.MODE_PRIVATE);
        if(sharedPref != null){
            String name = sharedPref.getString(getApplicationContext().getString(R.string.NAME_KEY), null);
            if(name == null){
                System.out.println(" -- NOME NULLO");
                speakText("Per favore dimmi il tuo nome");
            }else {
                System.out.println("MI CHIAMO: " + name);
            }
        }else{
            System.out.println("ZERO FILE TROVATI");
        }
    //MQTT PART

        System.out.println("connecting MQTT.. ");
        manager = new MQTTManager(this.getApplicationContext());

        manager.setFaceActivity(this);
        if(manager.isConnected()){
            connectionView.setAnimation(server_online_animazione);
            server_online_animazione.start();
        }



    }

    public void esprimiQualcheDubbio(){
        stopCry();
        bastaIndugi = false;


        occhiView.setImageResource(R.drawable.question_face_1);
        for (int i = 0; i < 5; i++) {
            if(bastaIndugi == true){
                System.out.println("qualcuno mi ha settato a true e io smetto di pormi domande esistenziali");
                return;
            }
            questionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    occhiView.setImageResource(R.drawable.question_face_2);

                }
            }, 200 + 1500*i);


            questionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    occhiView.setImageResource(R.drawable.question_face_1);

                }
            }, 1500 + 1500*i);

        }
        questionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                occhiView.setImageResource(R.drawable.question_face_2);

            }
        }, 200 + 1500*6);

    }

    public void stopCry(){
        stopme = true;
       clear();
   }

    public void clear(){
        stopme = true;
        bastaIndugi = true;

        if(questionHandler != null){
            questionHandler.removeCallbacksAndMessages(null);
        }
        if(cryHandler != null){
            cryHandler.removeCallbacksAndMessages(null);
        }


        float sopraccigliaZ =  ViewCompat.getTranslationZ(sopraccigliaView);
     //   System.out.println("SOPRACCIGLIA Z = "+sopraccigliaZ);

        float occhiViewZ =  ViewCompat.getTranslationZ(occhiView);
     //   System.out.println("occhiViewZ Z = "+occhiViewZ);

        float palliniViewZ =  ViewCompat.getTranslationZ(palliniView);
   //     System.out.println("palliniViewZ Z = "+palliniViewZ);

        ViewCompat.setTranslationZ(contorno_occhiView,0);
        ViewCompat.setTranslationZ(sopraccigliaView,0);
        ViewCompat.setTranslationZ(palliniView,0);
       ViewCompat.setTranslationZ(occhiView,0);



        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(android.R.color.transparent);
        sopraccigliaView.setImageResource(android.R.color.transparent);
        contorno_occhiView.setImageResource(android.R.color.transparent);
        occhioDXView.setImageResource(android.R.color.transparent);
        occhioSXView.setImageResource(android.R.color.transparent);
        occhioDXView_OVER.setImageResource(android.R.color.transparent);
        occhioSXView_OVER.setImageResource(android.R.color.transparent);
       // if(animation_blackdown !=null) animation_blackdown.cancel();

    }


    public void lacrimevoliLacrime(){
        System.out.println("-----------------------------------");
        System.out.println("LACRIMEVOLI LACRIME IN ARRIVO");
        if(stopme){
            System.out.println("NON MI FARANNO MAI PIANGERE IN QUESTO MODO");
            return;
        }

        cryHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("LACRIMEVOLE SPETTACOLO");
                palliniView.setImageResource(android.R.color.transparent);
                occhiView.setImageResource(R.drawable.cry_lacrime);

                cryHandler.postDelayed(new Runnable() {
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


    public void piangi(){
        System.out.println("\t\t\t\tSTO PIANGENDO ");
        clear();
        stopme = false;


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

        animation_blackdown.start();

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

    public void innamorati(){

        System.out.println("\t\t\t\tMI STO INNAMORANDO DAVVERO ");
        stopCry();
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

    public void intristiscitiAnimosamente(){
        clear();
        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(R.drawable.occhi_tristi_pallini);
        palliniView.setAnimation(shake);
        sopraccigliaView.setImageResource(R.drawable.occhi_tristi_sopracciglia);
        sopraccigliaView.setAnimation(ciglia_tremanti);

        contorno_occhiView.setImageResource(R.drawable.occhi_tristi_contorno_occhi);
        contorno_occhiView.setAnimation(tristi_contorno);

    }

    public void incazzati(){
        clear();
        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(R.drawable.outrage_flames);
        palliniView.setAnimation(shake);

        contorno_occhiView.setImageResource(R.drawable.outrage_contorno);
        contorno_occhiView.setAnimation(outrage_anim);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }

    }

    public  void ilRisoAbbonda(){
        System.out.println("\t\t\t\tIL RISO ABBONDA ..");
        stopCry();

        occhiView.setImageResource(android.R.color.transparent);
        palliniView.setImageResource(R.drawable.ridacchia_pupille);
        palliniView.setAnimation(shake_vertical);
        //sopraccigliaView.setImageResource(R.drawable.ridacchia_contorno);
        //sopraccigliaView.setAnimation(ciglia_tremanti);

        contorno_occhiView.setImageResource(R.drawable.ridacchia_contorno);
        //contorno_occhiView.setAnimation(tristi_contorno);

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

                    manager.publish(""+result.get(0));
                    //if(!test)



                    if((""+result.get(0)).equals("sono triste")){

                        intristiscitiAnimosamente();

                        //tts.setLanguage(Locale.ITALIAN);
                       // tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);

                        tts.speak("Mi dispiace amico mio",TextToSpeech.QUEUE_FLUSH,null,null);

                }else if((""+result.get(0)).equals("sono felice")){
                        innamorati();
                    }
                    else if((""+result.get(0)).toLowerCase().contains("mi è morto il gatto")){
                        piangi();
                    }
                    else if((""+result.get(0)).contains("sei simpatico")){
                        ilRisoAbbonda();
                    }
                    else if((""+result.get(0)).contains("cognitivo")){
                        showTestChoice();
                    }
                    else if((""+result.get(0)).contains("tabella")){
                        String message = "laboratorio di filatelia;16:30-18:00!laboratorio di filatelia;16:30-18:00";

                        showTableData(message.split("!"));
                    }
                    else if((""+result.get(0)).contains("dubbio")){
                        esprimiQualcheDubbio();
                    }
                    else if((""+result.get(0)).contains("franchestin")){

                        Intent videoDialogIntent = new Intent(FaceActivity.this, VideoDialog.class);
                        FaceActivity.this.startActivity(videoDialogIntent);


                      //  VideoDialog videoDialog = new VideoDialog();
                      //  videoDialog.show(getSupportFragmentManager(), "video");
                       // videoDialog.startActivity();
                      //  System.out.println("PACKAGE NAME = "+getPackageName());
                      //  videoDialog.play("NON SI SA");
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

    public void showYouTubeVideo(String id){

        if(id.equals("link farlocco")) {
            YouTubeActivity.id = null;
            Intent videoDialogIntent = new Intent(FaceActivity.this, YouTubeActivity.class);
            FaceActivity.this.startActivity(videoDialogIntent);
        }else{
            System.out.println("[YouTube Activity] playing video: "+id);
            YouTubeActivity.id = id;
            Intent videoDialogIntent = new Intent(FaceActivity.this, YouTubeActivity.class);
            FaceActivity.this.startActivity(videoDialogIntent);

            /*while(!YouTubeActivity.active){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

             */
           // System.out.println("LA MINCHIA ******************************************************");
           // EventManager.getInstance().playYouTubeVideo(id);
        }

        /*
        System.out.println("caricamento video di test: 2XKBGRryVDQ");
        WebView youtubeWebView =  findViewById(R.id.youtube);
        String myVideoYoutubeId = "2XKBGRryVDQ";

        youtubeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        youtubeWebView.setVisibility(View.VISIBLE);
        youtubeWebView.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId);

         */

    }

    public void showVideo(){
        Intent videoDialogIntent = new Intent(FaceActivity.this, VideoDialog.class);
        FaceActivity.this.startActivity(videoDialogIntent);
    }

    public void showImage(){
        ImageDisplayerDialog.externalURL = null;
        Intent idd = new Intent(FaceActivity.this, ImageDisplayerDialog.class);
        FaceActivity.this.startActivity(idd);

    }

    public void showImage(String url){
        ImageDisplayerDialog.externalURL = url;
        Intent idd = new Intent(FaceActivity.this, ImageDisplayerDialog.class);
        FaceActivity.this.startActivity(idd);

    }


    public void speakText(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void showGenericTable(String [] data){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.table_dialog, null);
        builder.setView(alertView);
        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.tableLayout);
        int row = 0;
        for( String d : data){
            String[] split = d.split(";");
            TableRow tableRow = new TableRow(dialogContext);
            tableRow.setPadding(3,3,3,3);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                    (0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.setMargins(3,3,3,3);
            tableRow.setLayoutParams(layoutParams);

            for (final String cella : split) {
                final TextView textView1 = new TextView(dialogContext);
                if(row == 0){
                    textView1.setTypeface(null, Typeface.BOLD);
                }else{
                    GradientDrawable gd = new GradientDrawable(
                          //  GradientDrawable.Orientation.TOP_BOTTOM,
                           // new int[] {0xFFe5edef,0xFFcedde0});
                    );
                   // gd.setCornerRadius(6);
                    gd.setColor(0xFFFFFFFF);
                    gd.setStroke(1, 0xFF000000);
                    textView1.setBackground(gd);
                }
                textView1.setPadding(5,5,5,5);
                // textView1.setLayoutParams(new TableRow.LayoutParams
                //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                textView1.setTextSize(18);
                textView1.setText(cella);
                colorCellMap.put(""+cella,Boolean.FALSE);
                textView1.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if(colorCellMap.get(""+cella)){
                                                         System.out.println("TRUE");
                                                         textView1.setBackgroundColor(0xFFFFFFFF);
                                                         colorCellMap.put(""+cella,Boolean.FALSE);
                                                     }else{
                                                         System.out.println("FALSE");
                                                         textView1.setBackgroundColor(0xFF00FF00);
                                                         colorCellMap.put(""+cella,Boolean.TRUE);
                                                     }

                                                 }
                                             }

                );


                tableRow.addView(textView1,layoutParams);

            }
            row++;
            tableLayout.addView(tableRow);
        }
        builder.setCancelable(true);
        //AlertDialog alertDialog =
        if(this.tableDialog != null){
            this.tableDialog.cancel();
            this.tableDialog.dismiss();
            this.tableDialog = builder.create();
        }else{
            this.tableDialog = builder.create();
        }

        tableDialog.show();

    }

    public void showTableData(String[] data){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informazioni");

        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.table_dialog, null);
        builder.setView(alertView);



        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.tableLayout);
        for( String d : data){

            String[] split = d.split(";");
            String title = split[0];
            String infoinfo = split[1];

            TableRow tableRow = new TableRow(dialogContext);
            tableRow.setPadding(10,10,10,10);
            tableRow.setLayoutParams(new TableRow.LayoutParams
                    (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

            TextView textView1 = new TextView(dialogContext);
            textView1.setPadding(15,15,15,15);
           // textView1.setLayoutParams(new TableRow.LayoutParams
           //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            textView1.setTextSize(18);
            textView1.setText(title);
            tableRow.addView(textView1);

            TextView textView2 = new TextView(dialogContext);
            textView2.setPadding(15,15,15,15);
           // textView2.setLayoutParams(new TableRow.LayoutParams
           //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            textView2.setTextSize(18);
            textView2.setText(infoinfo);
            textView2.setBackgroundResource(R.color.row2);
            tableRow.addView(textView2);

            tableLayout.addView(tableRow);
        }

        builder.setCancelable(true);
        if(this.tableDialog != null){
            this.tableDialog.cancel();
            this.tableDialog.dismiss();
            this.tableDialog = builder.create();
        }else{
            this.tableDialog = builder.create();
        }

        tableDialog.show();
       // AlertDialog alertDialog = builder.create();
      //  alertDialog.show();
    }

    public void showTestChoice(){
        final CharSequence[] charSequence = new CharSequence[] {"Storia di Esopo","Conta le parole", "Trova l'intruso"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scegli il test cognitivo")
                //.setMessage("You can buy our products without registration too. Enjoy the shopping")
                .setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Hai selezionato: "+charSequence);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  manager.publish("si");
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void showTestChoice(String text){
    //example multichoice:tette,culo,figa:cosa ti piace di più ?
        String[] mainText = text.split(":");
        String[] choices = mainText[1].split(",");
        String title = mainText[2];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                //.setMessage("You can buy our products without registration too. Enjoy the shopping")
                .setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Hai selezionato: "+which);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  manager.publish("si");
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    @Override
    public void onInit(int i) {


        if(i == TextToSpeech.ERROR){
            System.out.println("ERRORE !!");
//            tts.setLanguage(Locale.ITALIAN);
          //  tts.setPitch(1);
        }
        if (i == TextToSpeech.SUCCESS) {
            //Setting speech Language
            System.out.println("SUX !!");
            tts.setLanguage(Locale.ITALIAN);
            tts.setPitch(1);

            tts.speak("Oggi è un bel giorno",TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }

    @Override
    public void serverOnline() {
        ImageView img= (ImageView) findViewById(R.id.imageView_ServerStatus);
        img.setImageResource(R.drawable.green);
        //Toast.makeText(getApplicationContext(), "Server Online", Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(getApplicationContext(),"Server Online", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if(first){
            first = false;
        }else {
            tts.speak("Server online", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        button_reconnect.setVisibility(View.INVISIBLE);
        img.setAnimation(server_online_animazione);
        server_online_animazione.start();
        System.out.println("animazio guggulazio");

    }

    @Override
    public void serverOffline() {
        server_online_animazione.cancel();
        ImageView img= (ImageView) findViewById(R.id.imageView_ServerStatus);
        img.setImageResource(R.drawable.gdot_red_16);
        Toast.makeText(getApplicationContext(), "Server Offline", Toast.LENGTH_SHORT).show();
        tts.speak("Server offline",TextToSpeech.QUEUE_FLUSH,null,null);
        button_reconnect.setVisibility(View.INVISIBLE);
    }
}