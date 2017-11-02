package com.shayer.samebirthday.parser;

import android.util.Log;

import com.shayer.samebirthday.activites.APICall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreeya Patel on 3/10/2016.
 */
public class JSONParser {
    static String SITE_URL;
    static HttpClient httpClient;
    static HttpEntity httpEntity;
    static HttpPost httpPost;
    static HttpResponse httpResponse;
    static InputStream is;
    static String json;
    static JSONArray jsonArray;
    static JSONObject jsonObject;
    static List<NameValuePair> pairs;
    static String result;
    static String url;

    static {
        //SITE_URL = "http://ilaxo.com/developer/help_around/api/";
        SITE_URL = "http://ilaxo.com/developer/simplebirthday/api/";
        json = "";
    }

    private static void init() {
        result = "";
        jsonObject = null;
        httpPost = null;
        is = null;
        jsonArray = null;
        httpClient = new DefaultHttpClient();
    }

    private static String getResult(InputStream is) {
        BufferedReader reader;
        StringBuilder stringBuilder = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"), 8);

            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            is.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static JSONObject GetMessage(String SessionID,
                                        String receiver_id){
//                                        String Last_msg_id) {
        try {
            init();
            url = APICall.baseURL + "message/get_message";
            Log.d("TAG", url);
            httpPost = new HttpPost(url.toString());
            Log.d("TAG", "HTTP POST" + httpPost);
            pairs = new ArrayList();
            pairs.add(new BasicNameValuePair("SessionID", SessionID));
            pairs.add(new BasicNameValuePair("receiver_id", receiver_id));
//            pairs.add(new BasicNameValuePair("Last_msg_id", Last_msg_id));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            Log.d("TAG", "HTTP POST" + httpPost);
            httpResponse = httpClient.execute(httpPost);
            Log.d("TAG", "HTTP RESPONSE" + httpResponse);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            result = getResult(is);
            Log.d("TAG", "RESULT : " + result);
            jsonObject = new JSONObject(result);
            Log.d("TAG", "JSON OBJECT : " + jsonObject);
        } catch (ClientProtocolException e) {
            Log.e("TAG", "Error in Client Protocol : " + e.toString());
        } catch (JSONException e2) {
            Log.e("TAG", "Error Parsing data " + e2.toString());
        } catch (Exception e3) {
            Log.e("TAG", "Error in HTTP Connection : " + e3.toString());
        }
        return jsonObject;
    }

    public static JSONObject NearByUsers(String SessionID,
                                        String Latitude,
                                         String Longitude) {
        try {
            init();
            url = APICall.baseURL + "users/nearby";
            Log.d("TAG", url);
            httpPost = new HttpPost(url.toString());
            Log.d("TAG", "HTTP POST" + httpPost);
            pairs = new ArrayList();
            pairs.add(new BasicNameValuePair("SessionID", SessionID));
            pairs.add(new BasicNameValuePair("Latitude", Latitude));
            pairs.add(new BasicNameValuePair("Longitude", Longitude));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            Log.d("TAG", "HTTP POST" + httpPost);
            httpResponse = httpClient.execute(httpPost);
            Log.d("TAG", "HTTP RESPONSE" + httpResponse);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            result = getResult(is);
            Log.d("TAG", "RESULT : " + result);
            jsonObject = new JSONObject(result);
            Log.d("TAG", "JSON OBJECT : " + jsonObject);
        } catch (ClientProtocolException e) {
            Log.e("TAG", "Error in Client Protocol : " + e.toString());
        } catch (JSONException e2) {
            Log.e("TAG", "Error Parsing data " + e2.toString());
        } catch (Exception e3) {
            Log.e("TAG", "Error in HTTP Connection : " + e3.toString());
        }
        return jsonObject;
    }

    public static JSONObject GetMyMsgHistory(String SessionID) {
        try {
            init();
            url = APICall.baseURL + "message/message_history";
            Log.d("TAG", url);
            httpPost = new HttpPost(url.toString());
            Log.d("TAG", "HTTP POST" + httpPost);
            pairs = new ArrayList();
            pairs.add(new BasicNameValuePair("SessionID", SessionID));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            Log.d("TAG", "HTTP POST" + httpPost);
            httpResponse = httpClient.execute(httpPost);
            Log.d("TAG", "HTTP RESPONSE" + httpResponse);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            result = getResult(is);
            Log.d("TAG", "RESULT : " + result);
            jsonObject = new JSONObject(result);
            Log.d("TAG", "JSON OBJECT : " + jsonObject);
        } catch (ClientProtocolException e) {
            Log.e("TAG", "Error in Client Protocol : " + e.toString());
        } catch (JSONException e2) {
            Log.e("TAG", "Error Parsing data " + e2.toString());
        } catch (Exception e3) {
            Log.e("TAG", "Error in HTTP Connection : " + e3.toString());
        }
        return jsonObject;
    }
}
