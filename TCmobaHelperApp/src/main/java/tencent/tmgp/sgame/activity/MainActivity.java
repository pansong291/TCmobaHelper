package tencent.tmgp.sgame.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import com.tencent.tmgp.sgame.R;
import java.util.List;
import tencent.tmgp.sgame.adapter.SpinnerArrayAdapter;
import tencent.tmgp.sgame.other.BaseModel;
import tencent.tmgp.sgame.other.FileUtils;
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
 AlertDialog dialog_hero_name, dialog_delete_hero, dialog_import_backup;
 EditText edt_hero_name = null;
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  init();
  
  ViewUtils.requestFloatWindow(this);
 }

 private void reLoadData()
 {
  models.clear();
  models.addAll(JsonUtils.parseJsonArray(opString(Zactivity.MAIN_MODELS, null)));
  spinnerArrayAdapter.notifyDataSetChanged();
  if(models.size() > 0)
  {
   selectedIndex = 0;
   selectedModel = models.get(selectedIndex);
  }else
  {
   selectedIndex = -1;
   selectedModel = null;
  }
 }
 
 private void init()
 {  
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
  
  if(models.size() > 0 && selectedIndex >= 0)
  {
   selectedModel = models.get(selectedIndex);
   spinner_hero.setSelection(selectedIndex);
  }
  
 }
 
 public void onExportClick(View v)
 {
  if(FileUtils.exportBackup(this))
   new AlertDialog.Builder(this)
    .setTitle("备份成功")
    .setMessage("备份文件的绝对路径为 " + FileUtils.getBackupFilePath(this))
    .show();
  else
   toast("备份失败");
 }
 
 public void onImportClick(View v)
 {
  if(dialog_import_backup == null)
   dialog_import_backup = new AlertDialog.Builder(this)
    .setTitle("导入备份")
    .setMessage("导入备份会覆盖当前已有数据，确定继续？")
    .setNegativeButton("取消", null)
    .setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
     @Override
     public void onClick(DialogInterface p1, int p2)
     {
      if(FileUtils.importBackup(MainActivity.this))
      {
       toast("导入成功");
       reLoadData();
      }else
       toast("导入失败");
     }
    })
    .create();
  dialog_import_backup.show();
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
      //selectedModel = new BaseModel(hero);
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
     if(models.size() <= 0)
     {
      selectedModel = null;
     }else
     {
      if(selectedIndex < 0)
       selectedIndex = 0;
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
 public boolean onCreateOptionsMenu(Menu menu)
 {
  Intent itHelp = new Intent(Intent.ACTION_VIEW);
  itHelp.setData(Uri.parse("https://github.com/pansong291/TCmobaHelper/wiki"));
  menu.add("帮助")
  .setIcon(android.R.drawable.ic_menu_help)
  .setIntent(itHelp)
  .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
  return super.onCreateOptionsMenu(menu);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ipInt(Zactivity.MAIN_HERO_SEL_INDEX, spinner_hero.getSelectedItemPosition());
 }
 
 
}
