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
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GraphView;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
        chartData.add(new CustomDataEntry("1986", 3.6, 2.3, 2.8));
       chartData.add(new CustomDataEntry("1987", 7.1, 4.0, 4.1));
       chartData.add(new CustomDataEntry("1988", 8.5, 6.2, 5.1));
       chartData.add(new CustomDataEntry("1989", 9.2, 11.8, 6.5));
       chartData.add(new CustomDataEntry("1990", 10.1, 13.0, 12.5));
       chartData.add(new CustomDataEntry("1991", 11.6, 13.9, 18.0));
       chartData.add(new CustomDataEntry("1992", 16.4, 18.0, 21.0));
       chartData.add(new CustomDataEntry("1993", 18.0, 23.3, 20.3));
       chartData.add(new CustomDataEntry("1994", 13.2, 24.7, 19.2));
       chartData.add(new CustomDataEntry("1995", 12.0, 18.0, 14.4));
       chartData.add(new CustomDataEntry("1996", 3.2, 15.1, 9.2));
       chartData.add(new CustomDataEntry("1997", 4.1, 11.3, 5.9));
       chartData.add(new CustomDataEntry("1998", 6.3, 14.2, 5.2));
       chartData.add(new CustomDataEntry("1999", 9.4, 13.7, 4.7));
       chartData.add(new CustomDataEntry("2000", 11.5, 9.9, 4.2));
       chartData.add(new CustomDataEntry("2001", 13.5, 12.1, 1.2));
       chartData.add(new CustomDataEntry("2002", 14.8, 13.5, 5.4));
       chartData.add(new CustomDataEntry("2003", 16.6, 15.1, 6.3));
       chartData.add(new CustomDataEntry("2004", 18.1, 17.9, 8.9));
       chartData.add(new CustomDataEntry("2005", 17.0, 18.9, 10.1));
       chartData.add(new CustomDataEntry("2006", 16.6, 20.3, 11.5));
       chartData.add(new CustomDataEntry("2007", 14.1, 20.7, 12.2));
       chartData.add(new CustomDataEntry("2008", 15.7, 21.6, 10));
       chartData.add(new CustomDataEntry("2009", 12.0, 22.5, 8.9));
        Set set = Set.instantiate();
        set.data(chartData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

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

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Whiskey");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Tequila");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);


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
                    updateTemperatureChart(Float.parseFloat(message.toString()));
                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    public void updateTemperatureChart(float newTemperature) {
        chartData.add(new ValueDataEntry(System.currentTimeMillis(), newTemperature));

        // Update the chart
        series.data(chartData);
        anyChartView.setChart(series);

    }


    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }
}