package com.walter.simpletimebasedalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
  EditText inputTime, inputMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputTime=findViewById(R.id.inputTime);
        inputMinutes=findViewById(R.id.inputMins);
    }




    public void trigger(View view)
    {
        /*String t = inputTime.getText().toString().trim();
        String m = inputMinutes.getText().toString().trim();
        if (t.isEmpty() || m.isEmpty())
        {
            Toast.makeText(this, "Invalid Time", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = Integer.parseInt(t);
        int minutes = Integer.parseInt(m);*/

        Calendar now = Calendar.getInstance();
        int hour =now.get(Calendar.HOUR_OF_DAY);
        int minutes= now.get(Calendar.MINUTE)+1;

        NotificationScheduler.setReminder(MainActivity.this,AlarmReceiver.class, hour,minutes);//alarm will ring at 11:40
        Toast.makeText(this, "Alarm Set set at "+hour+":"+minutes, Toast.LENGTH_SHORT).show();



    }


}
