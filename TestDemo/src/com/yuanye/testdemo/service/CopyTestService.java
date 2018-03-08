package com.yuanye.testdemo.service;

import com.yuanye.testdemo.CopyTestActivity;
import com.yuanye.testdemo.R;
import com.yuanye.testdemo.utils.CopyUtil;
import com.yuanye.testdemo.utils.LogUtil;
import com.yuanye.testdemo.utils.SysUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CopyTestService extends Service{
	
	protected String tag = "CopyTestService";
	private boolean stop = false;
	private WindowManager wm;
	private View view;// ������ť  
	private TextView tvCopyState,tvCpuState;
	private int startX,startY,endX,endY;
	private AnimationDrawable anim;
	
	private final static String FROMPATH = "/mnt/sdcard/test.mp4/";  
    private final static String TOPATH = "/mnt/sdcard/Movies/test.mp4";  
    private int count = 1;
    
    private Handler handler = new MyHandler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "copyservice started", 1).show();
		
		new Thread(){
			public void run() {
				while (!stop) {
					try {
						if(CopyUtil.CopySdcardFile(count%2==1?FROMPATH:TOPATH, count%2==1?TOPATH:FROMPATH)==0){  
							Log.d(tag , "�������");
							count++;
							handler.sendEmptyMessage(1);
		                }else{  
		                	Log.d(tag , "����ʧ��");
		                	handler.sendEmptyMessage(2);
		                }  
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		new Thread(){
			public void run() {
				while (!stop) {
					try {					
						handler.sendEmptyMessage(3);
						Thread.sleep(4000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		
		createFloatView(50);
	}
	
	@Override
	public void onDestroy() {
		stop = true;
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
        view = LayoutInflater.from(this).inflate(R.layout.window_copytest, null);  
        ImageView iv = (ImageView) view.findViewById(R.id.xhj);
        tvCopyState = (TextView) view.findViewById(R.id.copy_state);
        tvCpuState = (TextView) view.findViewById(R.id.cpu_state);
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

    class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				tvCopyState.setText("��"+count+"�ο������");
				Toast.makeText(CopyTestService.this, "��"+count+"���ļ��������  ", Toast.LENGTH_LONG).show();
			}else if (msg.what == 2) {
				tvCopyState.setText("��"+count+"�ο���ʧ�ܣ�");
				Toast.makeText(CopyTestService.this, "��"+count+"���ļ�����ʧ��  ", Toast.LENGTH_LONG).show();
			}else if (msg.what == 3) {
				String rate = SysUtil.getCPURateDesc();
				String ram = SysUtil.getAvailMemory(CopyTestService.this);
 				tvCpuState.setText(rate + "% "+ram+"%");
			}
		}
    	
    }
   
}
