package com.yuanye.testdemo.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

public class LogUtil {
	
	public static void saveLog2(){
		ArrayList<String> commandLine = new ArrayList<String>();
        commandLine.add("logcat");
         commandLine.add("-d");//ʹ�øò���������logcat��ȡ��־��Ϻ���ֹ����
         commandLine.add("-v");
         commandLine.add("time");
         commandLine.add("-f");//���ʹ��commandLine.add(">");�ǲ���д���ļ�������ʹ��-f�ķ�ʽ
         commandLine.add("/sdcard/saveLog2.txt");
         try {
			Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveLog(String fileName){
		
		final String cmdReadLog = "logcat -d -v time";
//		final String cmdReadLog = "logcat >> mnt/sdcard/yy.txt";
		final String cmdClearLog = "logcat -c";
		try{
			//������־
			Process process = executeRunTimeCommand(cmdReadLog);   
			//����������ת��ΪBufferedReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));    
//        	Runtime.runFinalizersOnExit(true);
			String str = null;
			int count = 0;
			while((str=bufferedReader.readLine())!=null)    //��ʼ��ȡ��־��ÿ�ζ�ȡһ��
			{
				count++;
//				Log.d("", "count:"+count);
				executeRunTimeCommand(cmdClearLog);  //������־....����������Ҫ��������Ļ����κβ������������µ���־�����������ѭ����ֱ��bufferreader��
				logcat(fileName, str);
//				if (count>1000) {
//					break;
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	};

	//��sdcardд�ļ�
	public static void logcat(String fileName, String content) {
		try {
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//ִ��shell�µ�����
	public static Process executeRunTimeCommand(String arCommand) {
		Process loProcess = null;
		try {
			loProcess = Runtime.getRuntime().exec(arCommand);
//			System.out.println("executeRunTimeCommand " + arCommand);
		} catch (IOException e) {
			Log.e("", e.getMessage());
			return null;
		}
		return loProcess;
	}
}
