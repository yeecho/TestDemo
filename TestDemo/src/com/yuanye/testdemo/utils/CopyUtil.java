package com.yuanye.testdemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class CopyUtil {

	private static String tag = "CopyUtil";

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
	
	public static int copy(String fromFile, String toFile)  
    {  
        //Ҫ���Ƶ��ļ�Ŀ¼  
        File[] currentFiles;  
        File root = new File(fromFile);  
        //��ͬ�ж�SD���Ƿ���ڻ����ļ��Ƿ����  
        //����������� return��ȥ  
        if(!root.exists())  
        {  
            return -1;  
        }  
        //����������ȡ��ǰĿ¼�µ�ȫ���ļ� �������  
        currentFiles = root.listFiles();  
           
        //Ŀ��Ŀ¼  
        File targetDir = new File(toFile);  
        //����Ŀ¼  
        if(!targetDir.exists())  
        {  
            targetDir.mkdirs();  
        }  
        //����Ҫ���Ƹ�Ŀ¼�µ�ȫ���ļ�  
        for(int i= 0;i<currentFiles.length;i++)  
        {  
            if(currentFiles[i].isDirectory())//�����ǰ��Ϊ��Ŀ¼ ���еݹ�  
            {  
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");  
                   
            }else//�����ǰ��Ϊ�ļ�������ļ�����  
            {  
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());  
            }  
        }  
        return 0;  
    }  
	
	//�ļ�����  
    //Ҫ���Ƶ�Ŀ¼�µ����з���Ŀ¼(�ļ���)�ļ�����  
    public static int CopySdcardFile(String fromFile, String toFile)  
    {  
           
        try  
        {  
            InputStream fosfrom = new FileInputStream(fromFile);  
            OutputStream fosto = new FileOutputStream(toFile);  
            byte bt[] = new byte[1024];  
            int c;  
            while ((c = fosfrom.read(bt)) > 0)  
            {  
                fosto.write(bt, 0, c);  
            }  
            fosfrom.close();  
            fosto.close(); 
            return 0;  
               
        } catch (Exception e){
        	e.printStackTrace();
            return -1;  
        }  
    }  
}
