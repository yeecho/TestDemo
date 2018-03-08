package com.yuanye.testdemo;

import java.text.DecimalFormat;

import com.yuanye.testdemo.utils.DataUtil;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends Activity {
	
	private SensorManager sm;
	private Sensor temprature;
	private Sensor humidity;
	private MySensorEventListener listener;
	
	private TextView t1,t2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		t1 = (TextView) findViewById(R.id.temprature);
		t2 = (TextView) findViewById(R.id.humidity);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		temprature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humidity = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        listener = new MySensorEventListener();
        if (temprature == null) {
			Toast.makeText(this, "温感不存在", Toast.LENGTH_SHORT).show();
			float a = 55.55f;
			t1.setText(""+a);
			t2.setText(""+((int) a));
		}else{
			sm.registerListener(listener, temprature, SensorManager.SENSOR_DELAY_NORMAL);
		}
        if (humidity == null) {
			Toast.makeText(this, "湿度传感不存在", Toast.LENGTH_SHORT).show();
		}else{
			sm.registerListener(listener, humidity, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	public class MySensorEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			int type = event.sensor.getType();
        	if(type == Sensor.TYPE_AMBIENT_TEMPERATURE){
        		float temp = event.values[0];
        		DecimalFormat df = new DecimalFormat("#.0");
        		if (temp <= 100) {
        			t1.setText("温度值："+df.format(temp)+"℃");
				}else if(temp >100){
					temp = temp/100;
					t2.setText("湿度值："+df.format(temp)+"%");
				}
        		
        	}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		} 
		
	}
	
	@Override
	protected void onDestroy() {
		sm.unregisterListener(listener);
		super.onDestroy();
	}
	

}
