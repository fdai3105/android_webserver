package com.example.database_webserver_hanghoa;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConnectionService {
    static InputStream is = null;
    static String response = null;
    final static int GET = 1;
    final static int POST = 2;

    public String callService(String url, int method) {
        return this.callService(url, method, null);
    }

    public String callService(String url, int method, List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            // Kiểm tra loại method là POST hay GET
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // Thêm tham số
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                // Gắn tham số vào URL
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (Exception ex) {
            Log.e("Error at line 56 ","ConnectionService: " + ex.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            response = sb.toString();

            //for debug
            //đổi json > utf-8
            //String temp = response;
            //JSONObject jsonObject = new JSONObject(temp);
            //String error = jsonObject.getString("message");
            //Log.i("Result ConectionService", "line 72:" + error);
        } catch (Exception e) {
            Log.e("Error Buffer ", "line 74: " + e.toString());
        }
        return response;
    }
}
