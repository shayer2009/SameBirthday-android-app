package com.shayer.samebirthday.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.shayer.samebirthday.R;
import com.shayer.samebirthday.alarm.SampleAlarmReceiver;
import com.shayer.samebirthday.app.SameBirthday;

public class LoginActivity extends AppCompatActivity {

    EditText tEmail, tPassword;
    Button bLogin;
    ProgressDialog pDialog;
    RequestQueue mRequestQueue;
    StringRequest strReq;
    Context con;
    String SessionID="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String UserEmail, UserPassword;

    String Result="",Messsage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        declareData();

        clickedOnLogInButton();
        new SampleAlarmReceiver().setAlarm(getApplicationContext());
    }

    public void doSignUp(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void declareData(){
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        con = LoginActivity.this;

        mRequestQueue = Volley.newRequestQueue(con);

        tEmail = (EditText) findViewById(R.id.tEmail);
        tPassword = (EditText) findViewById(R.id.tPassword);
        bLogin = (Button) findViewById(R.id.btnLogIn);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        clearPrefData();
    }
    public void clickedOnLogInButton(){
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    public void validation(){
        UserEmail = tEmail.getText().toString().trim();
        UserPassword = tPassword.getText().toString().trim();

        if (TextUtils.isEmpty(UserEmail)
                ||TextUtils.isEmpty(UserPassword)
                ||(UserPassword.length()<6)
                ||(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())){

            if (TextUtils.isEmpty(UserEmail)){
                tEmail.requestFocus();
                tEmail.setError("Required..!!");
            }
            else if ((!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())){
                tEmail.requestFocus();
                tEmail.setError("Enter valide Email Address..!!");
            }
            else {

            }

            if (TextUtils.isEmpty(UserPassword)){
                tPassword.requestFocus();
                tPassword.setError("Required..!!");
            }
            else if (UserPassword.length()<6){
                tPassword.requestFocus();
                tPassword.setError("Password length should be 6 character");
            }
            else {

            }
        }
        else {
            //calling web service using volley
            volleyRequest();
            new SampleAlarmReceiver().setAlarm(con);
        }

    }

    public void volleyRequest(){
        pDialog.show();

        strReq = new StringRequest(Request.Method.POST,
                APICall.baseURL + "users/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG","Search Response= "+response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");

                            if (Result.equals("1")){
                                SessionID = jsonObject.getString("SessionID");
                                Log.d("TAG","SessionID="+SessionID);

                                setPrefData();

                                startActivity(new Intent(con, SearchActivity.class));
                                overridePendingTransition(R.anim.right_out,R.anim.right_in);
                                finish();
                            }
                            else if (Result.equals("0")){
                                Toast.makeText(con,"Please, Enter valide Email or Password",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(con,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
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
                SharedPreferences sp = getSharedPreferences("mLastLocation", Context.MODE_PRIVATE);

                Map<String, String> params = new HashMap<>();
                params.put("Email", UserEmail);
                params.put("Password", UserPassword);
                params.put("Latitude", sp.getString("lat","0"));
                params.put("Longitude", sp.getString("lon","0"));

                return params;
            }
        };
        strReq.setShouldCache(false);
        mRequestQueue.add(strReq);
    }
    public void setPrefData(){
        pref = getSharedPreferences(SameBirthday.SESSION_ID,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.SESSION_ID, SessionID);
        editor.commit();
    }

    public void clearPrefData(){
        pref = getSharedPreferences(SameBirthday.LAST_MESSAGE_ID,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.LAST_MESSAGE_ID,"");
        editor.commit();
    }
}
