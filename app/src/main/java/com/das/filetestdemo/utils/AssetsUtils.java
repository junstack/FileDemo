package com.das.filetestdemo.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AssetsUtils {
    private static final String TAG = "AssetsUtils";

    /**
     * 读取assets文件数据
     * @param context 上下文
     * @param fileName 文件名称
     * @return 文件字符串
     */
    public static  String getAssetsFile(Context context, String fileName) {
        try {
            //得到assets数据流
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取流的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            in.close();
            String res = new String(buffer, StandardCharsets.UTF_8);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取res下raw文件夹下的文件
     * @return 文件字符串
     */
    public static String getResourceRaw(Context context,int raw) {
        try {
            //得到raw数据流
            InputStream in = context.getResources().openRawResource(raw);
            //获取流的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            in.close();
            String res = new String(buffer, StandardCharsets.UTF_8);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取与res同目录assets的文件(音频文件)
     * @param context 上下文
     * @param fileName 文件名称
     * @return
     */
    public static AssetFileDescriptor getAssetsVoice(Context context, String fileName) {
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = context.getResources().getAssets().openFd(fileName);
            return fileDescriptor;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
