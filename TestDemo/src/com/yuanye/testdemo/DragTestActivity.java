package com.yuanye.testdemo;
import com.yuanye.testdemo.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class DragTestActivity extends Activity implements OnTouchListener{
	
	private LinearLayout llDrag;
	private int startX,startY,endX,endY;
	private SharedPreferences mPref;
	
	private TextView mTextView;
	private TextView mTextView2;
	
	//控制偏移量，用于自定义点击动作的条件
	int tag = 0;
	int oldOffsetX, oldOffsetY;
	private int width;
	private int height;  

	long[] mHits = new long[4];//三击数组
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragtest);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		Point p = new Point();
		getWindowManager().getDefaultDisplay().getSize(p);
		width = p.x;
		height = p.y;
		
		llDrag = (LinearLayout) findViewById(R.id.ll_drag);
		mTextView = (TextView) findViewById(R.id.tvtest);
		mTextView2 = (TextView) findViewById(R.id.tvtest2);
		mTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				mHits[mHits.length-1] = SystemClock.uptimeMillis();
				if ((mHits[mHits.length-1] - mHits[0]) <= 500) {
					//多击成功
					Animation shake = AnimationUtils.loadAnimation(DragTestActivity.this, R.anim.shake);
					mTextView.startAnimation(shake);
				}
			}
		});
		
		if (lastY < height/2) {
			mTextView.setVisibility(View.INVISIBLE);
			mTextView2.setVisibility(View.VISIBLE);
		}
		
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llDrag.getLayoutParams();
		layoutParams.leftMargin = lastX;
		layoutParams.topMargin = lastY;
		llDrag.setLayoutParams(layoutParams);
		llDrag.setOnTouchListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (tag == 0) {
				oldOffsetX = (int) llDrag.getX();
				oldOffsetY = (int) llDrag.getY();
			}
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			endX = (int) event.getRawX();
			endY = (int) event.getRawY();
			int dx = endX - startX;
			int dy = endY - startY;
			int dz = 1;
			int l = llDrag.getLeft() + dx/dz;
			int r = llDrag.getRight() + dx/dz;
			int t = llDrag.getTop() + dy/dz;
			int b = llDrag.getBottom() + dy/dz;
			Log.d("", llDrag.getLeft()+" "+ llDrag.getRight()+" "+llDrag.getTop()+" "+llDrag.getBottom());
			if (l < 0 || r > width || t < 0 || b > height - 30){
				break;
			}
			if (t < height/2) {
				mTextView.setVisibility(View.INVISIBLE);
				mTextView2.setVisibility(View.VISIBLE);
			}else{
				mTextView.setVisibility(View.VISIBLE);
				mTextView2.setVisibility(View.INVISIBLE);
			}
			llDrag.layout(l, t, r, b);
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			tag = 1;
			break;
		case MotionEvent.ACTION_UP:
			
			
			int newOffsetX = (int) llDrag.getX();  
            int newOffsetY = (int) llDrag.getY(); 
            if (Math.abs(oldOffsetX - newOffsetX) <= 20  
                    && Math.abs(oldOffsetY - newOffsetY) <= 20) {  
                onViewClick();  
            } else {  
            	Editor edit = mPref.edit();
            	edit.putInt("lastX", llDrag.getLeft());
            	edit.putInt("lastY", llDrag.getTop());
            	edit.commit();
            	
                tag = 0;  
            }  
			
			break;

		default:
			break;
		}
		return true;
	}

	private void onViewClick() {
		Log.d("yuanye", "click");
	}
	
}
