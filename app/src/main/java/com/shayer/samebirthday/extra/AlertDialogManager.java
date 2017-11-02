package com.shayer.samebirthday.extra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class AlertDialogManager {

	public static void showInternetEnableAlertToUser(final Context context) {
		AlertDialog.Builder altForInternet = new AlertDialog.Builder(context);

		// Setting Dialog Title
		altForInternet.setTitle("Internet Enable Or Not ?");

		// Setting Dialog Message
		altForInternet.setMessage("Internet Is Not Enable. \nDo You Want To Enable ?");
		
		// On pressing Settings button
		altForInternet.setPositiveButton("YES",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Settings.ACTION_WIFI_SETTINGS);
				context.startActivity(intent);
			}
			
		});
	
		// Showing Alert Message
        altForInternet.show();

	}
	public static void showInternetEnableAlertToUser1(final Context context) {
		AlertDialog.Builder altForInternet = new AlertDialog.Builder(context);

		// Setting Dialog Title
		altForInternet.setTitle("PLease Connect With Internet ");

		// Setting Dialog Message
		altForInternet.setMessage("Internet Connection Is Not Available. \nPlease Connect With Net And Restart Application");
		
		// On pressing Settings button
		altForInternet.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			System.exit(0);
			
				
			}
			
		});
		
		// Showing Alert Message
        altForInternet.show();

	}
	public static void showGPSDisabledAlertToUser(final Context context) {
		// TODO Auto-generated method stub
		AlertDialog.Builder altForGPS = new AlertDialog.Builder(context);

		altForGPS.setTitle("GPS Enable Or Not ?");

		// Setting Dialog Message
		altForGPS.setMessage("GPS Is Not Enable. \nDo You Want To Enable ?");
		altForGPS.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent callGPSSettingIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(callGPSSettingIntent);
			}
		});
		altForGPS.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		});
		AlertDialog alert = altForGPS.create();
		alert.show();
	}
}
