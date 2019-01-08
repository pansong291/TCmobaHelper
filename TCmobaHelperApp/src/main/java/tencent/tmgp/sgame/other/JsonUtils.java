package tencent.tmgp.sgame.other;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils
{
 public static final String JSON_NAME = "name",
 JSON_CIRCLE = "circle", JSON_COLOR = "color",
 JSON_SOLID = "solid", JSON_SPACE = "space",
 JSON_LEFT = "left", JSON_TOP = "top",
 JSON_BOTTOM = "bottom", JSON_STROKE = "stroke";  
 
 private JsonUtils()
 {}
 
 public static DashCircle parseJsonObject(String json)
 {
  DashCircle dc = new DashCircle();
  try
  {
   JSONObject jo = new JSONObject(json);
   dc.setColor(jo.opt(JSON_COLOR));
   dc.setDashStyle((float)jo.optDouble(JSON_SOLID), (float)jo.optDouble(JSON_SPACE));
   dc.setDistance((float)jo.optDouble(JSON_LEFT), (float)jo.optDouble(JSON_TOP), (float)jo.optDouble(JSON_BOTTOM));
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
   jo.put(JSON_LEFT, dc.mRectF.left);
   jo.put(JSON_TOP, dc.mRectF.top);
   jo.put(JSON_BOTTOM, dc.mRectF.bottom);
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
     DashCircle dc = new DashCircle();
     dc.setColor(jaoao.opt(JSON_COLOR));
     dc.setDashStyle((float)jaoao.optDouble(JSON_SOLID), (float)jaoao.optDouble(JSON_SPACE));
     dc.setDistance((float)jaoao.optDouble(JSON_LEFT), (float)jaoao.optDouble(JSON_TOP), (float)jaoao.optDouble(JSON_BOTTOM));
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
     jaoao.put(JSON_LEFT, dc.mRectF.left);
     jaoao.put(JSON_TOP, dc.mRectF.top);
     jaoao.put(JSON_BOTTOM, dc.mRectF.bottom);
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
