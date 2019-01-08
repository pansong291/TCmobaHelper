package tencent.tmgp.sgame.service;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import com.tencent.tmgp.sgame.R;
import pansong291.DashCircleView;
import tencent.tmgp.sgame.activity.MainActivity;
import tencent.tmgp.sgame.notification.MyNotification;

public class MainService extends Zservice 
{

 //定义浮动窗口布局
 private LinearLayout mFloatLayout;
 private LayoutParams wmParams;
 //创建浮动窗口设置布局参数的对象
 private WindowManager mWindowManager;

 DashCircleView dashcircleview;
 
 private static final String TAG="MainService";

 @Override
 public void onCreate() 
 {
  // TODO Auto-generated method stub
  super.onCreate();
  createFloatView();
 }

 @Override
 public int onStartCommand(Intent intent, int flags, int startId)
 {
  MyNotification.startNotification(this);
  if(dashcircleview != null)
  {
   dashcircleview.setBaseModel(MainActivity.selectedModel);
   dashcircleview.invalidate();
  }

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
  mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
  wmParams = new LayoutParams();
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
  mFloatLayout = (LinearLayout)inflater.inflate(R.layout.flowin_main, null);

  dashcircleview = mFloatLayout.findViewById(R.id.dashcircleview);
  //添加mFloatLayout
  mWindowManager.addView(mFloatLayout,wmParams);
  
  mFloatLayout.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

 }
 
 @Override
 public void onDestroy()
 {
  // TODO Auto-generated method stub
  super.onDestroy();
  MyNotification.stopNotification(this, true);
  if(mFloatLayout != null)
  {
   //移除悬浮窗口
   mWindowManager.removeView(mFloatLayout);
  }
 }


}

