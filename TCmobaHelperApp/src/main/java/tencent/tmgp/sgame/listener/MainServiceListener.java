package tencent.tmgp.sgame.listener;

import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.a4455jkjh.colorpicker.ColorPickerDialog;
import com.a4455jkjh.colorpicker.view.OnColorChangedListener;
import com.tencent.tmgp.sgame.R;
import tencent.tmgp.sgame.activity.MainActivity;
import tencent.tmgp.sgame.activity.Zactivity;
import tencent.tmgp.sgame.other.BaseModel;
import tencent.tmgp.sgame.other.DashCircle;
import tencent.tmgp.sgame.other.JsonUtils;
import tencent.tmgp.sgame.other.ViewUtils;
import tencent.tmgp.sgame.service.MainService;

public class MainServiceListener implements OnClickListener,
OnItemSelectedListener, OnCheckedChangeListener, OnTouchListener
{
 MainService ms;
 ColorPickerDialog dialog_color_picker;
 DashCircle dc;
 int tblr = 0;
 
 public MainServiceListener(MainService m)
 {
  ms = m;
 }

 @Override
 public void onClick(View p1)
 {
  switch(p1.getId())
  {
   case R.id.linearlayout_color:
    if(dialog_color_picker == null)
     dialog_color_picker = new ColorPickerDialog(ms)
      .setTitle("选择颜色")
      .setOnColorChangedListener(new OnColorChangedListener()
      {
       @Override
       public void beforeColorChanged()
       {
        // TODO: Implement this method
       }

       @Override
       public void onColorChanged(int color)
       {
        ms.tst_color.setInitColor(color);
        dc.setColor(color);
        ms.dashcircleview.invalidate();
       }

       @Override
       public void afterColorChanged()
       {
        // TODO: Implement this method
       }
      })
      .create();
    dialog_color_picker.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    dialog_color_picker.show(ms.tst_color.getColor());
    break;
   case R.id.btn_sub1:
    addValue(-1);
    break;
   case R.id.btn_sub10:
    addValue(-10);
    break;
   case R.id.btn_sub100:
    addValue(-100);
    break;
   case R.id.btn_add1:
    addValue(1);
    break;
   case R.id.btn_add10:
    addValue(10);
    break;
   case R.id.btn_add100:
    addValue(100);
    break;
  }
 }

 @Override
 public void onCheckedChanged(CompoundButton p1, boolean p2)
 {
  switch(p1.getId())
  {
   case R.id.switch_bgc:
    if(p2)
    {
     ms.switch_bgc.setText("亮背景");
     ms.mFloatLayout2.setBackgroundColor(Color.argb(200,255,255,255));
     ms.switch_oval.setTextColor(Color.BLACK);
     ms.switch_bgc.setTextColor(Color.BLACK);
     ms.btn_close.setTextColor(Color.BLACK);
     ms.btn_reset.setTextColor(Color.BLACK);
     ms.btn_save.setTextColor(Color.BLACK);
     ms.btn_sub1.setTextColor(Color.BLACK);
     ms.btn_sub10.setTextColor(Color.BLACK);
     ms.btn_sub100.setTextColor(Color.BLACK);
     ms.btn_add1.setTextColor(Color.BLACK);
     ms.btn_add10.setTextColor(Color.BLACK);
     ms.btn_add100.setTextColor(Color.BLACK);
     ms.txt_value.setTextColor(Color.BLACK);
     ms.txt_0.setTextColor(Color.BLACK);
    }else
    {
     ms.switch_bgc.setText("暗背景");
     ms.mFloatLayout2.setBackgroundColor(Color.argb(155,0,0,0));
     ms.switch_oval.setTextColor(Color.WHITE);
     ms.switch_bgc.setTextColor(Color.WHITE);
     ms.btn_close.setTextColor(Color.WHITE);
     ms.btn_reset.setTextColor(Color.WHITE);
     ms.btn_save.setTextColor(Color.WHITE);
     ms.btn_sub1.setTextColor(Color.WHITE);
     ms.btn_sub10.setTextColor(Color.WHITE);
     ms.btn_sub100.setTextColor(Color.WHITE);
     ms.btn_add1.setTextColor(Color.WHITE);
     ms.btn_add10.setTextColor(Color.WHITE);
     ms.btn_add100.setTextColor(Color.WHITE);
     ms.txt_value.setTextColor(Color.WHITE);
     ms.txt_0.setTextColor(Color.WHITE);
    }
    break;
   case R.id.switch_oval:
    if(p2)
     ms.switch_oval.setText("椭圆");
    else
     ms.switch_oval.setText("矩形");
    ms.dashcircleview.setDrawOval(p2);
    ms.dashcircleview.invalidate();
    break;
  }
 }

 @Override
 public void onNothingSelected(AdapterView<?> p1)
 {
  // TODO: Implement this method
 }

 @Override
 public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
 {
  switch(p1.getId())
  {
   case R.id.spinner_hero:
    ms.selectedIndex = p3;
    ms.currentModel = new BaseModel(MainActivity.models.get(p3));
    ms.dashcircleview.setBaseModel(ms.currentModel);
    ms.dashcircleview.invalidate();
    ms.updateCircleAdapter();
    return;
    //break;
    
   case R.id.spinner_circle:
    dc = ms.currentModel.circles.get(p3);
    ms.tst_color.setInitColor(dc.color);
    break;
    
   case R.id.spinner_tblr:
    tblr = p3;
    break;
  }
  setText();
 }
 
 private void addValue(int v)
 {
  switch(tblr)
  {
   case 0:
    dc.mRectF.top += v;
    break;
   case 1:
    dc.mRectF.bottom += v;
    break;
   case 2:
    dc.mRectF.left += v;
    break;
   case 3:
    dc.mRectF.right += v;
    break;
   case 4:
    dc.setSolid(dc.solidLineLength + v);
    break;
   case 5:
    dc.setSpace(dc.spaceLength + v);
    break;
   case 6:
    dc.setStrokeWidth(dc.strokeWidth + v);
    break;
  }
  ms.dashcircleview.invalidate();
  setText();
 }
 
 private void setText()
 {
  float value = 0;
  switch(tblr)
  {
   case 0:
    value = dc.mRectF.top;
    break;
   case 1:
    value = dc.mRectF.bottom;
    break;
   case 2:
    value = dc.mRectF.left;
    break;
   case 3:
    value = dc.mRectF.right;
    break;
   case 4:
    value = dc.solidLineLength;
    break;
   case 5:
    value = dc.spaceLength;
    break;
   case 6:
    value = dc.strokeWidth;
    break;
  }
  ms.txt_value.setText("" + (int)value);
 }
 
 Point pBefore = new Point(), pNow = new Point(), pFirst = new Point();
 
 @Override
 public boolean onTouch(View p1, MotionEvent p2)
 {
  switch(p2.getAction())
  {
   case MotionEvent.ACTION_DOWN:
    pBefore.set((int)p2.getRawX(), (int)p2.getRawY());
    pFirst.set(pBefore.x, pBefore.y);
    break;
   case MotionEvent.ACTION_MOVE:
    pNow.set((int)p2.getRawX(),(int)p2.getRawY());
    //getRawX是触摸位置相对于屏幕的坐标
    ms.wmParams2.x += pNow.x - pBefore.x;
    if(ms.wmParams2.x < 0 ||
       ms.wmParams2.x > ViewUtils.getScreenHeight(ms) - ms.mFloatLayout2.getMeasuredWidth())
     ms.wmParams2.x -= pNow.x - pBefore.x;
    //刷新
    ms.mWindowManager.updateViewLayout(ms.mFloatLayout2, ms.wmParams2);
    pBefore.set(pNow.x,pNow.y);
    break;
   case MotionEvent.ACTION_UP:
    //移动距离小于某值，则为点击事件或长按事件
    if(Math.abs(p2.getRawX() - pFirst.x) < 5 &&
       Math.abs(p2.getRawY() - pFirst.y) < 5)
    {
     switch(p1.getId())
     {
      case R.id.btn_close:
       ms.removeEditView();
       break;
      case R.id.btn_save:
       MainActivity.models.set(ms.selectedIndex, ms.currentModel);
       if(ms.sp.edit().putString(Zactivity.MAIN_MODELS,
                               JsonUtils.toJsonArray(MainActivity.models)).commit())
        ms.toast("已保存");
       break;
      case R.id.btn_reset:
       ms.currentModel = new BaseModel(MainActivity.models.get(ms.selectedIndex));
       onItemSelected(ms.spinner_circle, null, ms.spinner_circle.getSelectedItemPosition(), 0);
       ms.dashcircleview.setBaseModel(ms.currentModel);
       ms.dashcircleview.invalidate();
       break;
     }
    }
    break;
  }
  return true;
 }
 
}
