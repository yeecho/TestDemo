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
         commandLine.add("-d");//使用该参数可以让logcat获取日志完毕后终止进程
         commandLine.add("-v");
         commandLine.add("time");
         commandLine.add("-f");//如果使用commandLine.add(">");是不会写入文件，必须使用-f的方式
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
			//捕获日志
			Process process = executeRunTimeCommand(cmdReadLog);   
			//将捕获内容转换为BufferedReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));    
//        	Runtime.runFinalizersOnExit(true);
			String str = null;
			int count = 0;
			while((str=bufferedReader.readLine())!=null)    //开始读取日志，每次读取一行
			{
				count++;
//				Log.d("", "count:"+count);
				executeRunTimeCommand(cmdClearLog);  //清理日志....这里至关重要，不清理的话，任何操作都将产生新的日志，代码进入死循环，直到bufferreader满
				logcat(fileName, str);
//				if (count>1000) {
//					break;
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	};

	//往sdcard写文件
	public static void logcat(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//执行shell下的命令
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
