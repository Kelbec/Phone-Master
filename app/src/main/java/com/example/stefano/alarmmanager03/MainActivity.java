package com.example.stefano.alarmmanager03;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private PendingIntent pendingIntentStart;
    private PendingIntent pendingIntentStop;
    EditText phoneEditText;
    String phone;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneEditText = findViewById(R.id.editPhoneNumber);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        findViewById(R.id.setButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPhone();
            }
        });

        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        findViewById(R.id.startAtButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAt();
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    private void start() {
        String callNumber;
        if(flag){
            callNumber = "**21*"+phone+"#";
            flag = !flag;
        } else if(!flag){
            callNumber = "#002#";
            flag = !flag;
        }
        else{
            callNumber = "";
        }
        Log.d("PHONE_NUMBER","(phone_number = "+callNumber+") --- flag = "+flag);
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(callNumber)));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        startActivity(callIntent);

        //AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 8000;
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntentStart);
    }

    private void setPhone() {
        phone = String.valueOf(phoneEditText.getText());

        Toast.makeText(this,"Phone = "+phone, Toast.LENGTH_SHORT).show();
    }

    private void cancel() {
        try{
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            manager.cancel(pendingIntentStart);
            manager.cancel(pendingIntentStop);
            Toast.makeText(this, "Service canceled", Toast.LENGTH_SHORT).show();
            Log.d("TAG","Alarm Canceled");
        }catch(Exception e){
            Log.d("Exception", e.toString());
        }

    }

    private void startAt() {
        scheduleAlarms(Calendar.MONDAY);
        scheduleAlarms(Calendar.TUESDAY);
        scheduleAlarms(Calendar.WEDNESDAY);
        scheduleAlarms(Calendar.THURSDAY);
        scheduleAlarms(Calendar.FRIDAY);
        scheduleAlarms(Calendar.SATURDAY);
        //scheduleAlarms(Calendar.SUNDAY);
        Toast.makeText(this,"Service scheduled", Toast.LENGTH_SHORT).show();

    }

    private void scheduleAlarms(int day) {
        Intent alarmIntentStart = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntentStart.putExtra("day", day);
        alarmIntentStart.putExtra("type","start");
        alarmIntentStart.putExtra("phone", phone);

        Intent alarmIntentStop = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntentStop.putExtra("day", day);
        alarmIntentStop.putExtra("type","stop");
        alarmIntentStart.putExtra("phone", phone);

        pendingIntentStart = PendingIntent.getBroadcast(MainActivity.this, day+10, alarmIntentStart, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntentStop = PendingIntent.getBroadcast(MainActivity.this, day+20, alarmIntentStop, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager managerStart = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlarmManager managerStop = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 604800000;   // week time in ms

        Calendar alarmCalendarStart = Calendar.getInstance();

        Calendar alarmCalendarStop = Calendar.getInstance();

        // For mid-week day setup an alarm different from saturday, no alarm for sunday

        Calendar c = Calendar.getInstance();



        if(day == Calendar.SATURDAY){
            alarmCalendarStart.set(Calendar.DAY_OF_WEEK, day);  //
            alarmCalendarStart.set(Calendar.HOUR_OF_DAY, 10);   // Start time 10.00
            alarmCalendarStart.set(Calendar.MINUTE, 0);         //

            if(c.after(alarmCalendarStart)){
                alarmCalendarStart.add(Calendar.MILLISECOND,interval);
                Log.d("TIME_START","Day "+ day +" alarm postponed to "+alarmCalendarStart.getTimeInMillis());
            }
            Log.d("STRT ALARM SET",  "Day: "+alarmCalendarStart.get(Calendar.DAY_OF_WEEK)+
                                                " Hour: "+alarmCalendarStart.get(Calendar.HOUR_OF_DAY)+
                                                " Minute: "+alarmCalendarStart.get(Calendar.MINUTE));

            alarmCalendarStop.set(Calendar.DAY_OF_WEEK, day);   //
            alarmCalendarStop.set(Calendar.HOUR_OF_DAY, 18);    // Stop time 18.00
            alarmCalendarStop.set(Calendar.MINUTE, 0);          //

            if(c.after(alarmCalendarStop)){
                alarmCalendarStop.add(Calendar.MILLISECOND,interval);
                Log.d("TIME_STOP","Day "+ day +" alarm postponed to "+alarmCalendarStop.getTimeInMillis());
            }
            Log.d("STOP ALARM SET",  "Day: "+alarmCalendarStop.get(Calendar.DAY_OF_WEEK)+
                                                " Hour: "+alarmCalendarStop.get(Calendar.HOUR_OF_DAY)+
                                                " Minute: "+alarmCalendarStop.get(Calendar.MINUTE));

        } else {
            alarmCalendarStart.set(Calendar.DAY_OF_WEEK, day);         //
            alarmCalendarStart.set(Calendar.HOUR_OF_DAY, 9);           // Start time 9.00
            alarmCalendarStart.set(Calendar.MINUTE, 0);                //

            if(c.after(alarmCalendarStart)){
                alarmCalendarStart.add(Calendar.MILLISECOND,interval);
                Log.d("TIME_START","Day "+ day +" alarm postponed to "+alarmCalendarStart.getTimeInMillis());
            }
            Log.d("STRT ALARM SET",  "Day: "+alarmCalendarStart.get(Calendar.DAY_OF_WEEK)+
                                                " Hour: "+alarmCalendarStart.get(Calendar.HOUR_OF_DAY)+
                                                " Minute: "+alarmCalendarStart.get(Calendar.MINUTE));

            alarmCalendarStop.set(Calendar.DAY_OF_WEEK, day);           //
            alarmCalendarStop.set(Calendar.HOUR_OF_DAY, 19);            // Stop time 19.30
            alarmCalendarStop.set(Calendar.MINUTE, 30);                 //

            if(c.after(alarmCalendarStop)){
                alarmCalendarStop.add(Calendar.MILLISECOND,interval);
                Log.d("TIME_STOP","Day "+ day +" alarm postponed to "+alarmCalendarStop.getTimeInMillis());
            }
            Log.d("STOP ALARM SET",  "Day: "+alarmCalendarStop.get(Calendar.DAY_OF_WEEK)+
                                                " Hour: "+alarmCalendarStop.get(Calendar.HOUR_OF_DAY)+
                                                " Minute: "+alarmCalendarStop.get(Calendar.MINUTE));
        }

        // Set repeating start and stop alarms
        try{
            managerStart.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendarStart.getTimeInMillis(),
                    interval, pendingIntentStart);
        }catch(Exception e){
            Log.d("EXCEPTION",e.toString());
        }
        try{
            managerStop.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendarStop.getTimeInMillis(),
                    interval, pendingIntentStop);
        }catch(Exception e){
            Log.d("EXCEPTION",e.toString());
        }
    }
}