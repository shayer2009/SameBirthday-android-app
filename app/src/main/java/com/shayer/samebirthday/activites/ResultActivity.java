package com.shayer.samebirthday.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shayer.samebirthday.R;
import com.shayer.samebirthday.adapter.ChatMessageAdapter;
import com.shayer.samebirthday.adapter.ResultBirthdateAdapter;
import com.shayer.samebirthday.app.SameBirthday;
import com.shayer.samebirthday.model.ChatMessage;
import com.shayer.samebirthday.model.ResultBirthdateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ResultActivity extends ActionBarActivity {

    ArrayList<ResultBirthdateModel> resultBirthdateArray;
    ListView lvResultView;
    TextView tvShowMessage;
    Context con;
    String UserID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String SessionID="";
    RequestQueue mRequestQueue;
    StringRequest strReq;
    String Result="";
    String SenderUserID="",SenderUserName="";
    String ReceiverUserID="",ReceiverUserName="";
    String id="",sender_id="",receiver_id="",message="",IsSend="";
    String SenderMessage="";
    ProgressDialog pDialog;

    ChatMessageAdapter mAdapter;

    ArrayList<ChatMessage> chatMessages;
    ArrayList<ChatMessage> chatMsgs;
    public ResultActivity(){
        chatMessages = new ArrayList<>();
//        chatMsgs = new ArrayList<>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        declareData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();
        con = ResultActivity.this;

        Bundle bundleObject = getIntent().getExtras();
        resultBirthdateArray = (ArrayList<ResultBirthdateModel>)  bundleObject.getSerializable("ReceiveUserArray");
        lvResultView = (ListView) findViewById(R.id.lvDisplayResult);
        tvShowMessage = (TextView) findViewById(R.id.tvShowMessage);
        mRequestQueue = Volley.newRequestQueue(con);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

//        chatMsgs = SampleSchedulingService.getArrayListOfMessage();
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
//        mListView.setAdapter(mAdapter);
        getPrefData();

        if (resultBirthdateArray.size()>0){
            lvResultView.setAdapter(new ResultBirthdateAdapter(resultBirthdateArray, con));

            lvResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserID = resultBirthdateArray.get(position).getUserId();
                    pref = getSharedPreferences(SameBirthday.RECEIVER_ID,Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString(SameBirthday.RECEIVER_ID,UserID);
                    editor.commit();

                    volleyRrequest();
                }
            });
        }

        else {
            lvResultView.setVisibility(View.GONE);
            tvShowMessage.setVisibility(View.VISIBLE);
        }

    }
    public void getPrefData(){
        pref = getSharedPreferences(SameBirthday.SESSION_ID, Context.MODE_PRIVATE);
        SessionID = pref.getString(SameBirthday.SESSION_ID, "");
        Log.d("ADTAG","SessionID:"+SessionID);

//        pref = getSharedPreferences(SameBirthday.USERID, Context.MODE_PRIVATE);
//        UserID = pref.getString(SameBirthday.USERID, "");
//        Log.d("ADTAG","UserID:"+UserID);
    }

    public void setPrefData(){
        pref = getSharedPreferences(SameBirthday.RECEIVER_MESSAGE,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.RECEIVER_MESSAGE, message);
        editor.commit();

        pref = getSharedPreferences(SameBirthday.SENDER_MESSAGE,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.SENDER_MESSAGE,SenderMessage);
        editor.commit();

        pref = getSharedPreferences(SameBirthday.RECEIVER_ID,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.RECEIVER_ID,UserID);
        editor.commit();
    }

    public void volleyRrequest(){

        pDialog.setCancelable(false);
        pDialog.show();

        strReq = new StringRequest(Request.Method.POST,
                APICall.baseURL + "message/get_message",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG","Search Response= "+response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");

                            if (Result.equals("1")){

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

                                JSONArray messageJsonArray = jsonObject.getJSONArray("Message");
                                chatMessages.clear();
                                if (messageJsonArray.length()>0){
                                    ChatMessage chat;

                                    for (int i=0;i<messageJsonArray.length();i++){

                                        id = messageJsonArray.getJSONObject(i).getString("id");
                                        Log.d("TAG","id:"+id);
                                        sender_id = messageJsonArray.getJSONObject(i).getString("sender_id");
                                        Log.d("TAG","sender_id:"+sender_id);
                                        receiver_id = messageJsonArray.getJSONObject(i).getString("receiver_id");
                                        Log.d("TAG","receiver_id:"+receiver_id);
                                        IsSend = messageJsonArray.getJSONObject(i).getString("IsSend");
                                        Log.d("TAG","IsSend:"+IsSend);
                                        message = messageJsonArray.getJSONObject(i).getString("message");
                                        Log.d("TAG","message:"+message);

                                        if (IsSend.equals("0")){
                                            chat = new ChatMessage(message,false,false);
                                            chat.setIsMine(false);
                                            chat.setContent(message);
                                            chat.setIsImage(false);
                                            chatMessages.add(chat);
                                        }
                                        else {
                                            chat = new ChatMessage(message,true,false);
                                            chat.setIsMine(true);
                                            chat.setContent(message);
                                            chat.setIsImage(false);
                                            chatMessages.add(chat);
                                        }
                                    }
                                    Bundle bundleObject = new Bundle();
                                    bundleObject.putSerializable("Msg", chatMessages);
                                    Intent i = new Intent(con,ChatBoardActivity.class);
                                    i.putExtra("UserID", UserID);
                                    i.putExtras(bundleObject);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_out, R.anim.right_in);
                                }
                            }
                            else {
                                Toast.makeText(con,"Starting new chat..",Toast.LENGTH_LONG).show();
                                Bundle bundleObject = new Bundle();
                                chatMessages.clear();
                                bundleObject.putSerializable("Msg", chatMessages);
                                Intent i = new Intent(con,ChatBoardActivity.class);
                                i.putExtra("UserID", UserID);
                                i.putExtras(bundleObject);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("SessionID", SessionID);
                params.put("receiver_id", UserID);

                return params;
            }
        };
        strReq.setShouldCache(false);
        mRequestQueue.add(strReq);
    }
}
