package com.abs104a.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * Http接続をして、byte列を取得する関数
 * 主にインターネットからTwitterのアイコンを取得するために使用する
 */
public class HttpClient {
    public static byte[] getByteArrayFromURL(String strUrl) {
        byte[] byteArray = new byte[1024];
        byte[] result = null;
        HttpURLConnection con = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        int size = 0;
        try {
            URL url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();//接続
            con.setRequestMethod("GET");
            con.connect();
            in = con.getInputStream();

            //データの取得
            out = new ByteArrayOutputStream();
            while ((size = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, size);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.disconnect();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap getImage(String url) {
        byte[] byteArray = getByteArrayFromURL(url);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}