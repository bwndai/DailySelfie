package bdai.dailyselfie;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Service;

import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
	
	public static final int MY_NOTIFICATION_ID = 1;
	
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	
	public void onReceive(Context context, Intent intent) {
		
		mNotificationIntent = new Intent(context.getApplicationContext(), DailySelfieActivity.class);
		Log.i("Alarm", "alarm triggered");
		mContentIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, mNotificationIntent, 
				Intent.FLAG_ACTIVITY_NEW_TASK );
		
		Notification notificationBuilder = new Notification.Builder(context.getApplicationContext())
		    .setSmallIcon(android.R.drawable.ic_menu_camera)		    
		    .setTicker("It issssss selfie time!!!")
		    .setAutoCancel(true)
		    .setContentTitle("Daily Selfie")
			.setContentText("Selfie time")
			.setContentIntent(mContentIntent)
			.build();
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder);

	}	
}
