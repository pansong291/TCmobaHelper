package tencent.tmgp.sgame.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import tencent.tmgp.sgame.activity.Zactivity;

public class ViewUtils
{
 private static int statusHeight = -1;
 private static int screenHeight = -1;
 
 //获取状态栏高度
 public static int getStatusHeight(Context c)
 {
  if(statusHeight <= 0)
  {
   try
   {
    Class<?>c1=Class.forName("com.android.internal.R$dimen");
    Object o1=c1.newInstance();
    int i2=Integer.parseInt(c1.getField("status_bar_height").get(o1).toString());
    statusHeight = c.getResources().getDimensionPixelSize(i2);
   }catch(Exception e)
   {
    e.printStackTrace();
   }
  }
  return statusHeight;
 }
 
 //获取屏幕高度
 public static int getScreenHeight(Context c)
 {
  if(screenHeight <= 0)
  {
   WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE); 
   screenHeight = Math.max(wm.getDefaultDisplay().getHeight(), wm.getDefaultDisplay().getWidth()); 
  }
  return screenHeight;
 }
 
 //转成十六进制颜色
 public static String getHexColor(int color)
 {
  //String.format("#%08X", color)
  StringBuffer sb = new StringBuffer("#");
  String str;
  int alpha = Color.alpha(color);
  if(alpha < 0xff)
  {
   str = Integer.toHexString(alpha);
   if(str.length() < 2)sb.append('0');
   sb.append(str);
  }
  str = Integer.toHexString(Color.red(color));
  if(str.length() < 2)sb.append('0');
  sb.append(str);
  str = Integer.toHexString(Color.green(color));
  if(str.length() < 2)sb.append('0');
  sb.append(str);
  str = Integer.toHexString(Color.blue(color));
  if(str.length() < 2)sb.append('0');
  sb.append(str);
  return sb.toString();
 }
 
 public static Bitmap getResBitmap(int res, Context c)
 {
  //获取res下的图片
  return BitmapFactory.decodeResource(
   c.getResources(), res);
  /*
   InputStream is;
   //获取assets目录里的图片流
   try{
   is=getResources().getAssets().open("pic.png");
   }catch(IOException e)
   {
   return null;
   }
   //将图片流转换成Bitmap并返回
   return BitmapFactory.decodeStream(is);
   */
 }

 public static Matrix getImgFitCenterMatrix(Bitmap mbitmap, View v)
 {
  Matrix matrix = new Matrix();
  float bitH = mbitmap.getHeight(),
   bitW = mbitmap.getWidth();
  float scale = bitW/bitH < v.getWidth()/v.getHeight() ?
   v.getHeight()/bitH : v.getWidth()/bitW;
  matrix.postTranslate((v.getWidth()-bitW)/2, (v.getHeight()-bitH)/2);
  matrix.postScale(scale, scale, v.getWidth()/2, v.getHeight()/2);
  return matrix;
 }
 
 // 请求悬浮窗权限
 public static void requestFloatWindow(final Zactivity ac)
 {
  if(Build.VERSION.SDK_INT >= 23)
  {
   if(!Settings.canDrawOverlays(ac))
   {
    //若没有权限，提示获取.
    //AlertDialog wad = 
    new AlertDialog.Builder(ac)
     .setTitle("提示")
     .setMessage("需要为本应用开启悬浮窗权限，请在弹出的页面中设置为允许。\n\nvivo等设备若重新打开软件仍显示此对话框，请自行前往设置-更多设置-权限管理，为本软件开启悬浮窗权限。")
     .setCancelable(false)
     .setNegativeButton("取消", null)
     .setPositiveButton("确定", new Dialog.OnClickListener()
     {
      @Override
      public void onClick(DialogInterface p1, int p2)
      {
       Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
       ac.startActivity(intent);
       ac.finish();
      }
     }).show();
    //wad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    //wad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
   }
  }
 }
 
}
