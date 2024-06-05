package com.example.iot_application;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
//import androidx.activity.EdgeToEdge;
//import Log
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, air_quality, txtSoil,  txtAI, txtTemp2,txtLux;
    SwitchCompat txtPump,txtLight;
    private MqttAndroidClient mqttAndroidClient;

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

        txtLux = findViewById(R.id.lux);
        txtLight = findViewById(R.id.light);
        txtPump = findViewById(R.id.turbine);
        txtSoil = findViewById(R.id.soil);
        txtAI = findViewById(R.id.AI);
        Button btnGoToTempGraph = findViewById(R.id.btnGoToTempGraph);

        btnGoToTempGraph.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        Button btnGoToHumidGraph = findViewById(R.id.btnGoToHumidityGraph);
        btnGoToHumidGraph.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(intent);
        });

        Button btnGoToLightGraph = findViewById(R.id.btnGoToLightGraph);
        btnGoToLightGraph.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity4.class);
            startActivity(intent);
        });

        Button btnGoToSoilGraph = findViewById(R.id.btnGoToSoilGraph);
        btnGoToSoilGraph.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity5.class);
            startActivity(intent);
        });


        setupUI();
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
                    checkTemperatureRange(Double.parseDouble(message.toString()));
                } else if(topic.equals("tien_le29/feeds/do-an-da-nganh.do-am")){
                    txtHumid.setText(message.toString() + "%");
                    checkHumidityRange(Double.parseDouble(message.toString()));
                } else if(topic.equals("tien_le29/feeds/do-an-da-nganh.do-am-dat")){
                    txtSoil.setText(message.toString() + "%");
                    checkSoilMoistureRange(Double.parseDouble(message.toString()));
                } else if (topic.equals("tien_le29/feeds/do-an-da-nganh.nhan-dien-ai")){
                    txtAI.setText(message.toString());

                }else if(topic.equals("tien_le29/feeds/do-an-da-nganh.anh-sang")){
                    txtLux.setText(message.toString() + "lux");
                    checkLuxRange(Double.parseDouble(message.toString()));
                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    public void setupUI(){
        txtPump = findViewById(R.id.turbine);
        txtPump.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String topic = "tien_le29/feeds/do-an-da-nganh.may-bom";
            String message = isChecked ? "1" : "0";
            try {
                mqttHelper.publishMessage(topic, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


         txtLight= findViewById(R.id.light);
        txtLight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String topic = "tien_le29/feeds/do-an-da-nganh.bong-den";
            String message = isChecked ? "1" : "0";
            try {
                mqttHelper.publishMessage(topic, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



    }



    private void checkTemperatureRange(double temperature) {
        if (temperature < 0 || temperature > 100) {
            showAlert("Temperature out of range", "The temperature is " + temperature + "°C. Please check the system.");
        }
    }

    private void checkHumidityRange(double humidity) {
        if (humidity < 20 || humidity > 80) {
            showAlert("Humidity out of range", "The humidity is " + humidity + "%. Please check the system.");
        }
    }

    private void checkSoilMoistureRange(double soilMoisture) {
        if (soilMoisture < 10 || soilMoisture > 90) {
            showAlert("Soil moisture out of range", "The soil moisture is " + soilMoisture + "%. Please check the system.");
        }
    }

    private void checkLuxRange(double lux) {
        if (lux < 100 || lux > 10000) {
            showAlert("Light level out of range", "The light level is " + lux + " lux. Please check the system.");
        }
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}