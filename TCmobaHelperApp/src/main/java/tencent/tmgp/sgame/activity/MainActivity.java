package tencent.tmgp.sgame.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import com.tencent.tmgp.sgame.R;
import java.util.List;
import tencent.tmgp.sgame.activity.Zactivity;
import tencent.tmgp.sgame.adapter.SpinnerArrayAdapter;
import tencent.tmgp.sgame.other.BaseModel;
import tencent.tmgp.sgame.other.DashCircle;
import tencent.tmgp.sgame.other.JsonUtils;
import tencent.tmgp.sgame.other.ViewUtils;
import tencent.tmgp.sgame.service.MainService;

public class MainActivity extends Zactivity 
{
 public static List<BaseModel> models;
 public static Spinner spinner_hero;
 public static SpinnerArrayAdapter spinnerArrayAdapter;
 public static BaseModel selectedModel = null;
 public static int selectedIndex = -1;
 AlertDialog dialog_hero_name, dialog_delete_hero;
 EditText edt_hero_name = null;
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  init();
  
  ViewUtils.requestFloatWindow(this);
 }

 private void init()
 {
  // 测试代码 --start
  selectedModel = new BaseModel("");
  DashCircle dc1 = new DashCircle();
  dc1.setColor(Color.YELLOW);
  dc1.setDashStyle(30,20);
  dc1.setDistance(595, 1080-705, 1080-200);
  dc1.setStrokeWidth(5);
  DashCircle dc2 = new DashCircle();
  dc2.setColor(Color.YELLOW);
  dc2.setDashStyle(30,20);
  dc2.setDistance(405, 1080-795, 1080-20);
  dc2.setStrokeWidth(5);
//  DashCircle dc3 = new DashCircle();
//  dc3.setColor(Color.YELLOW);
//  dc3.setDashStyle(30,20);
//  dc3.setDi(270);
//  dc3.setStrokeWidth(5);
  selectedModel.circles.add(dc1);
  selectedModel.circles.add(dc2);
//  choosedModel.circles.add(dc3);
  
  // 测试代码 --end
  
  models = JsonUtils.parseJsonArray(opString(Zactivity.MAIN_MODELS, null));
  spinnerArrayAdapter = new SpinnerArrayAdapter(this, models);
  spinner_hero = findViewById(R.id.spinner_hero);
  spinner_hero.setAdapter(spinnerArrayAdapter);
  spinner_hero.setOnItemSelectedListener(new OnItemSelectedListener()
   {
    @Override
    public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
    {
     selectedIndex = p3;
     selectedModel = models.get(p3);
    }

    @Override
    public void onNothingSelected(AdapterView<?> p1)
    {
     // TODO: Implement this method
    }
   });
  selectedIndex = opInt(Zactivity.MAIN_HERO_SEL_INDEX, -1);
  
  if(selectedIndex >= 0)
  {
   selectedModel = models.get(selectedIndex);
   spinner_hero.setSelection(selectedIndex);
  }
  
 }
 
 public void onNewHeroClick(View v)
 {
  if(edt_hero_name == null)
   edt_hero_name = new EditText(this);
  if(dialog_hero_name == null)
   dialog_hero_name = new AlertDialog.Builder(this)
   .setTitle("输入方案名称")
   .setView(edt_hero_name)
   .setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
     @Override
     public void onClick(DialogInterface p1, int p2)
     {
      String hero = edt_hero_name.getText().toString();
      if(hero.length() < 1)
      {
       toast("名称不能为空");
       return;
      }
      if(JsonUtils.containsName(models, hero))
      {
       toast("该名称已存在");
       return;
      }
      edt_hero_name.getText().clear();
      selectedModel = new BaseModel(hero);
      selectedIndex = models.size();
      Intent it = new Intent(MainActivity.this,ModelEditActivity.class);
      it.putExtra(JsonUtils.JSON_NAME,hero);
      startActivity(it);
     }
    })
   .setNegativeButton("取消",null)
   .create();
  dialog_hero_name.show();
 }
 
 public void onUpdateClick(View v)
 {
  if(selectedIndex < 0 || selectedModel == null)
  {
   toast("当前无可行方案");
   return;
  }
  selectedModel = new BaseModel(selectedModel);
  Intent it = new Intent(this,ModelEditActivity.class);
  startActivity(it);
 }
 
 public void onDeleteClick(View v)
 {
  if(selectedIndex < 0 || selectedModel == null)
  {
   toast("当前无可行方案");
   return;
  }
  if(dialog_delete_hero == null)
  dialog_delete_hero = new AlertDialog.Builder(this)
  .setTitle("删除")
  .setMessage("")
  .setPositiveButton("确定删除", new DialogInterface.OnClickListener()
   {
    @Override
    public void onClick(DialogInterface p1, int p2)
    {
     models.remove(selectedIndex--);
     spinnerArrayAdapter.notifyDataSetChanged();
     if(selectedIndex < 0)
      selectedModel = null;
     else
     {
      selectedModel = models.get(selectedIndex);
      spinner_hero.setSelection(selectedIndex);
     }
     ipString(Zactivity.MAIN_MODELS, JsonUtils.toJsonArray(models));
     toast("已删除");
    }
   })
  .setNegativeButton("取消",null)
  .create();
  dialog_delete_hero.show();
  dialog_delete_hero.setMessage("确定要删除方案 " + models.get(selectedIndex).name + " 吗？");
 }
 
 public void onTestClick(View v)
 {
  Intent intent = new Intent(this,TestActivity.class);
  startActivity(intent);
 }
 
 public void onStartClick(View v)
 {
  if(selectedIndex < 0 || selectedModel == null)
  {
   toast("当前无可行方案");
   return;
  }
  Intent intent = new Intent(this,MainService.class);
  startService(intent);
 }
 
 public void onCloseClick(View v)
 {
  if(selectedIndex < 0 || selectedModel == null)
  {
   toast("当前无可行方案");
   return;
  }
  Intent intent = new Intent(this,MainService.class);
  stopService(intent);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ipInt(Zactivity.MAIN_HERO_SEL_INDEX, spinner_hero.getSelectedItemPosition());
 }
 
 
}
