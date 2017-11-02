package com.shayer.samebirthday.alarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shayer.samebirthday.R;
import com.shayer.samebirthday.activites.ResultActivity;
import com.shayer.samebirthday.adapter.ChatMessageAdapter;
import com.shayer.samebirthday.app.SameBirthday;
import com.shayer.samebirthday.model.ChatMessage;
import com.shayer.samebirthday.model.ResultBirthdateModel;
import com.shayer.samebirthday.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shreeya Patel on 3/11/2016.
 */
public class SampleSchedulingService extends IntentService {

    String SessionID="",Latitude="",Longitude="";
    SharedPreferences pref;
    String Result="",UserID="",Username="",Email="",Birthdate="",Gender="",UpdatedAt="",Distance="";
    String SenderUserID="",SenderUserName="";
    String ReceiverUserID="",ReceiverUserName="";
    String id="",sender_id="",receiver_id="",message="",IsSend="";
    public ArrayList<ChatMessage> chatMessages;
    public static ArrayList<ChatMessage> chatMsg;
    ChatMessageAdapter mAdapter;
    String Receiver_user_id="";
    String LAST_MESSAGE_ID="";
    String Message="";
    ArrayList<ResultBirthdateModel> resultBirthdateArray;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SampleSchedulingService(String name) {
        super(name);
    }

    public SampleSchedulingService(){
        super("SchedulingService");
        resultBirthdateArray = new ArrayList<>();
        chatMessages = new ArrayList<>();
        chatMsg = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getPrefData();
        new GetNearByUsers().execute();
//        new GetMessageTask().execute();
        SampleAlarmReceiver.completeWakefulIntent(intent);
    }

    class GetNearByUsers extends AsyncTask<Void, Integer, JSONObject>{

        private ProgressDialog p_dialog;

        JSONObject jObject;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            jObject = JSONParser.NearByUsers(SessionID,
                    Latitude,
                    Longitude);

            Log.d("TAG", "JSON OBJECTSSSSSSS : " + jObject);
            String msg = "Nothing Happened...";
            try {
                if (jObject != null) {

                    Result = jObject.getString("Result");
                    Log.d("TAG", "RESULT:" + Result);

                    if (Result.equals("1")){
                        JSONObject object;
                        JSONArray jsonArray = jObject.getJSONArray("Users");
                        resultBirthdateArray.clear();
                        if (jsonArray.length() > 0){
                            ResultBirthdateModel items;
                            for (int i=0;i<jsonArray.length();i++){
                                items = new ResultBirthdateModel();
                                object = jsonArray.getJSONObject(i);
                                items.setUserName(object.getString("Username"));
                                items.setUserGender(object.getString("Gender"));
                                items.setUserBirthdate(object.getString("Birthdate"));
                                items.setUserId(object.getString("UserID"));

                                resultBirthdateArray.add(items);

                                Username = object.getString("Username");
//                                        Log.d("TAG","Username="+Username);
                                Email = object.getString("Email");
//                                        Log.d("TAG","Email="+Email);
                                Birthdate = object.getString("Birthdate");
//                                        Log.d("TAG","BirthDate="+Birthdate);
                                Gender = object.getString("Gender");
//                                        Log.d("TAG","Gender="+Gender);
                                UserID = object.getString("UserID");
                                Log.d("TAG", "UserID=" +UserID);

                            }
                        }
                        Bundle bundleObject = new Bundle();
                        bundleObject.putSerializable("ReceiveUserArray", resultBirthdateArray);
                    }
                    else {
                        Message = jObject.getString("Message");
                    }


                } else {
                    Log.d("TAG", "Login Time Already Syncd....");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.e("TAG", "Warn :" + e.getLocalizedMessage());
            }
            return jObject;
            //return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            Log.d("TAG:","REsultttttt:"+result);
            if (Result == null || Result.equals("")) {
                //Toast.makeText(getApplicationContext(),"Please, check your Internet Connection",Toast.LENGTH_LONG).show();
            } else {
                if (Result.equals("1")) {

                   notificationForNEarByUser(resultBirthdateArray);
//                    new GetMessageTask().execute();
                }
                else if (Result.equals("0")){
//                    Toast.makeText(getApplicationContext(),"Invalide User",Toast.LENGTH_LONG).show();
                } else{

                }
            }
        }
    }
    class GetMessageTask extends AsyncTask<Void, Integer, JSONObject> {
        private ProgressDialog p_dialog;

        JSONObject jObject;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            jObject = JSONParser.GetMessage(SessionID, "19");

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
//                                    chatMessages.add(chat);
                                    chatMsg.add(chat);
                                } else {
                                    chat = new ChatMessage(message, true, false);
                                    chat.setIsMine(true);
                                    chat.setContent(message);
                                    chat.setIsImage(false);
//                                    chatMessages.add(chat);
                                    chatMsg.add(chat);
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Starting new chat..",Toast.LENGTH_LONG).show();
//                            Bundle bundleObject = new Bundle();
//                            chatMessages.clear();
                            chatMsg.clear();
//                            bundleObject.putSerializable("Msg", chatMessages);
//                            Intent i = new Intent(getApplicationContext(),ChatBoardActivity.class);
//                            i.putExtra("UserID", UserID);
//                            i.putExtras(bundleObject);
//                            startActivity(i);
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

            Log.d("TAG:","REsultttttt:"+result);
            if (Result == null || Result.equals(""))
                Toast.makeText(getApplicationContext(),"Please, check your Internet Connection",Toast.LENGTH_LONG).show();
            else {
                if (Result.equals("1")) {
                    mAdapter = new ChatMessageAdapter(getApplicationContext(), chatMsg);
                    mAdapter.notifyDataSetChanged();
                    chatMsg = SampleSchedulingService.getArrayListOfMessage();
                    Log.d("TAG","NewMsgs:"+chatMsg.size());
                }
                else if (Result.equals("0")){
                    Toast.makeText(getApplicationContext(),"Invalide user",Toast.LENGTH_LONG).show();
                } else{

                }
            }
        }
    }

    public static ArrayList<ChatMessage> getArrayListOfMessage(){
        Log.d("MSG","ChatMSgSize:"+chatMsg.size());
        return chatMsg;
    }
    public void getPrefData(){
        pref = getSharedPreferences(SameBirthday.SESSION_ID, Context.MODE_PRIVATE);
        SessionID = pref.getString(SameBirthday.SESSION_ID, "");
        Log.d("ADTAG","SessionID:"+SessionID);

//        pref = getSharedPreferences(SameBirthday.RECEIVER_ID, Context.MODE_PRIVATE);
//        Receiver_user_id = pref.getString(SameBirthday.RECEIVER_ID, "");
//        Log.d("ADTAG","RECEIVER_ID:"+Receiver_user_id);
//
//        pref = getSharedPreferences(SameBirthday.LAST_MESSAGE_ID, Context.MODE_PRIVATE);
//        LAST_MESSAGE_ID = pref.getString(SameBirthday.LAST_MESSAGE_ID, "");
//        Log.d("ADTAG","LAST_MESSAGE_ID:"+LAST_MESSAGE_ID);

        SharedPreferences sp = getSharedPreferences("mLastLocation", Context.MODE_PRIVATE);
        Latitude = sp.getString("lat","0");
        Longitude = sp.getString("lon", "0");
    }

    public void notificationForNEarByUser(ArrayList<ResultBirthdateModel> resultBirthdateArray){
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification myNotication;
        Bundle bundleObject = new Bundle();
        bundleObject.putSerializable("ReceiveUserArray", resultBirthdateArray);
        Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
        intent.putExtras(bundleObject);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
//        builder.setAutoCancel(true);
        builder.setTicker("SameB'Day");
        builder.setContentTitle("Same BirthDay");
        builder.setContentText("Nearest Match Found");
        builder.setSmallIcon(R.drawable.ic_sb_notification);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.build();
        myNotication = builder.getNotification();
        myNotication.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(11, myNotication);
    }
}
