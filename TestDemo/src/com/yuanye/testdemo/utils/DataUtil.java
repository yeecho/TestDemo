package com.yuanye.testdemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;

public class DataUtil {
	
	private static String TAG = DataUtil.class.getSimpleName();
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	//zhangsx
	public static String generate_128C(String input){
        StringBuilder text = new StringBuilder();//text是输出的结果
        text.append(code128[105][3]);//添加一个StartCodeC
        int currentFlag = 2;//0:128A, 1:128B, 2:128C
        int index = 1;//用于检验位的计算
        int examine = 0;//检验位
        if (isNumeric(input.subSequence(0, 1).toString()) && isNumeric(input.subSequence(1, 2).toString())){
            examine += 105;//以128C开始
            currentFlag = 2;
        }else {
            //以128B开始
            examine += 104;
            currentFlag = 1;
        }

        while (input.length() != 0){
            if (isNumeric(input.subSequence(0, 1).toString()) && isNumeric(input.subSequence(1, 2).toString())){
                if (currentFlag == 2){
                    text.append(transformC(input.subSequence(0, 2).toString()));
                    examine += (Integer.parseInt(input.subSequence(0, 2).toString()) * index);
                    index += 1;
                    input = input.substring(2);
                }else if(currentFlag == 1){
                    index = 1;
                    examine += 105;
                    examine += (Integer.parseInt(input.subSequence(0, 2).toString()) * index);
                    index += 1;
                    text.append(code128[99][3]);//128B转128C
                    text.append(transformC(input.subSequence(0, 2).toString()));
                    input = input.substring(2);
                    currentFlag = 2;
                }
            }else{
                String[] strings = transformB(input.subSequence(0, 1).toString());
                if (currentFlag == 1){
                    examine += (Integer.parseInt(strings[0]) * index);
                    index += 1;
                    text.append(strings[1]);
                    input = input.substring(1);
                    
                    strings = transformB(input.subSequence(0, 1).toString());
                    examine += (Integer.parseInt(strings[0]) * index);
                    index += 1;
                    text.append(strings[1]);
                    input = input.substring(1);
                }else if(currentFlag == 2){
                    currentFlag = 1;
                    index = 1;
                    examine += 104;
                    examine += (Integer.parseInt(strings[0]) * index);
                    index += 1;
                    text.append(code128[100][3]);//128C转128B
                    text.append(strings[1]);
                    input = input.substring(1);
                    
                    strings = transformB(input.subSequence(0, 1).toString());
                    examine += (Integer.parseInt(strings[0]) * index);
                    index += 1;
                    text.append(strings[1]);
                    input = input.substring(1);
                }
            }
        }
        examine %= 103;
        text.append(code128[examine][3]);
        text.append(code128[106][3]);//添加Stop
        Log.e(TAG, "examine = " + String.valueOf(examine));
        Log.e(TAG, "text = " + text.toString());
        return text.toString();
    }
    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private static String transformC(String str){
        int tempValue = Integer.valueOf(str);
        return code128[tempValue][3];
    }
    private static String[] transformB(String str){
        String[] strings = new String[2];
        for (int i = 0; i < code128.length; i++){
            if (code128[i][1].equals(str)){
                strings[0] = code128[i][2];
                strings[1] = code128[i][3];
                return strings;
            }
        }
        return null;
    }
//    public static String getValue(int i, int j){
//        return code128[i][j];
//    }


    private static String[][] code128 = { { " ", " ", "00", "212222", "11011001100" },
            { "!", "!", "01", "222122", "11001101100" },
            { "\"", "\"", "02", "222221", "11001100110" },
            { "#", "#", "03", "121223", "10010011000" },
            { "$", "$", "04", "121322", "10010001100" },
            { "%", "%", "05", "131222", "10001001100" },
            { "&", "&", "06", "122213", "10011001000" },
            { "'", "'", "07", "122312", "10011000100", },
            { "(", "(", "08", "132212", "10001100100" },
            { ")", ")", "09", "221213", "11001001000" },
            { "*", "*", "10", "221312", "11001000100" },
            { "+", "+", "11", "231212", "11000100100" },
            { ",", ",", "12", "112232", "10110011100" },
            { "-", "-", "13", "122132", "10011011100" },
            { ".", ".", "14", "122231", "10011001110" },
            { "/", "/", "15", "113222", "10111001100" },
            { "0", "0", "16", "123122", "10011101100" },
            { "1", "1", "17", "123221", "10011100110" },
            { "2", "2", "18", "223211", "11001110010", },
            { "3", "3", "19", "221132", "11001011100" },
            { "4", "4", "20", "221231", "11001001110", },
            { "5", "5", "21", "213212", "11011100100" },
            { "6", "6", "22", "223112", "11001110100" },
            { "7", "7", "23", "312131", "11101101110" },
            { "8", "8", "24", "311222", "11101001100" },
            { "9", "9", "25", "321122", "11100101100" },
            { ":", ":", "26", "321221", "11100100110" },
            { ";", ";", "27", "312212", "11101100100" },
            { "<", "<", "28", "322112", "11100110100" },
            { "=", "=", "29", "322211", "11100110010" },
            { ">", ">", "30", "212123", "11011011000" },
            { "?", "?", "31", "212321", "11011000110" },
            { "@", "@", "32", "232121", "11000110110" },
            { "A", "A", "33", "111323", "10100011000" },
            { "B", "B", "34", "131123", "10001011000" },
            { "C", "C", "35", "131321", "10001000110" },
            { "D", "D", "36", "112313", "10110001000" },
            { "E", "E", "37", "132113", "10001101000" },
            { "F", "F", "38", "132311", "10001100010" },
            { "G", "G", "39", "211313", "11010001000" },
            { "H", "H", "40", "231113", "11000101000" },
            { "I", "I", "41", "231311", "11000100010" },
            { "J", "J", "42", "112133", "10110111000" },
            { "K", "K", "43", "112331", "10110001110" },
            { "L", "L", "44", "132131", "10001101110" },
            { "M", "M", "45", "113123", "10111011000" },
            { "N", "N", "46", "113321", "10111000110" },
            { "O", "O", "47", "133121", "10001110110" },
            { "P", "P", "48", "313121", "11101110110" },
            { "Q", "Q", "49", "211331", "11010001110" },
            { "R", "R", "50", "231131", "11000101110" },
            { "S", "S", "51", "213113", "11011101000" },
            { "T", "T", "52", "213311", "11011100010" },
            { "U", "U", "53", "213131", "11011101110" },
            { "V", "V", "54", "311123", "11101011000" },
            { "W", "W", "55", "311321", "11101000110" },
            { "X", "X", "56", "331121", "11100010110" },
            { "Y", "Y", "57", "312113", "11101101000" },
            { "Z", "Z", "58", "312311", "11101100010" },
            { "[", "[", "59", "332111", "11100011010" },
            { "\\", "\\", "60", "314111", "11101111010" },
    { "]", "]", "61", "221411", "11001000010" },
    { "^", "^", "62", "431111", "11110001010" },
    { "_", "_", "63", "111224", "10100110000" },
    { "NUL", "`", "64", "111422", "10100001100" },
    { "SOH", "a", "65", "121124", "10010110000" },
    { "STX", "b", "66", "121421", "10010000110" },
    { "ETX", "c", "67", "141122", "10000101100" },
    { "EOT", "d", "68", "141221", "10000100110" },
    { "ENQ", "e", "69", "112214", "10110010000" },
    { "ACK", "f", "70", "112412", "10110000100" },
    { "BEL", "g", "71", "122114", "10011010000" },
    { "BS", "h", "72", "122411", "10011000010" },
    { "HT", "i", "73", "142112", "10000110100" },
    { "LF", "j", "74", "142211", "10000110010" },
    { "VT", "k", "75", "241211", "11000010010" },
    { "FF", "I", "76", "221114", "11001010000" },
    { "CR", "m", "77", "413111", "11110111010" },
    { "SO", "n", "78", "241112", "11000010100" },
    { "SI", "o", "79", "134111", "10001111010" },
    { "DLE", "p", "80", "111242", "10100111100" },
    { "DC1", "q", "81", "121142", "10010111100" },
    { "DC2", "r", "82", "121241", "10010011110" },
    { "DC3", "s", "83", "114212", "10111100100" },
    { "DC4", "t", "84", "124112", "10011110100" },
    { "NAK", "u", "85", "124211", "10011110010" },
    { "SYN", "v", "86", "411212", "11110100100" },
    { "ETB", "w", "87", "421112", "11110010100" },
    { "CAN", "x", "88", "421211", "11110010010" },
    { "EM", "y", "89", "212141", "11011011110" },
    { "SUB", "z", "90", "214121", "11011110110" },
    { "ESC", "{", "91", "412121", "11110110110" },
    { "FS", "|", "92", "111143", "10101111000" },
    { "GS", "},", "93", "111341", "10100011110" },
    { "RS", "~", "94", "131141", "10001011110" },
    { "US", "DEL", "95", "114113", "10111101000" },
    { "FNC3", "FNC3", "96", "114311", "10111100010" },
    { "FNC2", "FNC2", "97", "411113", "11110101000" },
    { "SHIFT", "SHIFT", "98", "411311", "11110100010" },
    { "CODEC", "CODEC", "99", "113141", "10111011110" },
    { "CODEB", "FNC4", "CODEB", "114131", "10111101110" },
    { "FNC4", "CODEA", "CODEA", "311141", "11101011110" },
    { "FNC1", "FNC1", "FNC1", "411131", "11110101110" },
    { "StartA", "StartA", "StartA", "211412", "11010000100" },
    { "StartB", "StartB", "StartB", "211214", "11010010000" },
    { "StartC", "StartC", "StartC", "211232", "11010011100" },
    { "Stop", "Stop", "Stop", "2331112", "1100011101011" }, };
    
    //计算字符串MD5值
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    //计算文件的MD5值
    public static String md5(File file) {
        String result = "";
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            byte[] bytes = md5.digest();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    //多次MD5加密
    public static String md5(String string, int times) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String md5 = md5(string);
        for (int i = 0; i < times - 1; i++) {
            md5 = md5(md5);
        }
        return md5(md5);
    }
    
    //MD5加油
    public static String md5(String string, String slat) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
	public static String getNodeString(String path) {
        String prop = "";// 默认值
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            prop = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

}
