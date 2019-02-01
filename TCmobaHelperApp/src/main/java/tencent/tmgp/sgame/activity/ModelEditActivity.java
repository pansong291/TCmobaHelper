package tencent.tmgp.sgame.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.a4455jkjh.colorpicker.view.TestView;
import com.tencent.tmgp.sgame.R;
import tencent.tmgp.sgame.adapter.CircleListAdapter;
import tencent.tmgp.sgame.listener.ModelEditViewListener;
import tencent.tmgp.sgame.other.DashCircle;
import tencent.tmgp.sgame.other.JsonUtils;
import tencent.tmgp.sgame.other.BaseModel;

public class ModelEditActivity extends Zactivity
{
 public BaseModel currentModel;
 ListView list_circle;
 public CircleListAdapter circleListAdapter;
 ModelEditViewListener listener;
 
 public DashCircle currentCircle;
 
 AlertDialog dialog_hero_name, dialog_circle_data, dialog_cancel;
 EditText edt_hero_name;
 View view_dialog_circle_data = null;
 View linearlayout_color;
 public TestView tst_color;
 public EditText edt_solid, edt_space, edt_stroke,
  edt_top, edt_bottom, edt_left, edt_right;
 
 String newHeroName = null;
 
 public boolean dataChanged = false;
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_editmodel);

  
  newHeroName = getIntent().getStringExtra(JsonUtils.JSON_NAME);
  if(newHeroName == null || newHeroName.isEmpty())
  {
   currentModel = new BaseModel(MainActivity.selectedModel);
   newHeroName = currentModel.name;
  }else
  {
   currentModel = new BaseModel(newHeroName);
  }
  setTitle(newHeroName);
  
  init();
  
 }
 
 private void init()
 {
  list_circle = findViewById(R.id.list_circle);
  circleListAdapter = new CircleListAdapter(this, currentModel.circles);
  listener = new ModelEditViewListener(this);
  list_circle.setOnItemClickListener(listener);
  list_circle.setOnItemLongClickListener(listener);
  list_circle.setAdapter(circleListAdapter);
 }
 
 public void onRenameClick(View v)
 {
  if(edt_hero_name == null)
   edt_hero_name = new EditText(this);
  if(dialog_hero_name == null)
   dialog_hero_name = new AlertDialog.Builder(this)
    .setTitle("方案名称")
    .setView(edt_hero_name)
    .setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
     @Override
     public void onClick(DialogInterface p1, int p2)
     {
      String hero = edt_hero_name.getText().toString();
      if(hero.equals(currentModel.name))
       return;
      if(JsonUtils.containsName(MainActivity.models, hero))
      {
       toast("该名称已存在");
       return;
      }
      dataChanged = true;
      ModelEditActivity.this.setTitle(hero);
      currentModel.name = hero;
     }
    })
    .setNegativeButton("取消",null)
    .create();
  dialog_hero_name.show();
  edt_hero_name.setText(currentModel.name);
 }
 
 public void onNewCircleClick(View v)
 {
  dataChanged = true;
  DashCircle dc = new DashCircle();
  dc.setColor(Color.rgb(0,255,255));
  dc.setDashStyle(30,20);
  dc.setDistance(20,1000,20,1000);
  dc.setStrokeWidth(10);
  currentModel.circles.add(dc);
  circleListAdapter.notifyDataSetChanged();
 }
 
 public void onSaveHeroClick(View v)
 {
  if(!dataChanged)
  {
   toast("未作任何更改");
   return;
  }
  if(MainActivity.selectedIndex >= MainActivity.models.size())
  {
   MainActivity.models.add(currentModel);
   MainActivity.spinnerArrayAdapter.notifyDataSetChanged();
   MainActivity.spinner_hero.setSelection(MainActivity.selectedIndex);
  }else
  {
   MainActivity.models.set(MainActivity.selectedIndex, currentModel);
   MainActivity.selectedModel = currentModel;
  }
  ipString(Zactivity.MAIN_MODELS, JsonUtils.toJsonArray(MainActivity.models));
  finish();
 }
 
 public void onCancelClick(View v)
 {
  if(!dataChanged)
  {
   finish();
   return;
  }
  if(dialog_cancel == null)
  dialog_cancel = new AlertDialog.Builder(this)
  .setTitle("放弃更改")
  .setMessage("确定要放弃所有更改吗？")
  .setPositiveButton("确定放弃", new DialogInterface.OnClickListener()
   {
    @Override
    public void onClick(DialogInterface p1, int p2)
    {
//     if(MainActivity.selectedIndex >= 0)
//     {
//      if(MainActivity.selectedIndex < MainActivity.models.size()){
//       MainActivity.selectedModel = MainActivity.models.get(MainActivity.selectedIndex);
//      }else
//      {
       MainActivity.selectedIndex = MainActivity.spinner_hero.getSelectedItemPosition();
//       MainActivity.selectedModel = MainActivity.selectedIndex < 0 ?
//        null : MainActivity.models.get(MainActivity.selectedIndex);
//      }
//     }
     ModelEditActivity.this.finish();
    }
   })
  .setNegativeButton("取消",null)
  .create();
  dialog_cancel.show();
 }

 @Override
 public void onBackPressed()
 {
  onCancelClick(null);
 }
 
 public void setCircleData(int which)
 {
  if(view_dialog_circle_data == null)
  {
   view_dialog_circle_data = LayoutInflater.from(this).inflate(R.layout.dialog_circle,null);
   linearlayout_color = view_dialog_circle_data.findViewById(R.id.linearlayout_color);
   linearlayout_color.setOnClickListener(listener);
   tst_color = view_dialog_circle_data.findViewById(R.id.tst_color);
   edt_left = view_dialog_circle_data.findViewById(R.id.edt_left);
   edt_right = view_dialog_circle_data.findViewById(R.id.edt_right);
   edt_top = view_dialog_circle_data.findViewById(R.id.edt_top);
   edt_bottom = view_dialog_circle_data.findViewById(R.id.edt_bottom);
   edt_solid = view_dialog_circle_data.findViewById(R.id.edt_solid);
   edt_space = view_dialog_circle_data.findViewById(R.id.edt_space);
   edt_stroke = view_dialog_circle_data.findViewById(R.id.edt_stroke);
  }
   
  if(dialog_circle_data == null)
   dialog_circle_data = new AlertDialog.Builder(this)
   .setTitle("曲线")
   .setView(view_dialog_circle_data)
   .setPositiveButton("确定",listener)
   .setNegativeButton("取消",null)
   .create();
  dialog_circle_data.show();
  
  dialog_circle_data.setTitle("曲线" + (1 + which));
  
  currentCircle = currentModel.circles.get(which);
  tst_color.setInitColor(currentCircle.color);
  edt_left.setText(""+(int)currentCircle.mRectF.left);
  edt_right.setText(""+(int)currentCircle.mRectF.right);
  edt_top.setText(""+(int)currentCircle.mRectF.top);
  edt_bottom.setText(""+(int)currentCircle.mRectF.bottom);
  edt_solid.setText(""+(int)currentCircle.solidLineLength);
  edt_space.setText(""+(int)currentCircle.spaceLength);
  edt_stroke.setText(""+(int)currentCircle.strokeWidth);
 }
 
}
