package tencent.tmgp.sgame.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import pansong291.crash.ASControl;

public class Zactivity extends Activity
{
 public static final String MAIN_MODELS = "models",
 MAIN_HERO_SEL_INDEX = "hero_sel_index",
 TEST_FLOPOS = "test_flopos", TEST_BGC = "test_bgc",
 TEST_OVAL = "test_oval",TEST_CIRCLE = "test_circle";
 
 public static final String TAG_PACKAGE = "com.tencent.tmgp.sgame";
 
 public SharedPreferences sp;

 @Override
 protected void onResume()
 {
  super.onResume();
  
 }
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ASControl.getASControl().addActivity(this);
  sp=getSharedPreferences(getPackageName()+"_preferences",0);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ASControl.getASControl().removeActivity(this);
 }

 public void ipBoolean(String k,boolean v)
 {
  sp.edit().putBoolean(k,v).commit();
 }
 
 public boolean opBoolean(String k,boolean v)
 {
  return sp.getBoolean(k,v);
 }
 
 public void ipString(String k,String v)
 {
  sp.edit().putString(k,v).commit();
 }

 public String opString(String k,String v)
 {
  return sp.getString(k,v);
 }
 
 public void ipInt(String k,int v)
 {
  sp.edit().putInt(k,v).commit();
 }
 
 public int opInt(String k,int v)
 {
  return sp.getInt(k,v);
 }
 
 public void toast(String s)
 {
  toast(s, 0);
 }
 
 public void toast(String s, int i)
 {
  Toast.makeText(this, s, i).show();
 }
 
}
