package tencent.tmgp.sgame.other;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.SharedPreferences;
import tencent.tmgp.sgame.activity.Zactivity;

public class JsonUtils
{
 public static final String JSON_NAME = "name",
 JSON_CIRCLE = "circle", JSON_COLOR = "color",
 JSON_SOLID = "solid", JSON_SPACE = "space",
 JSON_TOP = "top", JSON_BOTTOM = "bottom",
 JSON_LEFT = "left",JSON_RIGHT = "right",
 JSON_STROKE = "stroke";  
 
 private JsonUtils()
 {}
 
 public static String sp2Json(SharedPreferences sp)
 {
  return new JSONObject(sp.getAll()).toString();
 }
 
 public static boolean json2SP(String json, SharedPreferences sp)
 {
  boolean success = false;
  try
  {
   JSONObject jo = new JSONObject(json);
   sp.edit().putString(Zactivity.MAIN_MODELS, jo.optString(Zactivity.MAIN_MODELS, null))
   .putInt(Zactivity.MAIN_HERO_SEL_INDEX, jo.optInt(Zactivity.MAIN_HERO_SEL_INDEX, -1))
   .putString(Zactivity.TEST_CIRCLE, jo.optString(Zactivity.TEST_CIRCLE, null))
   .putBoolean(Zactivity.TEST_BGC, jo.optBoolean(Zactivity.TEST_BGC, true))
   .putBoolean(Zactivity.TEST_OVAL, jo.optBoolean(Zactivity.TEST_OVAL, false))
   .putBoolean(Zactivity.TEST_FLOPOS, jo.optBoolean(Zactivity.TEST_FLOPOS, false))
   .commit();
   success = true;
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return success;
 }
 
 public static DashCircle parseJsonObject(String json)
 {
  DashCircle dc = new DashCircle();
  try
  {
   JSONObject jo = new JSONObject(json);
   // 增加右距自定义后，原数据没有该项
   // 直接取出会抛出异常
   // 所以先判断，没有就直接放入默认值
   if(!jo.has(JSON_RIGHT))
    jo.put(JSON_RIGHT, 1000);
   dc.setColor(jo.opt(JSON_COLOR));
   dc.setDashStyle((float)jo.optDouble(JSON_SOLID), (float)jo.optDouble(JSON_SPACE));
   dc.setDistance((float)jo.optDouble(JSON_TOP), (float)jo.optDouble(JSON_BOTTOM),
                  (float)jo.optDouble(JSON_LEFT), (float)jo.optDouble(JSON_RIGHT));
   dc.setStrokeWidth((float)jo.optDouble(JSON_STROKE));
  }catch(Exception e)
  {
   e.printStackTrace();
   dc.defInit();
  }
  return dc;
 }
 
 public static String toJsonObject(DashCircle dc)
 {
  String str = null;
  try
  {
   JSONObject jo = new JSONObject();
   jo.put(JSON_COLOR, dc.color);
   jo.put(JSON_SOLID, dc.solidLineLength);
   jo.put(JSON_SPACE, dc.spaceLength);
   jo.put(JSON_STROKE, dc.strokeWidth);
   jo.put(JSON_TOP, dc.mRectF.top);
   jo.put(JSON_BOTTOM, dc.mRectF.bottom);
   jo.put(JSON_LEFT, dc.mRectF.left);
   jo.put(JSON_RIGHT, dc.mRectF.right);
   str = jo.toString();
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return str;
 }
 
 public static List<BaseModel> parseJsonArray(String json)
 {
  List<BaseModel> list = new ArrayList<>();
  try
  {
   JSONArray ja = new JSONArray(json);
   for(int i = 0;i < ja.length();i++)
   {
    JSONObject jao = ja.optJSONObject(i);
    BaseModel bm = new BaseModel(jao.optString(JSON_NAME));
    JSONArray jaoa = jao.optJSONArray(JSON_CIRCLE);
    for(int j = 0;j < jaoa.length();j++)
    {
     JSONObject jaoao = jaoa.optJSONObject(j);
     // 增加右距自定义后，原数据没有该项
     // 直接取出会抛出异常
     // 所以先判断，没有就直接放入默认值
     if(!jaoao.has(JSON_RIGHT))
      jaoao.put(JSON_RIGHT, 1000);
     DashCircle dc = new DashCircle();
     dc.setColor(jaoao.opt(JSON_COLOR));
     dc.setDashStyle((float)jaoao.optDouble(JSON_SOLID), (float)jaoao.optDouble(JSON_SPACE));
     dc.setDistance((float)jaoao.optDouble(JSON_TOP), (float)jaoao.optDouble(JSON_BOTTOM),
                    (float)jaoao.optDouble(JSON_LEFT), (float)jaoao.optDouble(JSON_RIGHT));
     dc.setStrokeWidth((float)jaoao.optDouble(JSON_STROKE));
     bm.circles.add(dc);
    }
    list.add(bm);
   }
  }catch(Exception e)
  {
   e.printStackTrace();
  }
 
  return list;
 }
  
 public static String toJsonArray(List<BaseModel> list)
 {
  String str = null;
  try{
   JSONArray ja = new JSONArray();
   for(int i = 0;i < list.size();i++)
   {
    BaseModel bm = list.get(i);
    JSONObject jao = new JSONObject();
    jao.put(JSON_NAME, bm.name);
    JSONArray jaoa = new JSONArray();
    for(int j = 0;j < bm.circles.size();j++)
    {
     DashCircle dc = bm.circles.get(j);
     JSONObject jaoao = new JSONObject();
     jaoao.put(JSON_COLOR, dc.color);
     jaoao.put(JSON_SOLID, dc.solidLineLength);
     jaoao.put(JSON_SPACE, dc.spaceLength);
     jaoao.put(JSON_STROKE, dc.strokeWidth);
     jaoao.put(JSON_TOP, dc.mRectF.top);
     jaoao.put(JSON_BOTTOM, dc.mRectF.bottom);
     jaoao.put(JSON_LEFT, dc.mRectF.left);
     jaoao.put(JSON_RIGHT, dc.mRectF.right);
     jaoa.put(jaoao);
    }
    jao.put(JSON_CIRCLE, jaoa);
    ja.put(jao);
   }
   str = ja.toString();
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return str;
 }
 
 public static boolean containsName(List<BaseModel> list, String n)
 {
  if(n != null && list != null && list.size() > 0)
   for(BaseModel bm : list)
   {
    if(n.equals(bm.name))return true;
   }
  return false;
 }
 
// public void remove(int index)
// {
//  if(index >= names.size()) return;
//  models.remove(index);
//  names.remove(index);
// }
// 
// public void add(BaseModel bm)
// {
//  models.add(bm);
//  names.add(bm.name);
// }
// 
// public void set(int index, BaseModel bm)
// {
//  models.set(index, bm);
//  names.set(index, bm.name);
// }
 
}
