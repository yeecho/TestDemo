package com.yuanye.testdemo;

import java.util.List;

import com.yuanye.testdemo.service.CopyTestService;
import com.yuanye.testdemo.service.LogcatService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class CopyTestActivity extends Activity implements OnCheckedChangeListener{
	
	private Switch copytest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_copytest);
		copytest = (Switch) findViewById(R.id.switch_copytest);
		copytest.setChecked(isServiceStarted() ? true : false);
		copytest.setOnCheckedChangeListener(this);
	}
	
	private boolean isServiceStarted() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : serviceList) {
			ComponentName service = runningServiceInfo.service;
			String serviceName = service.getClassName();
			if (serviceName.equals("com.yuanye.testdemo.service.CopyTestService")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent service = new Intent(this, CopyTestService.class);
		if (isChecked) {
			startService(service);
		}else{
			stopService(service);
			Toast.makeText(this, "copyservice closed", 1).show();
		}
	}
}
