
package it.cnr.mobilebot.game.mindgames.supermarket;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import it.cnr.mobilebot.FaceActivity;
import it.cnr.mobilebot.MQTTManager;
import it.cnr.mobilebot.R;
import it.cnr.mobilebot.logic.EventManager;

public class SuperMarket extends AppCompatActivity {

    FaceActivity faceActivity = null;
    TextView button_speak,request;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    MediaPlayer mp_REC = null;
    MQTTManager manager = null;
    private boolean willanswer = false;
    private boolean listening;
    private TextView score;
    int toGet = EventManager.getInstance().getSuperMarketBlob().getSolutionProducts().size();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        int uiOptionsFull = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptionsFull);

        mp_REC = MediaPlayer.create(this, R.raw.rec_sound);
        button_speak = findViewById(R.id.button_mainButton_speakGame1);
        request = findViewById(R.id.request);
        score = findViewById(R.id.score);
        score.setText(EventManager.getInstance().getCorrettiG1() + "/" + toGet);
        request.setText("premi il bottone 'LISTA DELLA SPESA' e poi 'AL REPARTO' per proseguire");
//        request.setText(EventManager.getInstance().getSuperMarketBlob().getRequest());


        //thread di attesa fine messaggio e enunciazione delle istruzioni:
        SuperMarketBlob blob = EventManager.getInstance().getSuperMarketBlob();


        Button repeat = findViewById(R.id.repeat_game1);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = EventManager.getInstance().getSuperMarketBlob().getVocalDescription();


               MQTTManager.faceActivity.speakText(message, false);

            }
        });



        final Button buttonListaSpesa = findViewById(R.id.button_lista_spesa);
        final Button buttonReparto = findViewById(R.id.button_reparto);

        buttonListaSpesa.setEnabled(true);
        buttonListaSpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = EventManager.getInstance().getSuperMarketBlob().getInitialMessage();

                MQTTManager.faceActivity.speakText(message, false);
                buttonListaSpesa.setEnabled(false);
                buttonReparto.setEnabled(true);


            }
        });


        buttonReparto.setEnabled(false);
        buttonReparto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = EventManager.getInstance().getSuperMarketBlob().getVocalRequest();

                MQTTManager.faceActivity.speakText(message, false);
                request.setText(Html.fromHtml(EventManager.getInstance().getSuperMarketBlob().getTextualRequest()));

            }
        });

        button_speak.bringToFront();
        button_speak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("sono qui a domandarmi il senso della vita");
                try {
                    if (mp_REC.isPlaying()) {
                        mp_REC.pause();
                        mp_REC.setVolume(1f, 1f);
                        mp_REC.seekTo(0);
                    }
                    mp_REC.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("rec button has been pressed");
                //manager.buttonPressed(LoggingTag.SPEAK);

                button_speak.setBackgroundResource(R.drawable.speak_button_pressed);
                System.out.println("trasto il plano");
                listen();
                System.out.println("non trasto più il plano");


            }
        });

        RecyclerView recyclerView;
        SuperMarketAdapter superMarketAdapter = new SuperMarketAdapter(this);

        recyclerView = findViewById(R.id.RecycleProducts);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        recyclerView.setAdapter(superMarketAdapter);
    }

    public void listen() {
        System.out.println("LISTEN ME");
        if (mSpeechRecognizer == null) {

            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    this.getPackageName());
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 40000000);
            mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {

                @Override
                public void onBeginningOfSpeech() {
                    System.out.println("BEGIN SPEECH");
                    //   Toast toast = Toast.makeText(getApplicationContext(),"speech begin", Toast.LENGTH_SHORT);
                    //   toast.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL, 0, 0);
                    //   toast.show();
                    // TODO Auto-generated method stub

                }

                @Override
                public void onBufferReceived(byte[] arg0) {
                    // TODO Auto-generated method stub

                }


                @Override
                public void onEndOfSpeech() {
                    //  Toast toast = Toast.makeText(getApplicationContext(),"end of speech", Toast.LENGTH_SHORT);
                    //  toast.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL, 0, 0);
                    //  toast.show();
                    stopListen();

                }

                @Override
                public void onError(int arg0) {
                    // TODO Auto-generated method stub
                    System.err.println("SIdasodajsodjaosijd: " + arg0);
                    Toast toast = Toast.makeText(getApplicationContext(), "ERROR: " + arg0, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    willanswer = false;
                }

                @Override
                public void onEvent(int arg0, Bundle arg1) {
                    System.out.println("EVENT");
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // TODO Auto-generated method stub
                    System.out.println("PARTIAL");
                    //  Toast toast = Toast.makeText(getApplicationContext(),"Partial: "+partialResults.toString(), Toast.LENGTH_SHORT);
                    //   toast.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL, 0, 0);
                    //   toast.show();

                }

                @Override
                public void onReadyForSpeech(Bundle params) {
                    //      Toast toast = Toast.makeText(getApplicationContext(),"Ready", Toast.LENGTH_SHORT);
                    //     toast.setGravity(Gravity.TOP |Gravity.CENTER_HORIZONTAL, 0, 0);
                    //      toast.show();
                    System.out.println("READY");
                    // TODO Auto-generated method stub

                }

                @Override
                public void onResults(Bundle results) {

                    // TODO Auto-generated method stub
                    String str = new String();
                    System.out.println("onResults " + results);
                    ArrayList data = results.getStringArrayList(RESULTS_RECOGNITION);
                    for (int i = 0; i < data.size(); i++) {
                        System.out.println("> result " + data.get(i));
                        str += data.get(i);
                    }
                    ArrayList<String> userMessage;
                    userMessage = results.getStringArrayList(RESULTS_RECOGNITION);
                    if (userMessage == null || userMessage.isEmpty() || userMessage.get(0) == null || userMessage.get(0).isEmpty()) {
                        System.out.println("NO ANSWER");
                        System.out.println("--");

                    }
                    System.out.println("hai detto: " + userMessage.get(0));


                    Toast toast = Toast.makeText(getApplicationContext(), "hai detto: " + userMessage.get(0), Toast.LENGTH_SHORT);
                    List<Product> solutionProducts = EventManager.getInstance().getSuperMarketBlob().getSolutionProducts();
                    Iterator<Product> iterator = solutionProducts.iterator();
                   while (iterator.hasNext()){
                    LevenshteinDetailedDistance ldd = new LevenshteinDetailedDistance();
                    Product next = iterator.next();
                    LevenshteinResults apply = ldd.apply(userMessage.get(0).toLowerCase(), next.getName().toLowerCase());
                    String[] split = userMessage.get(0).split(" ");
                    String[] split1 = next.getName().split(" ");
                    if(apply.getDistance() <= 1 || split[split.length -  1].toLowerCase().equals(split1[split1.length -  1].toLowerCase())){
                        EventManager.getInstance().setCorrettiG1(EventManager.getInstance().getCorrettiG1() + 1);
                        MQTTManager.faceActivity.speakText("Esatto", false);
                        score.setText(EventManager.getInstance().getCorrettiG1() + "/" + toGet);
                        iterator.remove();
                        //CheckBoxManager.getInstance().getCheckBoxes().get(0).check.setVisibility(View.VISIBLE);
                        break;
                    }else{
                        MQTTManager.faceActivity.speakText("Hai sbagliato parola", false);
                    }


                   }

                   if(solutionProducts.isEmpty()){
                       score.setText(EventManager.getInstance().getCorrettiG1() + "/" + toGet);
                       MQTTManager.faceActivity.speakText("Complimenti, hai finito il gioco! Non era facile", false);
                   }
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i("TextToSpeech", "On Done");
                        }
                    });

                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // TODO Auto-generated method stub
                    //System.out.println("> "+rmsdB);

                }


            });

        }
        if (listening) {
            stopListen();
            System.out.println("FORCED STOP LISTENING");
        } else {
            listening = true;
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }

        //wordsList = (ListView) findViewById(R.id.listView1);

    }

    public void stopListen(){
        System.out.println("END END");
        runOnUiThread(new Runnable() {
            public void run() {
                Log.i("TextToSpeech","On Done");
                button_speak.setBackgroundResource(R.drawable.speak_button);
}});
        listening = false;
    }

}