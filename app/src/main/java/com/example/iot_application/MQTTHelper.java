package com.example.iot_application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MQTTHelper {
    public MqttAndroidClient mqttAndroidClient;

    public final String[] arrayTopics = {"tien_le29/feeds/do-an-da-nganh.gdd", "tien_le29/feeds/do-an-da-nganh.anh-sang", "tien_le29/feeds/do-an-da-nganh.bong-den", "tien_le29/feeds/do-an-da-nganh.do-am", "tien_le29/feeds/do-an-da-nganh.do-am-dat",  "tien_le29/feeds/do-an-da-nganh.nhan-dien-ai", "tien_le29/feeds/do-an-da-nganh.nhiet-do","tien_le29/feeds/do-an-da-nganh.may-bom"};

    final String clientId = "12345678";
    final String username ="";
    final String password = "";
    MemoryPersistence memPer;
    MqttClient client;
    final String serverUri = "tcp://io.adafruit.com:1883";

    public MQTTHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {



            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.d("MQTT", "MQTT Server connection lost" + throwable.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//                Log.w("Mqtt", mqttMessage.toString());
                Log.d("TEST", topic + "***" + mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                try {
                    Log.d("MQTT", "Delivery complete for message: "  );
                } catch (Exception e) {
                    Log.e("MQTT", "Error in delivery completion", e);
                }
            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        for (String arrayTopic : arrayTopics) {
            try {
                mqttAndroidClient.subscribe(arrayTopic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d("TEST", "Subscribed!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("TEST", "Subscribed fail!");
                    }
                });

            } catch (MqttException ex) {
                System.err.println("Exceptionst subscribing");
                ex.printStackTrace();
            }
        }
    }
    public void publicMessage(String topic, String msg) throws Exception{

            Log.d("TEST", topic + "***" + msg);
            mqttAndroidClient.publish(topic, new MqttMessage(msg.getBytes()));
    }
}