package it.cnr.mobilebot.logic;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttClientUpgraded extends MqttAndroidClient {
    public MqttClientUpgraded(Context context, String serverURI, String clientId) {
        super(context, serverURI, clientId);
    }

    //public MqttClientUpgraded(String serverURI, String clientId, MqttClientPersistence persistence, MqttPingSender pingSender) throws MqttException {
    //    aClient = new MqttAsyncClient(serverURI, clientId, persistence, pingSender);
   // }
}
