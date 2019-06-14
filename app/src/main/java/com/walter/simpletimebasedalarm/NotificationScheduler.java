package com.walter.simpletimebasedalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public  class NotificationScheduler
{
    //https://droidmentor.com/schedule-notifications-using-alarmmanager/
    public  static final int DAILY_REMINDER_REQUEST_CODE=1000;
    static String TAG="ALARM_APP";
    public static void setReminder(Context context, Class<?> cls, int hour, int min){
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
        cancelReminder(context,cls);

       if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        //setcalendar.add(Calendar.HOUR_OF_DAY,24);
        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,     DAILY_REMINDER_REQUEST_CODE, intent1,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d(TAG, "onReceive: Alarm set");
    }

    public static void cancelReminder(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d(TAG, "onReceive: Alarm cancelled");
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            final int NOTIFY_ID = 0; // ID of notification
            String id = "default_channel_id"; // default_channel_id
            String tittle =  "Default Channel";
            Intent intent;
            //PendingIntent pendingIntent;
            NotificationManager notifManager=null;
            NotificationCompat.Builder builder;
            if (notifManager == null) {
                notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(title)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(content);
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);


        }else {

            String channelId = "default_channel_id";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);
            Notification notification = builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
            Log.d(TAG, "onReceive: Alarm showing");
       }
    }
}

//https://bit.ly/2XLBcP0