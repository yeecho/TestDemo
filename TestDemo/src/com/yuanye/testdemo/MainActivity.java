package com.yuanye.testdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yuanye.testdemo.adapter.MainAdapter;
import com.yuanye.testdemo.service.LogcatService;
import com.yuanye.testdemo.utils.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity implements OnItemClickListener{
	
	private ListView mListView;
	private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

	private void init() {
		setContentView(R.layout.activity_main);
		String[] names = getResources().getStringArray(R.array.main_adapter_item_name);
		String[] values = getResources().getStringArray(R.array.main_adapter_item_value);
		List<String> listName = new ArrayList<String>();
		list = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			listName.add(names[i]);
		}
		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}
        mListView = (ListView) findViewById(R.id.lv_main);
        mListView.setAdapter(new MainAdapter(this, listName));
        mListView.setOnItemClickListener(this);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		String key = list.get(position);
		if (key.equals("StressTest")) {
			try{
				intent.setClassName("com.cghs.stresstest","com.cghs.stresstest.StressTestActivity");
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(this, "没有找到压力测试apk", Toast.LENGTH_SHORT).show();
			}
		}else if (key.equals("LogSave")) {
			intent.setClass(this, LogSaveActivity.class);
		}else if (key.equals("DragTest")) {
			intent.setClass(this, DragTestActivity.class);
		}else if (key.equals("SensorTest")) {
			intent.setClass(this, SensorActivity.class);
		}else if (key.equals("CopyTest")) {
			intent.setClass(this, CopyTestActivity.class);
	    }else if (key.equals("VibrateTest")) {
			intent.setClass(this, VibrateTestActivity.class);
	    }
		startActivity(intent);
	}
}
