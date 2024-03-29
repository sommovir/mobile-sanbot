package it.cnr.mobilebot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

import it.cnr.mobilebot.game.mindgames.supermarket.SuperMarket;
import it.cnr.mobilebot.game.mindgames.supermarket.SuperMarketBlob;
import it.cnr.mobilebot.logic.DeviceType;
import it.cnr.mobilebot.logic.EventManager;
import it.cnr.mobilebot.logic.LoggingTag;

/**
 * Created by Luca Coraci [luca.coraci@istc.cnr.it] on 18/06/2020.
 */
public class MQTTManager {

    private static final String clientId = generateClientId(); // "user-110";
    MqttAndroidClient client = null;
    //MqttAsyncClient client = null;
    boolean test = false;
    public static FaceActivity faceActivity = null;
    private static Context context = null;
    public static String ip = "192.168.43.112";
    public MqttMessage lastMessage = null;
    public String lastTopic = null;
    public static SuperMarketBlob superMarketBlob;

    public void updateIP(String m_text) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.ip_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.IP_KEY), m_text);
        editor.apply();
        ip = m_text;
        connect();
    }

    public void repeat() throws JSONException {
        if(lastMessage != null){
            parseMessage(lastTopic, lastMessage);
        }
    }



    public void setFaceActivity(FaceActivity faceActivity) {
        this.faceActivity = faceActivity;
    }

    public FaceActivity getFaceActivity() {
        return faceActivity;
    }

    public MQTTManager(Context context){  //ws://server:port/mqtt      tcp://151.15.31.217:1883
        System.out.println("Costruttore MQTT..");



        this.context = context;
        System.out.println("Costruttore MQTT Costruito");
        if(test) return;
            connect();


    }

    public boolean isConnected(){
        if(client == null){
            return false;
        }
        return client.isConnected();
    }

    private static String generateClientId(){
        return "id@"+(new Date().getTime());
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void connect()  {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.ip_file), Context.MODE_PRIVATE);
        if(sharedPref != null){
            ip = sharedPref.getString(context.getString(R.string.IP_KEY), "not found");
            System.out.println("SHARED IP: "+ip);
        }

        if(client != null){

            client.close();
        }

        client = new MqttAndroidClient(context, "tcp://"+ip+":1883", clientId);
        //MqttPingSender pingSender = new MqttPingSenderL(this);
        //client = new MqttAsyncClient("tcp://"+ip+":1883", clientId, new MemoryPersistence(), pingSender);
        try {
            final int qos = 1;
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setAutomaticReconnect(true);
            if(client.isConnected()){
                return;
            }
            IMqttToken token = client.connect(mqttConnectOptions);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    System.out.println("Client connesso!");
                    System.out.println("SUCCESSONE");
                    EventManager.getInstance().serverOnline();
                    try {
                        client.subscribe("user/110/to_user/text",qos);
                        client.subscribe("user/110/to_user/face",qos);
                        client.subscribe("user/110/to_user/command",qos);
                        client.subscribe("user/110/to_user/table",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"face",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"table",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"youtube",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"link",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"img",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"listen",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"reminder",qos);
                        client.subscribe(Topics.RESPONSES.getTopic() +"/"+clientId,qos);
                        client.subscribe(Topics.MINDGAME.getTopic()+"/demo/",qos);
                        Settings.getInstance(context,MQTTManager.this); //manda l'username se presente
                        publish(Topics.GETDEVICE.getTopic(),clientId+":"+ DeviceType.MOBILE.getDeviceType());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    exception.printStackTrace();
                    System.out.println("FALLIMENTO TOTALE");
                    EventManager.getInstance().serverOffline();

                    Thread reconnectionThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                                try {
                                    Thread.sleep(10000);
                                    connect();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                        }
                    });
                    //FUNZIONAVA
                    //reconnectionThread.start();


                }
            });

            //SUBSCRIBE



            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    EventManager.getInstance().serverOnline();
                    publish(Topics.GETDEVICE.getTopic(),clientId+":"+ DeviceType.MOBILE.getDeviceType());
                    try {
                        client.subscribe("user/110/to_user/text",qos);

                    client.subscribe("user/110/to_user/face",qos);
                    client.subscribe("user/110/to_user/command",qos);
                    client.subscribe("user/110/to_user/table",qos);
                    client.subscribe("user/110/to_user/link",qos);
                    client.subscribe("user/110/to_user/command/vtable",qos);
                    client.subscribe("user/110/to_user/command/youtube",qos);
                    client.subscribe(Topics.RESPONSES.getTopic() +"/"+clientId,qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"face",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"table",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"youtube",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"link",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"img",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"listen",qos);
                        client.subscribe(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"reminder",qos);
                        client.subscribe(Topics.MINDGAME.getTopic()+"/demo",qos);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection Lost");
                    EventManager.getInstance().serverOffline();

                    //FUNZIONAVA
                    /*
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "reconnecting in 10 seconds", Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            connect();
                        }
                    });
                    */


                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if(!(new String(message.getPayload())).equals("repeat")) {
                        lastMessage = message;
                        lastTopic = topic;
                    }

                    parseMessage(topic,message);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });




        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public String parseMultiText(String multitext){
        String[] tokens = multitext.split("%");
        int nextInt = ThreadLocalRandom.current().nextInt(0, tokens.length);
        return tokens[nextInt];
    }

    public void parseMessage(String topic, MqttMessage message) throws JSONException {
        System.out.println("TOPIC: "+topic);
        if(topic.equals(Topics.RESPONSES.getTopic() +"/"+clientId)){
            faceActivity.forceServerOnline();
            String text = (new String(message.getPayload(),StandardCharsets.UTF_8));
            text.replace("<AT>","@");
            System.out.println("TEXT = "+ text);

            if(text.contains("%")){
               text = parseMultiText(text);
                System.out.println("Multi text detected: ["+text+"] is the chosen one");
            }
            if(text.startsWith("<AUTOLISTEN>")){
                text = text.replace("<AUTOLISTEN>", "");
                faceActivity.speakText(text, true);
            }else {
                faceActivity.speakText(text, false);
            }

        }
        if(topic.endsWith("to_user/command")){
            String text = (new String(message.getPayload()));
            System.out.println("TEXT = "+ text);
            if(text.startsWith("multichoice")) {
                System.out.println("MULTI CHOICE");
                faceActivity.showTestChoice(text);
            }
            if(text.equals("video")) {
                faceActivity.showVideo();
            }
            if(text.equals("test image")) {
                faceActivity.showImage();
            }
            if(text.equals("repeat")) {
                repeat();
            }
            if(text.startsWith("test image ")) {

                faceActivity.showImage(text.split(" ")[2]);
            }

        }
        if(topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"img")){
            String imglink = (new String(message.getPayload()));
            faceActivity.showImage(imglink);
        }
        if(topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"reminder")){
            try {
                String reminderData = (new String(message.getPayload()));

                String reminderText = reminderData.split("<:>")[0];
                String reminderTime = reminderData.split("<:>")[1];
                int hh = Integer.parseInt(reminderTime.split(":")[0]);
                int mm = Integer.parseInt(reminderTime.split(":")[1]);
                // LocalDateTime localDateTime = LocalDate.now().atTime(LocalTime.parse(reminderTime)); API 26
                //System.out.println("LOCAL DATE TIME ALARM -> "+localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));  API 26

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = sdf.parse(reminderTime);

                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);


            }catch(Exception ex){
                ex.printStackTrace();
            }



        }
        if(topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"listen")){
           // Toast.makeText(context, "autoListen set", Toast.LENGTH_LONG).show();
            String mmm = new String(message.getPayload());
            if(mmm.equals("auto")){
                faceActivity.setAutoListen();
            }else {
                Long time = Long.parseLong(mmm);
                faceActivity.listenAt(time);
            }
        }if(topic.equals(Topics.MINDGAME.getTopic()+"/demo")){
            try{
                String mmm = new String(message.getPayload());
                System.out.println("message arrived in MINDGAME/DEMO");
                System.out.println(mmm);
                superMarketBlob = new Gson().fromJson(mmm,SuperMarketBlob.class);
                if(!superMarketBlob.getVocalRequest().isEmpty()) {
                    EventManager.getInstance().setSuperMarketBlob(superMarketBlob);
                    Intent intent = new Intent(EventManager.getInstance().getContext(), SuperMarket.class);
                    EventManager.getInstance().setCorrettiG1(0);
                    EventManager.getInstance().getContext().startActivity(intent);


                    //faceActivity.speakText(superMarketBlob.getInitialMessage() + "." + superMarketBlob.getVocalRequest(), false);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(topic.endsWith("to_user/link") || topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"link")){
            String link = (new String(message.getPayload()));
            System.out.println("Link da mostrare: "+link);
            faceActivity.showLink(link);
        }
        if(topic.endsWith("to_user/table")){
            String tabello = (new String(message.getPayload()));
            System.out.println("TABLE = "+ tabello);
            String[] tabella = tabello.split("<ROW>");
            faceActivity.showTableData(tabella);
        }
        if(topic.endsWith("to_user/command/youtube")|| topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"youtube")){
            System.out.println("richiesta test video youtube standard");
            String m = (new String(message.getPayload(),StandardCharsets.UTF_8));
            if(m.equals("test")) {
                faceActivity.showYouTubeVideo("link farlocco");
            }else{
                System.out.println("sending real video to screen");
                //https://youtu.be/BAVRCFQFeG4
                String id  = m.split("\\.be/")[1];
                System.out.println("youtube video id: "+id);
                faceActivity.showYouTubeVideo(id);
            }
        }
        if(topic.endsWith("to_user/command/vtable")|| topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"table")){
            String tabello = (new String(message.getPayload()));
            System.out.println("TABLE = "+ tabello);
            String[] tabella = tabello.split("<ROW>");
            faceActivity.showGenericTable(tabella);
        }
        if(topic.endsWith("to_user/command/game1")){
            String text = (new String(message.getPayload()));
            System.out.println("TEXT = "+ text);
            String[] tabella = text.split("!");
            faceActivity.showTableData(tabella);
            //test table Laboraratorio di cucina;10:30!Laboratorio di Filatelia;19:00
        }


        if(topic.endsWith("to_user/face") || topic.equals(Topics.COMMAND.getTopic()+"/"+clientId+"/"+"face")){
            String text = (new String(message.getPayload()));

            System.out.println("FACE CHANGE DETECTED: "+text);

            if(text.contains(",")){
                System.out.println("timeout detected");
                Long backToNormalTime = null;
                String stringytime = text.split(",")[1];
                backToNormalTime = Long.parseLong(stringytime);
                faceActivity.setBackToNormalTime(backToNormalTime);
                text = text.split(",")[0];
            }
            if(text.equals("fun")){
                faceActivity.ilRisoAbbonda();
            }
            if(text.equals("love")){
                faceActivity.innamorati();
            }
            if(text.equals("sad")){
                faceActivity.intristiscitiAnimosamente();
            }
            if(text.equals("cry")){
                faceActivity.piangi();
            }
            if(text.equals("question")){
                faceActivity.esprimiQualcheDubbio();
            }
            if(text.equals("rage")){
                System.out.println("SONO FURIOSO, VI SMONTO");
                faceActivity.incazzati();
            }
        }
    }

    public void changeName(String username){
        MqttMessage message = new MqttMessage(username.getBytes(StandardCharsets.UTF_8));
        message.setQos(2);
        message.setRetained(false);

        //String topic = "user/110/from_user";
        String topic = Topics.USERNAME.getTopic() +"/"+clientId;

        try {
            client.publish(topic, message);
            Log.i("mqtt", "Message published");

            // client.disconnect();
            //Log.i("mqtt", "client disconnected");

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void buttonPressed(LoggingTag tag){
        MqttMessage message = new MqttMessage(tag.getTag().getBytes(StandardCharsets.UTF_8));
        message.setQos(2);
        message.setRetained(false);

        //String topic = "user/110/from_user";
        String topic = Topics.BUTTON_PRESSED.getTopic() +"/"+clientId;

        try {
            client.publish(topic, message);
            Log.i("mqtt", "Message published");

            // client.disconBUTTON_PRESSEDnect();
            //Log.i("mqtt", "client disconnected");

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void remoteLog(String text){
        MqttMessage message = new MqttMessage(text.getBytes(StandardCharsets.UTF_8));
        message.setQos(2);
        message.setRetained(false);

        //String topic = "user/110/from_user";
        String topic = Topics.LOG.getTopic() +"/"+clientId;

        try {
            client.publish(topic, message);
            Log.i("mqtt", "Message published");

            // client.disconBUTTON_PRESSEDnect();
            //Log.i("mqtt", "client disconnected");

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void publish(String topic, String text){
        //PUBLISH THE MESSAGE
        MqttMessage message = new MqttMessage(text.getBytes(StandardCharsets.UTF_8));
        message.setQos(2);
        message.setRetained(false);

        //String topic = "user/110/from_user";

        try {
            client.publish(topic, message);
            Log.i(topic, "Message published");

            // client.disconnect();
            //Log.i("mqtt", "client disconnected");

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void publish(String text){
        //PUBLISH THE MESSAGE
        MqttMessage message = new MqttMessage(text.getBytes(StandardCharsets.UTF_8));
        message.setQos(2);
        message.setRetained(false);

        //String topic = "user/110/from_user";
        String topic = Topics.CHAT.getTopic() +"/"+clientId;

        try {
            client.publish(topic, message);
            Log.i("mqtt", "Message published");

            // client.disconnect();
            //Log.i("mqtt", "client disconnected");

        } catch (MqttPersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
