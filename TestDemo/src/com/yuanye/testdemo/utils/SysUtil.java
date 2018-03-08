package com.yuanye.testdemo.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

@SuppressLint("NewApi") public class SysUtil {
	
	public static String getCPURateDesc(){  
	    String path = "/proc/stat";// 系统CPU信息文件  
	    long totalJiffies[]=new long[2];  
	    long totalIdle[]=new long[2];  
	    int firstCPUNum=0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准  
	    FileReader fileReader = null;  
	    BufferedReader bufferedReader = null;  
	    Pattern pattern=Pattern.compile(" [0-9]+");  
	    for(int i=0;i<2;i++) {  
	        totalJiffies[i]=0;  
	        totalIdle[i]=0;  
	        try {  
	            fileReader = new FileReader(path);  
	            bufferedReader = new BufferedReader(fileReader, 8192);  
	            int currentCPUNum=0;  
	            String str;  
	            while ((str = bufferedReader.readLine()) != null&&(i==0||currentCPUNum<firstCPUNum)) {  
	                if (str.toLowerCase().startsWith("cpu")) {  
	                    currentCPUNum++;  
	                    int index = 0;  
	                    Matcher matcher = pattern.matcher(str);  
	                    while (matcher.find()) {  
	                        try {  
	                            long tempJiffies = Long.parseLong(matcher.group(0).trim());  
	                            totalJiffies[i] += tempJiffies;  
	                            if (index == 3) {//空闲时间为该行第4条栏目  
	                                totalIdle[i] += tempJiffies;  
	                            }  
	                            index++;  
	                        } catch (NumberFormatException e) {  
	                            e.printStackTrace();  
	                        }  
	                    }  
	                }  
	                if(i==0){  
	                    firstCPUNum=currentCPUNum;  
	                    try {//暂停50毫秒，等待系统更新信息。  
	                        Thread.sleep(50);
	                    } catch (InterruptedException e) {  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } finally {  
	            if (bufferedReader != null) {  
	                try {  
	                    bufferedReader.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }  
	    double rate=-1;  
	    if (totalJiffies[0]>0&&totalJiffies[1]>0&&totalJiffies[0]!=totalJiffies[1]){  
	        rate=1.0*((totalJiffies[1]-totalIdle[1])-(totalJiffies[0]-totalIdle[0]))/(totalJiffies[1]-totalJiffies[0]);  
	    }  
	  
	    return String.format("cpu:%.1f",rate*100);  
	}  

	public static String getAvailMemory(Context mContext) {  
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);  
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();  
        am.getMemoryInfo(mi);  
        float rate = (float) (mi.totalMem - mi.availMem)/mi.totalMem;
//        String availMem = String.format("%.2f MB", (float) mi.availMem / (1024*1024)); 
//        String totalMem = String.format("%.2f MB", (float) mi.totalMem / (1024*1024)); 
        return String.format("ram:%.1f", rate*100);   
    } 
}
