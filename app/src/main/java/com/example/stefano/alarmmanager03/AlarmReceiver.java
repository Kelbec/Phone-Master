package com.example.stefano.alarmmanager03;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    String type;
    int day;
    String phone;
    String callNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle extras = intent.getExtras();
        type = extras.getString("type", "*");
        day = extras.getInt("day",-1);
        phone = extras.getString("phone", "");
        try {
            Log.d("RECEIVER", "Receiver -> OnReceive() started, type: (" +type+ ") (day = " +day+ ")");

            startCallRoutine(context);

        }catch(Exception e){
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    // starting the phone call routine method
    private void startCallRoutine(Context context) {

        if(type.equals("start")){
            callNumber = "**21*"+phone+"#";
        } else if(type.equals("stop")){
            callNumber = "#002#";
        }
        else{
            callNumber = "";
        }
        Log.d("PHONE_NUMBER","(phone_number = "+callNumber+")");
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(callNumber)));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        context.startActivity(callIntent);
    }
}




/*

private void scheduleAlarm(int dayOfWeek) {

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

    // Check we aren't setting it in the past which would trigger it to fire instantly
    if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 7);
    }


    // Set this to whatever you were planning to do at the given time
    PendingIntent yourIntent;

    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, yourIntent);
}

private void setUpAlarms() {

    scheduleAlarm(Calendar.MONDAY);
    scheduleAlarm(Calendar.FRIDAY);
}
*/


/*
*
*   <<<<ORARI>>>>
*       Reperibilità telefono di servizio
*           Lun - Ven ----> 9 - 19:30
*           Sab ----------> 10 - 18
*       Reperibilità telefono privato
*           Lun - Ven ----> 19:30 - 9
*           Sab ----------> 18 - 10
*           Dom ----------> 0 - 24
*
* */

/*
        Sun = 1
        Lun = 2
        Mar = 3
*       Mer = 4
* */