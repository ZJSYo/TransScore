package com.example.demossm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/26 15:01
 */

public final class FileUtils {
    public static ObjectMapper mapper = new ObjectMapper();

    public enum FileType {
        AUDIO, TEXT, IMAGE
    }
    public static final String AUDIO_FOLDER_PATH = "/home/zhang/下载/软件工程/mp3_output/";
    public  static final String SHEET_FOLDER_PATH = "/home/zhang/下载/软件工程/png_output/";
    public  static final String TEXT_FOLDER_PATH = "/home/zhang/下载/软件工程/input_text";

    private FileUtils() {
    }

    public static String getTempName(String BASE_PATH, FileType type) {
        return switch (type) {
            case AUDIO -> BASE_PATH + "audio_" + System.currentTimeMillis() + ".mp3";
            case TEXT -> BASE_PATH + "text_" + System.currentTimeMillis() + ".txt";
            case IMAGE -> BASE_PATH + "image_" + System.currentTimeMillis() + ".jpg";
            default -> BASE_PATH + "temp_" + System.currentTimeMillis() + ".txt";
        };

    }

    public static String getBaseImg(String imgPath) {
        InputStream in;
        byte[] data = null;
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //进行Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);

    }
    public static void saveText(String text,String fileName){
        try {
            java.io.FileWriter fw = new java.io.FileWriter(fileName);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getFileName(String path){
        String[] split = path.split("/");
        return split[split.length-1];
    }
    public static byte[] decodeBase64(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64);
    }
    public static void saveFile(byte[] bytes, String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}