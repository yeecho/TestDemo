package com.yuanye.testdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yuanye.testdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<String> list;

	public MainAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_main_adapter, null);
		}
		TextView tvTestName = (TextView) convertView.findViewById(R.id.tv_test_name);
		tvTestName.setText(list.get(position));
		return convertView;
	}

}
