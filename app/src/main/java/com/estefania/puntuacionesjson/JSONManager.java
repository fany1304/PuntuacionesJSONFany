package com.estefania.puntuacionesjson;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Fany on 03/03/2017.
 */

public class JSONManager {

    static InputStream inputStream = null;
    static String jsonString = "";

    @SuppressWarnings("deprecation")
    public String getJsonString(String url, String method, List<NameValuePair> params) {

        try {

            if(method.equals("POST")){

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            }
            else if(method.equals("GET")){

                DefaultHttpClient httpClient = new DefaultHttpClient();

                if (params != null) {

                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;

                }

                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String linea = null;

            while ((linea = reader.readLine()) != null) {

                stringBuilder.append(linea + "\n");

            }

            inputStream.close();
            jsonString = stringBuilder.toString();

        } catch (Exception e) {

            Log.e("Error", "Error obteniendo JSON " + e.toString());

        }
        return jsonString;
    }


}
