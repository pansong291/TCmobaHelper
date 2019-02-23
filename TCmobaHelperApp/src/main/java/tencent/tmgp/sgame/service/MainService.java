package tencent.tmgp.sgame.service;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.a4455jkjh.colorpicker.view.TestView;
import com.tencent.tmgp.sgame.R;
import pansong291.DashCircleView;
import tencent.tmgp.sgame.activity.MainActivity;
import tencent.tmgp.sgame.activity.Zactivity;
import tencent.tmgp.sgame.adapter.SpinnerArrayAdapter;
import tencent.tmgp.sgame.listener.MainServiceListener;
import tencent.tmgp.sgame.notification.MyNotification;
import tencent.tmgp.sgame.other.BaseModel;

public class MainService extends Zservice 
{
 //创建浮动窗口设置布局参数的对象
 public WindowManager mWindowManager;
 //定义浮动窗口布局
 private RelativeLayout mFloatLayout1;
 public DashCircleView dashcircleview;
 
 public LinearLayout mFloatLayout2;
 public LayoutParams wmParams2;
 public Button btn_close,btn_reset, btn_save, btn_sub1,
 btn_sub10,btn_sub100, btn_add1, btn_add10, btn_add100;
 public Switch switch_bgc, switch_oval;
 public Spinner spinner_hero, spinner_circle, spinner_tblr;
 public View linearlayout_color;
 public TextView txt_value, txt_0;
 public TestView tst_color;
 
 MainServiceListener listener;
 
 public int selectedIndex;
 public BaseModel currentModel;
 
 SpinnerArrayAdapter adapter_hero;
 public ArrayAdapter<String> adapter_circle;
 ArrayAdapter<String> adapter_tblr;
 
 public static final String START_FROM_NOTIFICATION = "ssffnn";

 @Override
 public void onCreate() 
 {
  // TODO Auto-generated method stub
  super.onCreate();

  createFloatView();
  initEvent();
  initValue();
 }

 @Override
 public int onStartCommand(Intent intent, int flags, int startId)
 {
  MyNotification.startNotification(this);
   
  if(intent.getBooleanExtra(START_FROM_NOTIFICATION, false))
  {
   if(wmParams2 == null)
   {
    wmParams2 = new LayoutParams();
    wmParams2.type = LayoutParams.TYPE_PHONE;
    wmParams2.format = PixelFormat.RGBA_8888;
    wmParams2.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL;
    wmParams2.gravity = Gravity.LEFT | Gravity.TOP;
    wmParams2.x = 0;
    wmParams2.y = 0;
    wmParams2.width = LayoutParams.WRAP_CONTENT;
    wmParams2.height = LayoutParams.MATCH_PARENT;
   }
   try{
    mWindowManager.addView(mFloatLayout2, wmParams2);
    mFloatLayout2.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
   }catch(Exception e)
   {
    e.printStackTrace();
   }
  }else if(dashcircleview != null)
  {
   selectedIndex = MainActivity.selectedIndex;
   currentModel = new BaseModel(MainActivity.selectedModel);
   dashcircleview.setBaseModel(currentModel);
   dashcircleview.invalidate();
  }
  spinner_hero.setSelection(selectedIndex);
  updateCircleAdapter();

  return super.onStartCommand(intent, flags, startId);
 }

 @Override
 public IBinder onBind(Intent intent)
 {
  // TODO Auto-generated method stub
  return null;
 }
 
 private void createFloatView()
 {
  //获取的是WindowManagerImpl.CompatModeWrapper
  mWindowManager = (WindowManager)getApplication().getSystemService(WINDOW_SERVICE);
  LayoutParams wmParams = new LayoutParams();
  //设置window type
  wmParams.type = LayoutParams.TYPE_PHONE;
  //设置图片格式，效果为背景透明
  wmParams.format = PixelFormat.RGBA_8888;
  //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
  wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_TOUCHABLE;
  //调整悬浮窗显示的停靠位置为左侧置顶
  wmParams.gravity = Gravity.LEFT | Gravity.TOP;
  //以屏幕左上角为原点，设置x、y初始值，相对于gravity
  wmParams.x = 0;
  wmParams.y = 0;

  //设置悬浮窗口长宽数据  
  wmParams.width = LayoutParams.MATCH_PARENT;
  wmParams.height = LayoutParams.MATCH_PARENT;
  /*// 设置悬浮窗口长宽数据
   wmParams.width=200;
   wmParams.height=80;*/

  LayoutInflater inflater = LayoutInflater.from(getApplication());
  //获取浮动窗口视图所在布局
  mFloatLayout1 = (RelativeLayout)inflater.inflate(R.layout.flowin_main, null);

  dashcircleview = mFloatLayout1.findViewById(R.id.dashcircleview);
  
  mFloatLayout2 = (LinearLayout)inflater.inflate(R.layout.flowin_edit, null);
  btn_close = mFloatLayout2.findViewById(R.id.btn_close);
  btn_reset = mFloatLayout2.findViewById(R.id.btn_reset);
  btn_save = mFloatLayout2.findViewById(R.id.btn_save);
  btn_sub1 = mFloatLayout2.findViewById(R.id.btn_sub1);
  btn_sub10 = mFloatLayout2.findViewById(R.id.btn_sub10);
  btn_sub100 = mFloatLayout2.findViewById(R.id.btn_sub100);
  btn_add1 = mFloatLayout2.findViewById(R.id.btn_add1);
  btn_add10 = mFloatLayout2.findViewById(R.id.btn_add10);
  btn_add100 = mFloatLayout2.findViewById(R.id.btn_add100);
  switch_bgc = mFloatLayout2.findViewById(R.id.switch_bgc);
  switch_oval = mFloatLayout2.findViewById(R.id.switch_oval);
  spinner_hero = mFloatLayout2.findViewById(R.id.spinner_hero);
  spinner_circle = mFloatLayout2.findViewById(R.id.spinner_circle);
  spinner_tblr = mFloatLayout2.findViewById(R.id.spinner_tblr);
  linearlayout_color = mFloatLayout2.findViewById(R.id.linearlayout_color);
  txt_value = mFloatLayout2.findViewById(R.id.txt_value);
  txt_0 = mFloatLayout2.findViewById(R.id.txt_0);
  tst_color = mFloatLayout2.findViewById(R.id.tst_color);
  
  //添加mFloatLayout
  mWindowManager.addView(mFloatLayout1,wmParams);
  mFloatLayout1.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

 }
 
 private void initEvent()
 {
  listener = new MainServiceListener(this);
  btn_sub1.setOnClickListener(listener);
  btn_sub10.setOnClickListener(listener);
  btn_sub100.setOnClickListener(listener);
  btn_add1.setOnClickListener(listener);
  btn_add10.setOnClickListener(listener);
  btn_add100.setOnClickListener(listener);
  switch_bgc.setOnCheckedChangeListener(listener);
  switch_oval.setOnCheckedChangeListener(listener);
  spinner_hero.setOnItemSelectedListener(listener);
  spinner_circle.setOnItemSelectedListener(listener);
  spinner_tblr.setOnItemSelectedListener(listener);
  linearlayout_color.setOnClickListener(listener);
  btn_close.setOnTouchListener(listener);
  btn_reset.setOnTouchListener(listener);
  btn_save.setOnTouchListener(listener);
 }
 
 private void initValue()
 {
  adapter_hero = new SpinnerArrayAdapter(this, MainActivity.models);
  spinner_hero.setAdapter(adapter_hero);
  
  adapter_circle = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
  adapter_circle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  spinner_circle.setAdapter(adapter_circle);
  
  adapter_tblr = new ArrayAdapter<String>(
   this,android.R.layout.simple_spinner_item, new String[]{"顶距","底距","左距","右距","实长","隙长","厚度"});
  adapter_tblr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  spinner_tblr.setAdapter(adapter_tblr);
  
  switch_bgc.setChecked(opBoolean(Zactivity.TEST_BGC, false));
 }
 
 public void updateCircleAdapter()
 {
  for(int i = adapter_circle.getCount();i < currentModel.circles.size(); i++)
  {
   adapter_circle.add("曲线" + (i + 1));
  }
  while(adapter_circle.getCount() > currentModel.circles.size())
  {
   adapter_circle.remove(adapter_circle.getItem(adapter_circle.getCount() - 1));
  }
  adapter_circle.notifyDataSetChanged();
  if(spinner_circle.getSelectedItemPosition() >= 0 && spinner_circle.getSelectedItemPosition() < adapter_circle.getCount())
   listener.onItemSelected(spinner_circle, null, spinner_circle.getSelectedItemPosition(), 0);
 }
 
 public void removeEditView()
 {
  if(mFloatLayout2 != null)
  {
   try{
    mWindowManager.removeView(mFloatLayout2);
   }catch(Exception e)
   {
    e.printStackTrace();
   }
  }
 }
 
 @Override
 public void onDestroy()
 {
  // TODO Auto-generated method stub
  super.onDestroy();
  MyNotification.stopNotification(this, true);
  if(mFloatLayout1 != null)
  {
   //移除悬浮窗口
   mWindowManager.removeView(mFloatLayout1);
  }
  removeEditView();
  
  sp.edit()
   .putBoolean(Zactivity.TEST_BGC, switch_bgc.isChecked())
   .commit();
 }


}

