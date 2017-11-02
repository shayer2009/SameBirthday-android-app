package com.shayer.samebirthday.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shayer.samebirthday.R;
import com.shayer.samebirthday.adapter.ChatMessageAdapter;
import com.shayer.samebirthday.alarm.SampleAlarmReceiver;
import com.shayer.samebirthday.alarm.SampleSchedulingService;
import com.shayer.samebirthday.app.SameBirthday;
import com.shayer.samebirthday.model.ChatMessage;
import com.shayer.samebirthday.parser.JSONParser;


public class ChatBoardActivity extends ActionBarActivity {


    ListView mListView;
    Button mButtonSend;
    EditText mEditTextMessage;
    ImageView mImageView;
    ChatMessageAdapter mAdapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String SessionID="",UserID="",Result="",Message_id="",Message="";
    Context con;
    RequestQueue mRequestQueue;
    StringRequest strReq;
    ProgressDialog pDialog;
    String messages="";
    String SenderUserID="",ReceiverUserID="",SenderUserName="",ReceiverUserName="";
    ArrayList<ChatMessage> chatMessages;
    String id="",sender_id="",receiver_id="",message="",IsSend="";
    String Last_msg_id="";

    TimerTask doAsynchronousTask;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        declareData();
        Intent i = getIntent();
        UserID = i.getStringExtra("UserID");
//        Toast.makeText(con,"Id:"+UserID,Toast.LENGTH_LONG).show();

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages = mEditTextMessage.getText().toString();
                if (TextUtils.isEmpty(messages)) {
                    return;
                }
                mEditTextMessage.setText("");
                volleyRrequest();
//                new GetMessageTask().execute();
            }
        });
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void declareData(){
        setContentView(R.layout.activity_chat_board);
        getSupportActionBar().hide();

        Bundle bundleObject = getIntent().getExtras();
        chatMessages = (ArrayList<ChatMessage>) bundleObject.getSerializable("Msg");


        con = ChatBoardActivity.this;
        getPrefData();
        mRequestQueue = Volley.newRequestQueue(con);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (Button) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        mAdapter = new ChatMessageAdapter(this, chatMessages);

        mListView.setAdapter(mAdapter);
        Log.d("TAG", "ListSiZe:" + mListView.getCount());
        mAdapter.notifyDataSetChanged();
        callAsynchronousTask();
    }

    public void getPrefData(){
        pref = getSharedPreferences(SameBirthday.SESSION_ID, Context.MODE_PRIVATE);
        SessionID = pref.getString(SameBirthday.SESSION_ID, "");
        Log.d("ADTAG", "SessionID:" + SessionID);

//        pref = getSharedPreferences(SameBirthday.LAST_MESSAGE_ID, Context.MODE_PRIVATE);
//        Last_msg_id = pref.getString(SameBirthday.LAST_MESSAGE_ID, "");
        Log.d("ADTAG", "LAST_MESSAGE_ID:" + Last_msg_id);
    }

    public void volleyRrequest(){

        strReq = new StringRequest(Request.Method.POST,
                APICall.baseURL + "message/send",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Search Response= " + response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");

                            if (Result.equals("1")){

                                Message_id = jsonObject.getString("Message_id");
                                Log.d("TAG","Message_id="+Message_id);

                                pref = getSharedPreferences(SameBirthday.LAST_MESSAGE_ID, Context.MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString(SameBirthday.LAST_MESSAGE_ID, Message_id);
                                editor.commit();

                                Message = jsonObject.getString("Message");
                                Log.d("TAG", "Message=" + Message);

                                JSONObject senderInfo = jsonObject.getJSONObject("Sender");
                                JSONObject receiverInfo = jsonObject.getJSONObject("Receiver");

                                SenderUserID = senderInfo.getString("UserID");
                                Log.d("TAG", "SenderUserID=" + SenderUserID);
                                ReceiverUserID = receiverInfo.getString("UserID");
                                Log.d("TAG", "ReceiverUserID=" + ReceiverUserID);

                                SenderUserName = senderInfo.getString("Username");
                                Log.d("TAG", "SenderUserName=" + SenderUserName);
                                ReceiverUserName = receiverInfo.getString("Username");
                                Log.d("TAG", "ReceiverUserName=" + ReceiverUserName);

                                Toast.makeText(con,Message+"",Toast.LENGTH_LONG).show();
//                               callAsynchronousTask();
                            }
                            else {
//                                new Thread().start();
//                                Thread.sleep(2000);
                                Toast.makeText(con,"Please, Enter valide Email or Password",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("SessionID", SessionID);
                params.put("receiver_id", UserID);
                params.put("message", messages);

                return params;
            }
        };
        strReq.setShouldCache(false);
        mRequestQueue.add(strReq);
    }

    class GetMessageTask extends AsyncTask<Void, Integer, JSONObject> {

        JSONObject jObject;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            jObject = JSONParser.GetMessage(SessionID,UserID);

            Log.d("TAG", "JSON OBJECTSSSSSSS : " + jObject);
            String msg = "Nothing Happened...";
            try {
                if (jObject != null) {

                    Result = jObject.getString("Result");
                    Log.d("TAG", "RESULT:" + Result);

                    if (Result.equals("1")){

                        JSONObject senderInfo = jObject.getJSONObject("Sender");
                        JSONObject receiverInfo = jObject.getJSONObject("Receiver");

                        SenderUserID = senderInfo.getString("UserID");
                        Log.d("TAG", "SenderUserID=" + SenderUserID);
                        ReceiverUserID = receiverInfo.getString("UserID");
                        Log.d("TAG", "ReceiverUserID=" + ReceiverUserID);

                        SenderUserName = senderInfo.getString("Username");
                        Log.d("TAG", "SenderUserName=" + SenderUserName);
                        ReceiverUserName = receiverInfo.getString("Username");
                        Log.d("TAG", "ReceiverUserName=" + ReceiverUserName);

                        JSONArray messageJsonArray = jObject.getJSONArray("Message");

                        chatMessages.clear();

                        if (messageJsonArray.length()>0) {
                            ChatMessage chat;

                            for (int i = 0; i < messageJsonArray.length(); i++) {

                                id = messageJsonArray.getJSONObject(i).getString("id");
                                Log.d("TAG", "id:" + id);
                                sender_id = messageJsonArray.getJSONObject(i).getString("sender_id");
                                Log.d("TAG", "sender_id:" + sender_id);
                                receiver_id = messageJsonArray.getJSONObject(i).getString("receiver_id");
                                Log.d("TAG", "receiver_id:" + receiver_id);
                                IsSend = messageJsonArray.getJSONObject(i).getString("IsSend");
                                Log.d("TAG", "IsSend:" + IsSend);
                                message = messageJsonArray.getJSONObject(i).getString("message");
                                Log.d("TAG", "message:" + message);

                                if (IsSend.equals("0")) {
                                    chat = new ChatMessage(message, false, false);
                                    chat.setIsMine(false);
                                    chat.setContent(message);
                                    chat.setIsImage(false);
                                    chatMessages.add(chat);
                                } else {
                                    chat = new ChatMessage(message, true, false);
                                    chat.setIsMine(true);
                                    chat.setContent(message);
                                    chat.setIsImage(false);
                                    chatMessages.add(chat);
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Starting new chat..",Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Log.d("TAG", "Login Time Already Syncd....");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.e("TAG", "Warn :" + e.getLocalizedMessage());
            }
            return jObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            Log.d("TAG:", "REsultttttt:" + result);
            if (Result == null || Result.equals(""))
                Toast.makeText(con,"Please, check your Internet Connection",Toast.LENGTH_LONG).show();
            else {
                if (Result.equals("1")) {

                    mAdapter = new ChatMessageAdapter(con, chatMessages);
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                else if (Result.equals("0")){
                    try {

                        String ErrorMsg = jObject.getString("Message");
                        if (ErrorMsg.equals("No message found.")){
                            mAdapter = new ChatMessageAdapter(con, chatMessages);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(con,"Invalide user",Toast.LENGTH_LONG).show();
                            pref = getSharedPreferences(SameBirthday.SESSION_ID,Context.MODE_PRIVATE);
                            SessionID = pref.getString(SameBirthday.SESSION_ID, "");
                            Log.d("User SessionID","SessionId"+SessionID);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else{

                }
            }
        }
    }
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
//                            new Thread().start();
//                            Thread.sleep(2000);
                           new GetMessageTask().execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 500,3000); //execute in every 50000 ms
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (doAsynchronousTask!=null){
            doAsynchronousTask.cancel();
            timer.cancel();
            Log.d("Timer","Timer canceled");
        }
        ChatBoardActivity.this.finish();
    }
}
