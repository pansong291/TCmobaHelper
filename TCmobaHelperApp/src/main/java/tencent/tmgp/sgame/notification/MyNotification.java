package tencent.tmgp.sgame.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class MyNotification
{
 public static final int MYNOTIFICATION_ID = 214;
 private static NotificationManager mNotifyManager;
 private static Notification mNotification;
 private static boolean isStartedNotification = false;

 private MyNotification()
 {}

 public static boolean isStartedNotification()
 {
  return isStartedNotification;
 }

 public static void startNotification(Service sv)
 {
  //懒汉式
  if(mNotification == null)
   synchronized(MyNotification.class)
   {
    if(mNotification == null)
    {
//     String appName;
//     try{
//      PackageInfo pif = sv.getPackageManager().getPackageInfo(sv.getPackageName(), 0);
//      int labelRes = pif.applicationInfo.labelRes;
//      appName = sv.getResources().getString(labelRes);
//     }catch(Exception e)
//     {
//      appName = sv.getPackageName();
//      e.printStackTrace();
//     }
     //mNotifyManager = (NotificationManager) sv.getSystemService(Context.NOTIFICATION_SERVICE);
     //Intent it = new Intent(sv, EditService.class);
     //PendingIntent pi = PendingIntent.getService(sv,0,it,PendingIntent.FLAG_UPDATE_CURRENT);
     mNotification = new Notification.Builder(sv)
      //.setLargeIcon(Icon.createWithResource(sv,R.drawable.ic_launcher))
      .setContentTitle("ContentTitle")
      .setContentText("ContentText")
      //.setWhen(System.currentTimeMillis())
      //.setContentIntent(pi)
      .build();
     
    }
   }
  if(!isStartedNotification)
  {
   sv.startForeground(MYNOTIFICATION_ID, mNotification);
   isStartedNotification = true;
  }
 }

 public static void stopNotification(Service sv, boolean b)
 {
  if(isStartedNotification)
  {
   sv.stopForeground(b);
   isStartedNotification = false;
  }
 }

}

