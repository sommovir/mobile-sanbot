package it.cnr.mobilebot;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * Created by Luca Coraci [luca.coraci@istc.cnr.it] on 18/06/2020.
 */
public class MQTTManager {

    String clientId = "user-110";//MqttClient.generateClientId();
    MqttAndroidClient client = null;
    boolean test = false;
    private FaceActivity faceActivity = null;

    public void setFaceActivity(FaceActivity faceActivity) {
        this.faceActivity = faceActivity;
    }

    public MQTTManager(Context context){  //ws://server:port/mqtt      tcp://151.15.31.217:1883
        System.out.println("Costruttore MQTT..");
        client = new MqttAndroidClient(context, "tcp://192.168.43.111:1883",
                clientId);
        System.out.println("Costruttore MQTT Costruito");
        if(test) return;

        try {
            final int qos = 1;
            IMqttToken token = client.connect();

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    System.out.println("Client connesso!");
                    System.out.println("SUCCESSONE");
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

                }
            });

            //SUBSCRIBE



            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println("TOPIC: "+topic);
                    if(topic.equals(Topics.RESPONSES.getTopic() +"/"+clientId)){
                        String text = (new String(message.getPayload()));
                        System.out.println("TEXT = "+ text);
                        faceActivity.speakText(null,text);

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
                    }
                    if(topic.endsWith("game1")){
                        String text = (new String(message.getPayload()));
                        System.out.println("TEXT = "+ text);
                        String[] tabella = text.split("!");
                        faceActivity.showTableData(tabella);
                    }
                    if(topic.endsWith("table")){
                        String text = (new String(message.getPayload()));
                        System.out.println("TEXT = "+ text);
                        String[] tabella = text.split("!");
                        faceActivity.showTableData(tabella);
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
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });




        } catch (MqttException e) {
            e.printStackTrace();
        }
    }




    public void publish(String text){
        //PUBLISH THE MESSAGE
        MqttMessage message = new MqttMessage(text.getBytes());
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
