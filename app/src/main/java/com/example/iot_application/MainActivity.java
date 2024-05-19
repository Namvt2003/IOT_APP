package com.example.iot_application;

import android.os.Bundle;

//import androidx.activity.EdgeToEdge;
//import Log
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, air_quality, txtLight, txtSoil, txtPump, txtAI, txtTemp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtTemp = findViewById(R.id.temperature);
        txtHumid = findViewById(R.id.humidity);
        air_quality = findViewById(R.id.air_quality);
        startMQTT();
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if(topic.equals("tien_le29/feeds/do-an-da-nganh.nhiet-do")){
                    txtTemp.setText(message.toString() + "°C");
                } else if(topic.equals("tien_le29/feeds/do-an-da-nganh.do-am")){
                    txtHumid.setText(message.toString() + "%");
                } else if(topic.equals("tien_le29/feeds/do-an-da-nganh.air-quality")){
                    air_quality.setText(message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}