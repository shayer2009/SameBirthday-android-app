package com.shayer.samebirthday.activites;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shayer.samebirthday.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    final Calendar calendar = Calendar.getInstance();
    Spinner spneGender;
    Context con;

    DatePickerDialog dpd3;
    EditText tRegisterName, tRegisterEmail, tRegisterPassword;
    EditText dateButton;
    Button bRegister;
    ProgressDialog pDialog;
    RequestQueue mRequestQueue;
    StringRequest strReq;
    String Result="", Message="",Email="",Username="";
    String UserName, UserEmail, UserPassword, SelectedBDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        declareData();
        clickedOnRegisterButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doBirthdate(View v) {
//        dpd3.show(getSupportFragmentManager(), "datepicker");
        dpd3.show();
    }

//    @Override
//    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//        //Toast.makeText(RegisterActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
//        dateButton.setText(year + "-" + (month+1) + "-" + day);
//    }

    public void declareData(){
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        con = RegisterActivity.this;
        spneGender = (Spinner) findViewById(R.id.spneGender);
        ArrayAdapter<CharSequence> gAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        spneGender.setAdapter(gAdapter);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        tRegisterName = (EditText) findViewById(R.id.tRegisterName);
        tRegisterEmail = (EditText) findViewById(R.id.tRegisterEmail);
        tRegisterPassword = (EditText) findViewById(R.id.tRegisterPassword);
        dateButton = (EditText) findViewById(R.id.dateButton);
        bRegister = (Button) findViewById(R.id.bRegister);

        dateButton.setInputType(InputType.TYPE_NULL);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dpd3 = new DatePickerDialog(con, AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void clickedOnRegisterButton(){
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    public void validation(){
        UserName = tRegisterName.getText().toString().trim();
        UserEmail = tRegisterEmail.getText().toString().trim();
        UserPassword = tRegisterPassword.getText().toString().trim();
        SelectedBDate = dateButton.getText().toString().trim();

        if (TextUtils.isEmpty(UserName)
                ||TextUtils.isEmpty(UserEmail)
                ||TextUtils.isEmpty(UserPassword)
                ||(UserPassword.length()<6)
                ||TextUtils.isEmpty(SelectedBDate)
                ||(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())){

            if (TextUtils.isEmpty(UserName)){
                tRegisterName.requestFocus();
                tRegisterName.setError("Required..!!");
            }
            else{
            }

            if (TextUtils.isEmpty(UserEmail)){
                tRegisterEmail.requestFocus();
                tRegisterEmail.setError("Required..!!");
            }
            else if ((!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())){
                tRegisterEmail.requestFocus();
                tRegisterEmail.setError("Enter valide Email Address..!!");
            }
            else {
            }

            if (TextUtils.isEmpty(UserPassword)){
                tRegisterPassword.requestFocus();
                tRegisterPassword.setError("Required..!!");
            }
            else if (UserPassword.length()<6){
                tRegisterPassword.requestFocus();
                tRegisterPassword.setError("Password length should be 6 character");
            }
            else {
            }

            if (TextUtils.isEmpty(SelectedBDate)){
                dateButton.requestFocus();
                dateButton.setError("Required..!!");
            }
            else{
            }
        }
        else {
            //calling web service using volley
            volleyRequest();
        }
    }
    public void volleyRequest(){
        pDialog.show();

        strReq = new StringRequest(Request.Method.POST,
                APICall.baseURL + "users/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG","Search Response= "+response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");

                            if (Result.equals("1")){
                                startActivity(new Intent(con, LoginActivity.class));
                                finish();
                            }
                            else {
                                Message = jsonObject.getString("Message");
                                Log.d("TAG","Message="+Message);
                                JSONObject errorJsonObject = jsonObject.getJSONObject("Errors");
                                Log.d("TAG","ArrayLenght="+errorJsonObject.length());
//                                for (int i=0;i<errorJsonObject.length();i++){
                                    Username = errorJsonObject.getString("Username");
                                    Log.d("TAG","Username="+Username);
                                    Email = errorJsonObject.getString("Email");
                                    Log.d("TAG","Email="+Email);
//                                }
                                Toast.makeText(con,Username+"\n"+Email,Toast.LENGTH_LONG).show();
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
                params.put("Username", UserName);
                params.put("Email", UserEmail);
                params.put("Password", UserPassword);
                params.put("Birthdate", SelectedBDate);
                params.put("Gender", spneGender.getSelectedItem().toString());
                params.put("Latitude", sp.getString("lat","0"));
                params.put("Longitude", sp.getString("lon","0"));

                return params;
            }
        };
        strReq.setShouldCache(false);
        mRequestQueue.add(strReq);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateButton.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
    }
}

