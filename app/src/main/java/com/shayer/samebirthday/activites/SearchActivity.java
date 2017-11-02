package com.shayer.samebirthday.activites;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shayer.samebirthday.R;
import com.shayer.samebirthday.adapter.ResultBirthdateAdapter;
import com.shayer.samebirthday.app.SameBirthday;
import com.shayer.samebirthday.model.MessageHistoryModel;
import com.shayer.samebirthday.model.ResultBirthdateModel;
import com.shayer.samebirthday.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SearchActivity extends ActionBarActivity implements android.app.DatePickerDialog.OnDateSetListener{

    EditText etSelectedDate, etDistance, etStartAge, etEndAge, etGender;
    ImageView ivChatMessageHistory;
    Button btnSearch;
    Context con;
    ListView lvDistance, lvGender;
    String[] distance = {"1 Mile","2 Mile","3 Mile","4 Mile","5 Mile","6 Mile","7 Mile","8 Mile","9 Mile","10 Mile","10+ Mile"};
    String[] gender = {"Male","Female","Both"};
    String SelectBDate, SelectDistance,SAge, EAge,Gen;

    String Result="", Message="", SessionID="",UserID="",Username="",Email="",Birthdate="",Gender="",UpdatedAt="",Distance="";
    String message="",UserName="",add_date="",ErrorMessage="",HistoryUserID="";
    Calendar calendar;
    DatePickerDialog dpd3;

    RequestQueue mRequestQueue;
    ProgressDialog pDialog;
    StringRequest strReq;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<ResultBirthdateModel> resultBirthdateArray;
    ResultBirthdateModel resultBirthdateModel;
    ResultBirthdateAdapter resultBirthdateAdapter;

    ArrayList<MessageHistoryModel> messageHistoryList;

    public SearchActivity(){
        resultBirthdateArray = new ArrayList<>();
        messageHistoryList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        declareData();
        clickedOnDistanceEditText();
        clickedOnSelectGender();
        clickedOnSearchButton();
        clickedOnChatMsgHistory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        con = SearchActivity.this;

        etSelectedDate = (EditText) findViewById(R.id.etSelectBirthDate);
        etDistance = (EditText) findViewById(R.id.etSelectDistance);
        etStartAge = (EditText) findViewById(R.id.etStartAge);
        etEndAge = (EditText) findViewById(R.id.etEndAge);
        etGender = (EditText) findViewById(R.id.etSelectGender);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        ivChatMessageHistory = (ImageView) findViewById(R.id.ivChatMsgHistory);

        etSelectedDate.setInputType(InputType.TYPE_NULL);
        etDistance.setInputType(InputType.TYPE_NULL);
        etGender.setInputType(InputType.TYPE_NULL);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dpd3 = new DatePickerDialog(con, AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

        mRequestQueue = Volley.newRequestQueue(con);
        pDialog = new ProgressDialog(con);
        pDialog.setMessage("Loading...");

        pref = getSharedPreferences(SameBirthday.SESSION_ID,Context.MODE_PRIVATE);
        SessionID = pref.getString(SameBirthday.SESSION_ID, "");
        Log.d("TAG","SessionID="+SessionID);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void doSelectBirthDate(View v){
       dpd3.show();
    }

    public void clickedOnDistanceEditText(){
        etDistance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    distanceDialog();
                }
            }
        });

        etDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanceDialog();
            }
        });
    }

    public void clickedOnSelectGender(){

        etGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    genderDialog();
                }
            }
        });
        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderDialog();
            }
        });
    }
    public void clickedOnSearchButton(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

    }

    public void clickedOnChatMsgHistory(){
        ivChatMessageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetMyMsgHistory().execute();
            }
        });
    }

    public void validation(){
        SelectBDate = etSelectedDate.getText().toString().trim();
        SelectDistance = etDistance.getText().toString().trim();
        SAge = etStartAge.getText().toString().trim();
        EAge = etEndAge.getText().toString().trim();
        Gen = etGender.getText().toString().trim();

        if (TextUtils.isEmpty(SelectBDate)
                ||TextUtils.isEmpty(SelectDistance)
                ||TextUtils.isEmpty(SAge)
                ||TextUtils.isEmpty(EAge)
                ||TextUtils.isEmpty(Gen)){

            if (TextUtils.isEmpty(SelectBDate)){
                etSelectedDate.requestFocus();
                etSelectedDate.setError("Required..!!");
            }
            else{

            }

            if (TextUtils.isEmpty(SelectDistance)){
                etDistance.requestFocus();
                etDistance.setError("Required..!!");
            }
            else{

            }

            if (TextUtils.isEmpty(SAge)){
                etStartAge.requestFocus();
                etStartAge.setError("Required..!!");
            }
            else{

            }

            if (TextUtils.isEmpty(EAge)){
                etEndAge.requestFocus();
                etEndAge.setError("Required..!!");
            }
            else{

            }

            if (TextUtils.isEmpty(Gen)){
                etGender.requestFocus();
                etGender.setError("Required..!!");
            }
            else{

            }
        }
        else{
            // calling web service using volley
            volleyRequest();
        }
    }

    public void volleyRequest(){
        pDialog.show();

        strReq = new StringRequest(Request.Method.POST,
                APICall.baseURL + "users/search",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG","Search Response= "+response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");

                            if (Result.equals("1")){
                                JSONObject object;
                                JSONArray jsonArray = jsonObject.getJSONArray("Users");
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
                                        Log.d("TAG","UserID="+UserID);

                                        setPrefData();
                                    }
                                }
                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("ReceiveUserArray", resultBirthdateArray);
                                Intent i = new Intent(con,ResultActivity.class);
                                i.putExtras(bundleObject);
                                startActivity(i);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            }
                            else {
                                Message = jsonObject.getString("Message");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getLocalizedMessage());
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                SharedPreferences sp = getSharedPreferences("mLastLocation", Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();

                params.put("SessionID", SessionID);

//                String date = SelectBDate.substring(5);
//                Log.d("TAG","FormatedDate:"+date);
//                params.put("Birthdate", date);
                params.put("Birthdate",etSelectedDate.getText().toString().trim());
                if (SelectDistance.contains("Mile")){
                    SelectDistance = SelectDistance.replace("Mile","").trim();
                }
                if(SelectDistance.contains("+")){
                    SelectDistance = SelectDistance.replace("+","").trim();
                }
                int mile = Integer.parseInt(SelectDistance);
                Log.d("TAG","MILE="+mile);
                int dis = mile*1000;
                Log.d("TAG","Distance inKM ="+dis);

                params.put("Distance", String.valueOf(dis));
                params.put("StartAge",etStartAge.getText().toString().trim());
                params.put("EndAge",etEndAge.getText().toString().trim());
                params.put("Gender",etGender.getText().toString().trim());
                params.put("Latitude", sp.getString("lat","0"));
                params.put("Longitude", sp.getString("lon","0"));

                return params;
            }
        };
        strReq.setShouldCache(false);
        mRequestQueue.add(strReq);
    }

    public void setPrefData(){
        pref = getSharedPreferences(SameBirthday.USERNAME,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.USERNAME,Username);
        editor.commit();

        pref = getSharedPreferences(SameBirthday.GENDER,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.GENDER,Gender);
        editor.commit();

        pref = getSharedPreferences(SameBirthday.BIRTHDATE,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.BIRTHDATE,Birthdate);
        editor.commit();

//        pref = getSharedPreferences(SameBirthday.USERID,Context.MODE_PRIVATE);
//        editor = pref.edit();
//        editor.putString(SameBirthday.USERID,UserID);
//        editor.commit();
    }
    public void getPrefData(){

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        etSelectedDate.setText((monthOfYear + 1) + "-" + dayOfMonth);
    }

    class GetMyMsgHistory extends AsyncTask<Void, Integer, JSONObject> {

        private ProgressDialog p_dialog;

        JSONObject jObject;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            p_dialog = new ProgressDialog(con);
            p_dialog.setMessage("Loading...");
            p_dialog.setCancelable(false);
            p_dialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            jObject = JSONParser.GetMyMsgHistory(SessionID);

            Log.d("TAG", "JSON OBJECTSSSSSSS : " + jObject);
            String msg = "Nothing Happened...";
            try {
                if (jObject != null) {

                    Result = jObject.getString("Result");
                    Log.d("TAG", "RESULT:" + Result);

                    if (Result.equals("1")){
                        JSONArray getMsgHistoryArray = jObject.getJSONArray("Message");
                        JSONObject Users;

                        messageHistoryList.clear();
                        if (getMsgHistoryArray.length()>0){

                            MessageHistoryModel items;
                            for (int i=0;i<getMsgHistoryArray.length();i++){

                                items = new MessageHistoryModel();
                                message = getMsgHistoryArray.getJSONObject(i).getString("message");
                                Log.d("TAG","History Message:"+message);

                                add_date = getMsgHistoryArray.getJSONObject(i).getString("add_date");
                                Log.d("TAG","History Date:"+add_date);

                                Users = getMsgHistoryArray.getJSONObject(i).getJSONObject("User");
                                UserName = Users.getString("Username");
                                HistoryUserID = Users.getString("UserID");
                                Log.d("TAG","History Username:"+UserName);
                                Log.d("TAG","History UserID:"+HistoryUserID);

                                items.setHistoryMessageUserName(Users.getString("Username"));
                                items.setHistoryMessage(getMsgHistoryArray.getJSONObject(i).getString("message"));
                                items.setHistoryMessageDate(getMsgHistoryArray.getJSONObject(i).getString("add_date"));
                                items.setHistoryMessageUserId(Users.getString("UserID"));

                                messageHistoryList.add(items);
                            }
                        }
                    }
                    else{
                        ErrorMessage = jObject.getString("Message");
                        Log.d("TAG","ErrorMsg:"+ErrorMessage);
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
            p_dialog.dismiss();
            Log.d("TAG:","REsultttttt:"+result);
            if (Result == null || Result.equals(""))
                Toast.makeText(getApplicationContext(), "Please, check your Internet Connection", Toast.LENGTH_LONG).show();
            else {
                if (Result.equals("1")) {
                    Bundle bundleObject = new Bundle();
                    bundleObject.putSerializable("MessageHistory", messageHistoryList);
                    Intent i = new Intent(con,MyMessageHistoryActivity.class);
                    i.putExtras(bundleObject);
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
                else if (Result.equals("0")){
                    if (ErrorMessage.equals("No message found.")){
                        Bundle bundleObject = new Bundle();
                        bundleObject.putSerializable("MessageHistory", messageHistoryList);
                        Intent i = new Intent(con,MyMessageHistoryActivity.class);
                        i.putExtras(bundleObject);
                        startActivity(i);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalide User.", Toast.LENGTH_LONG).show();
                    }

                } else{

                }
            }
        }
    }

    public void genderDialog(){
        final Dialog dialogGender = new Dialog(con);
        dialogGender.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGender.setContentView(R.layout.row_gender_select);
        lvGender = (ListView) dialogGender.findViewById(R.id.lvGenderSelect);
        ArrayAdapter adapter = new ArrayAdapter(con, android.R.layout.simple_list_item_1, gender);
        lvGender.setAdapter(adapter);

        lvGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                etGender.setText(selectedGender);
                dialogGender.dismiss();
            }
        });
        dialogGender.show();
    }
    public void distanceDialog(){
        final Dialog dialogMileDistance = new Dialog(con);
        dialogMileDistance.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMileDistance.setContentView(R.layout.row_mile_distance);
        lvDistance = (ListView) dialogMileDistance.findViewById(R.id.lvDistancemile);
        ArrayAdapter adapter = new ArrayAdapter(con, android.R.layout.simple_list_item_1, distance);
        lvDistance.setAdapter(adapter);

        lvDistance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistance = (String) parent.getItemAtPosition(position);
                etDistance.setText(selectedDistance);
                dialogMileDistance.dismiss();
            }
        });
        dialogMileDistance.show();
    }
}
