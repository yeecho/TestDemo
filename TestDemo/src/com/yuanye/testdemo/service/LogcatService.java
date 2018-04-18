package com.yuanye.testdemo.service;

import com.yuanye.testdemo.LogSaveActivity;
import com.yuanye.testdemo.R;
import com.yuanye.testdemo.utils.LogUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LogcatService extends Service{
	
	private boolean stop = false;
	private WindowManager wm;
	private View view;// ������ť  
	private TextView mTextView;
	private int startX,startY,endX,endY;
	private SharedPreferences mPref;
	private AnimationDrawable anim;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Toast.makeText(this, "LogcatService started", 1).show();
		new Thread(){
			public void run() {
				while (!stop) {
					LogUtil.saveLog("mnt/sdcard/log.txt");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		
//		creatview();
		createFloatView(50);
	}

	private void creatview() {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = new LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		mTextView  = new TextView(this);
		mTextView.setText("Log���������Ѿ�����...");
		mTextView.setTextSize(24);
		mTextView.setTextColor(Color.RED);
		wm.addView(mTextView, params);
	}
	
	@Override
	public void onDestroy() {
		stop = true;
		if (wm != null && mTextView != null) {
			wm.removeViewImmediate(mTextView);
		}
		removeFloatView();
		super.onDestroy();
	}
	
    /** 
     * �������View 
     * @param paddingBottom ����View����Ļ�ײ��ľ��� 
     */  
    protected void createFloatView(int paddingBottom) {  
        int w = 200;// ��С  
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        view = LayoutInflater.from(this).inflate(R.layout.window_logsave, null);  
        ImageView iv = (ImageView) view.findViewById(R.id.xhj);
        iv.setBackgroundResource(R.drawable.xiaoheiji);
        anim = (AnimationDrawable) iv.getBackground();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();  
        params.type = LayoutParams.TYPE_TOAST;// ���г��򴰿ڵġ����ء����ڣ�����Ӧ�ó��򴰿ڶ���ʾ�������档  
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL  
                | LayoutParams.FLAG_NOT_FOCUSABLE;  
        params.format = PixelFormat.TRANSLUCENT;// ����������������͸��������ʾΪ��ɫ  
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;;  
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;  
        params.gravity = Gravity.CENTER_HORIZONTAL;  
        int screenWidth = getResources().getDisplayMetrics().widthPixels;  
        int screenHeight = getResources().getDisplayMetrics().heightPixels;  
//        params.x = screenWidth/2 - view.getWidth()/2;  
//        params.y = screenHeight/2 - view.getHeight()/2;  
//        params.x = screenWidth - w;  
//        params.y = screenHeight - w - paddingBottom;  
        view.setBackgroundColor(Color.TRANSPARENT);  
        view.setVisibility(View.VISIBLE);  
        view.setOnTouchListener(new OnTouchListener() {  
            // ��������  
            float lastX, lastY;  
            int oldOffsetX, oldOffsetY;  
            int tag = 0;// ������ �����Ա����  
  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
            	anim.start();
                final int action = event.getAction();  
                float x = event.getX();  
                float y = event.getY();  
                if (tag == 0) {  
                    oldOffsetX = params.x; // ƫ����  
                    oldOffsetY = params.y; // ƫ����  
                }  
                if (action == MotionEvent.ACTION_DOWN) {  
                    lastX = x;  
                    lastY = y;  
                } else if (action == MotionEvent.ACTION_MOVE) {  
                    params.x += (int) (x - lastX) / 3; // ��Сƫ����,��ֹ���ȶ���  
                    params.y += (int) (y - lastY) / 3; // ��Сƫ����,��ֹ���ȶ���  
                    tag = 1;  
                    wm.updateViewLayout(view, params);  
                } else if (action == MotionEvent.ACTION_UP) {  
                	anim.stop();
                    int newOffsetX = params.x;  
                    int newOffsetY = params.y;  
                    // ֻҪ��ťһ��λ�ò��Ǻܴ�,����Ϊ�ǵ���¼�  
                    if (Math.abs(oldOffsetX - newOffsetX) <= 20  
                            && Math.abs(oldOffsetY - newOffsetY) <= 20) {  
                        onFloatViewClick();  
                    } else {  
                        tag = 0;  
                    }  
                }  
                return true;  
            }  
        });  
        wm.addView(view, params);  
    }  
  
    /** 
     * ���������ť�����¼�����Ҫoverride�÷��� 
     */  
    protected void onFloatViewClick() {  
//    	Log.d("yuanye", "click");
    }  
  
    /** 
     * ������View��WindowManager���Ƴ�����Ҫ��createFloatView()�ɶԳ��� 
     */  
    protected void removeFloatView() {  
        if (wm != null && view != null) {  
            wm.removeViewImmediate(view);  
//          wm.removeView(view);//��Ҫ���������WindowLeaked  
            view = null;  
            wm = null;  
        }  
    }  
    /** 
     * ��������View 
     */  
    protected void hideFloatView() {  
        if (wm != null && view != null&&view.isShown()) {  
            view.setVisibility(View.GONE);  
        }  
    }  
    /** 
     * ��ʾ����View 
     */  
    protected void showFloatView(){  
        if (wm != null && view != null&&!view.isShown()) {  
            view.setVisibility(View.VISIBLE);  
        }  
    }  


}
