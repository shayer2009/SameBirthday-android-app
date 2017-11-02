package com.shayer.samebirthday.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.shayer.samebirthday.R;
import com.shayer.samebirthday.app.SameBirthday;
import com.shayer.samebirthday.extra.GPSTracker;
import com.shayer.samebirthday.extra.NetworkUtil;

public class SpashScreenActivity extends Activity {

    Context con;
    GPSTracker gps;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        con = SpashScreenActivity.this;
        checkInternetConnection();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        clearPrefData();
    }

    public void checkInternetConnection() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtil.getConnectivityStatus(con) == 0) {
                    Builder altForInternet = new Builder(con);
                    altForInternet.setTitle("Internet Enable Or Not ?");
                    altForInternet.setMessage("Internet Is Not Enable. \nDo You Want To Enable ?");
                    altForInternet.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                            finish();
                        }
                    });
                    altForInternet.show();
                    return;
                }
                if (!gps_enabled && !network_enabled) {
                    // notify user
                    AlertDialog.Builder dialog = new AlertDialog.Builder(con);
                    dialog.setTitle("GPS Enabled Or Not ?");
                    dialog.setMessage("GPS Is Not Enable. \nDo You Want To Enable ?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            finish();
                        }
                    });
                    dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                    return;
                }
                startActivity(new Intent(con, MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                finish();
            }
        }, 1000);
    }
    public void clearPrefData(){
        pref = getSharedPreferences(SameBirthday.SESSION_ID,Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(SameBirthday.SESSION_ID,"");
        editor.commit();
    }
}