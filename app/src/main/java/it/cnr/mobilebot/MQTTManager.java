package it.cnr.mobilebot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import it.cnr.mobilebot.logic.EventManager;
import it.cnr.mobilebot.logic.MqttPingSenderL;

/**
 * Created by Luca Coraci [luca.coraci@istc.cnr.it] on 18/06/2020.
 */
public class MQTTManager {

    private static final String clientId = generateClientId(); // "user-110";
    MqttAndroidClient client = null;
    //MqttAsyncClient client = null;
    boolean test = false;
    private FaceActivity faceActivity = null;
    private static Context context = null;
    public static String ip = "192.168.43.112";
    public MqttMessage lastMessage = null;
    public String lastTopic = null;

    public void updateIP(String m_text) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.ip_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.IP_KEY), m_text);
        editor.apply();
        ip = m_text;
        connect();
    }

    public void repeat(){
        if(lastMessage != null){
            parseMessage(lastTopic, lastMessage);
        }
    }

    public void setFaceActivity(FaceActivity faceActivity) {
        this.faceActivity = faceActivity;
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
                        client.subscribe(Topics.RESPONSES.getTopic() +"/"+clientId,qos);
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
                    try {
                        client.subscribe("user/110/to_user/text",qos);

                    client.subscribe("user/110/to_user/face",qos);
                    client.subscribe("user/110/to_user/command",qos);
                    client.subscribe("user/110/to_user/command/table",qos);
                    client.subscribe("user/110/to_user/command/vtable",qos);
                    client.subscribe(Topics.RESPONSES.getTopic() +"/"+clientId,qos);
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


    public void parseMessage(String topic, MqttMessage message){
        System.out.println("TOPIC: "+topic);
        if(topic.equals(Topics.RESPONSES.getTopic() +"/"+clientId)){
            String text = (new String(message.getPayload(),StandardCharsets.UTF_8));
            System.out.println("TEXT = "+ text);
            faceActivity.speakText(text);

        }
        if(topic.endsWith("command")){
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
        if(topic.endsWith("//table")){
            String tabello = (new String(message.getPayload()));
            System.out.println("TABLE = "+ tabello);
            String[] tabella = tabello.split("!");
            faceActivity.showTableData(tabella);
        }
        if(topic.endsWith("vtable")){
            String tabello = (new String(message.getPayload()));
            System.out.println("TABLE = "+ tabello);
            String[] tabella = tabello.split("!");
            faceActivity.showGenericTable(tabella);
        }
        if(topic.endsWith("game1")){
            String text = (new String(message.getPayload()));
            System.out.println("TEXT = "+ text);
            String[] tabella = text.split("!");
            faceActivity.showTableData(tabella);
            //test table Laboraratorio di cucina;10:30!Laboratorio di Filatelia;19:00
        }


        if(topic.endsWith("face")){
            String text = (new String(message.getPayload()));
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
                faceActivity.incazzati();
            }
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
