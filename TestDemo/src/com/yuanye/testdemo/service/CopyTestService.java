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
	private View view;// 浮动按钮  
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
							Log.d(tag , "拷贝完成");
							count++;
							handler.sendEmptyMessage(1);
		                }else{  
		                	Log.d(tag , "拷贝失败");
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
     * 添加悬浮View 
     * @param paddingBottom 悬浮View与屏幕底部的距离 
     */  
    protected void createFloatView(int paddingBottom) {  
        int w = 200;// 大小  
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        view = LayoutInflater.from(this).inflate(R.layout.window_copytest, null);  
        ImageView iv = (ImageView) view.findViewById(R.id.xhj);
        tvCopyState = (TextView) view.findViewById(R.id.copy_state);
        tvCpuState = (TextView) view.findViewById(R.id.cpu_state);
        iv.setBackgroundResource(R.drawable.xiaoheiji);
        anim = (AnimationDrawable) iv.getBackground();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();  
        params.type = LayoutParams.TYPE_TOAST;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。  
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL  
                | LayoutParams.FLAG_NOT_FOCUSABLE;  
        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色  
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
            // 触屏监听  
            float lastX, lastY;  
            int oldOffsetX, oldOffsetY;  
            int tag = 0;// 悬浮球 所需成员变量  
  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
            	anim.start();
                final int action = event.getAction();  
                float x = event.getX();  
                float y = event.getY();  
                if (tag == 0) {  
                    oldOffsetX = params.x; // 偏移量  
                    oldOffsetY = params.y; // 偏移量  
                }  
                if (action == MotionEvent.ACTION_DOWN) {  
                    lastX = x;  
                    lastY = y;  
                } else if (action == MotionEvent.ACTION_MOVE) {  
                    params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动  
                    params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动  
                    tag = 1;  
                    wm.updateViewLayout(view, params);  
                } else if (action == MotionEvent.ACTION_UP) {  
                	anim.stop();
                    int newOffsetX = params.x;  
                    int newOffsetY = params.y;  
                    // 只要按钮一动位置不是很大,就认为是点击事件  
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
     * 点击浮动按钮触发事件，需要override该方法 
     */  
    protected void onFloatViewClick() {  
//    	Log.d("yuanye", "click");
    }  
  
    /** 
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现 
     */  
    protected void removeFloatView() {  
        if (wm != null && view != null) {  
            wm.removeViewImmediate(view);  
//          wm.removeView(view);//不要调用这个，WindowLeaked  
            view = null;  
            wm = null;  
        }  
    }  
    /** 
     * 隐藏悬浮View 
     */  
    protected void hideFloatView() {  
        if (wm != null && view != null&&view.isShown()) {  
            view.setVisibility(View.GONE);  
        }  
    }  
    /** 
     * 显示悬浮View 
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
				tvCopyState.setText("第"+count+"次拷贝完成");
				Toast.makeText(CopyTestService.this, "第"+count+"次文件拷贝完成  ", Toast.LENGTH_LONG).show();
			}else if (msg.what == 2) {
				tvCopyState.setText("第"+count+"次拷贝失败！");
				Toast.makeText(CopyTestService.this, "第"+count+"次文件拷贝失败  ", Toast.LENGTH_LONG).show();
			}else if (msg.what == 3) {
				String rate = SysUtil.getCPURateDesc();
				String ram = SysUtil.getAvailMemory(CopyTestService.this);
 				tvCpuState.setText(rate + "% "+ram+"%");
			}
		}
    	
    }
   
}
