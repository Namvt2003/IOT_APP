package com.example.iot_application;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;

import com.anychart.AnyChartView;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    private AnyChartView anyChartView;
    private Cartesian cartesian;
    private Cartesian series;
    private MQTTHelper mqttHelper;
    private List<DataEntry> chartData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cartesian = AnyChart.line();
        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        chartData = new ArrayList<>();


        Set set = Set.instantiate();
        set.data(chartData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
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

                byte[] payloadChart = message.getPayload();
                String payloadString = new String(payloadChart, StandardCharsets.UTF_8);

                Log.d("TEST", topic + "***" + payloadString + "MainActitvity2");
                if(topic.equals("tien_le29/feeds/do-an-da-nganh.nhiet-do")){
                    updateTemperatureChart(Float.parseFloat(payloadString));

                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void updateTemperatureChart(float newTemperature) {
//        chartData.add(new CustomDataEntry(System.currentTimeMillis(), newTemperature));
//
//        // Update the chart
//        series.data(chartData);
//
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chartData.add(new CustomDataEntry(System.currentTimeMillis(), newTemperature));
                series.data(chartData);
                anyChartView.setChart(series);
                anyChartView.invalidate();
            }
        });

    }


    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(long x, Number value) {
            super(x, value);

        }

    }
}