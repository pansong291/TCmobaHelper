package tencent.tmgp.sgame.listener;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.a4455jkjh.colorpicker.ColorPickerDialog;
import com.a4455jkjh.colorpicker.view.OnColorChangedListener;
import com.tencent.tmgp.sgame.R;
import tencent.tmgp.sgame.activity.TestActivity;

public class TestViewListener implements OnClickListener, OnLongClickListener,
OnCheckedChangeListener, OnItemSelectedListener, OnEditorActionListener
{
 TestActivity test;
 View floatingView;
 ColorPickerDialog dialog_color_picker;
 
 int tblr = 0;
 
 public TestViewListener(TestActivity ta)
 {
  test = ta;
 }

 @Override
 public void onClick(View p1)
 {
  switch(p1.getId())
  {
   case R.id.txt_value:
    test.linearlayout_test.setVisibility(8 - test.linearlayout_test.getVisibility());
    break;
   case R.id.txt_selpic:
    Intent it = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    test.startActivityForResult(Intent.createChooser(it, "选择图片"), test.TEST_REQUEST_CODE);
    break;
   case R.id.test_color:
    if(dialog_color_picker == null)
     dialog_color_picker = new ColorPickerDialog(test)
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
        test.test_color.setInitColor(color);
        test.test_dc.setColor(color);
        test.dashcircleview.invalidate();
       }

       @Override
       public void afterColorChanged()
       {
        // TODO: Implement this method
       }
      })
      .create();
    dialog_color_picker.show(test.test_color.getColor());
    break;
  }
 }

 @Override
 public boolean onLongClick(View p1)
 {
  switch(p1.getId())
  {
   case R.id.txt_value:
    ClipboardManager cm = (ClipboardManager)test.getSystemService(test.CLIPBOARD_SERVICE);
    cm.setText(test.txt_value.getText().toString());
    test.toast("已复制");
    break;
  }
  return true;
 }

 @Override
 public void onCheckedChanged(CompoundButton p1, boolean p2)
 {
  if(floatingView == null)
   floatingView = (View)test.txt_value.getParent();
  switch(p1.getId())
  {
   case R.id.switch_flopos:
    LayoutParams lp = (LayoutParams)floatingView.getLayoutParams();
    if(p2)
    {
     lp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
     lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
     test.switch_flopos.setText("右侧");
    }else
    {
     lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
     lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
     test.switch_flopos.setText("左侧");
    }
    floatingView.setLayoutParams(lp);
    break;
   case R.id.switch_bgc:
    if(p2)
    {
     test.switch_bgc.setText("亮背景");
     floatingView.setBackgroundColor(Color.argb(200,255,255,255));
     test.switch_flopos.setTextColor(Color.BLACK);
     test.switch_oval.setTextColor(Color.BLACK);
     test.switch_bgc.setTextColor(Color.BLACK);
     test.txt_selpic.setTextColor(Color.BLACK);
     test.txt_value.setTextColor(Color.BLACK);
     test.edt_solid.setTextColor(Color.BLACK);
     test.edt_space.setTextColor(Color.BLACK);
     test.edt_stroke.setTextColor(Color.BLACK);
     test.edt_value.setTextColor(Color.BLACK);
     for(int i = 0;i < test.txt.length;i++)
     {
      test.txt[i].setTextColor(Color.BLACK);
     }
    }else
    {
     test.switch_bgc.setText("暗背景");
     floatingView.setBackgroundColor(Color.argb(155,0,0,0));
     test.switch_flopos.setTextColor(Color.WHITE);
     test.switch_oval.setTextColor(Color.WHITE);
     test.switch_bgc.setTextColor(Color.WHITE);
     test.txt_selpic.setTextColor(Color.WHITE);
     test.txt_value.setTextColor(Color.WHITE);
     test.edt_solid.setTextColor(Color.WHITE);
     test.edt_space.setTextColor(Color.WHITE);
     test.edt_stroke.setTextColor(Color.WHITE);
     test.edt_value.setTextColor(Color.WHITE);
     for(int i = 0;i < test.txt.length;i++)
     {
      test.txt[i].setTextColor(Color.WHITE);
     }
    }
    break;
   case R.id.switch_oval:
    if(p2)
     test.switch_oval.setText("椭圆");
    else
     test.switch_oval.setText("矩形");
    test.dashcircleview.setDrawOval(p2);
    test.dashcircleview.invalidate();
    break;
  }
 }

 @Override
 public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
 {
  tblr = p3;
  float f = 0;
  switch(tblr)
  {
   case 0:
    f = test.test_dc.mRectF.top;
    break;
   case 1:
    f = test.test_dc.mRectF.bottom;
    break;
   case 2:
    f = test.test_dc.mRectF.left;
    break;
   case 3:
    f = test.test_dc.mRectF.right;
    break;
  }
  test.edt_value.setText("" + (int)f);
 }

 @Override
 public void onNothingSelected(AdapterView<?> p1)
 {
  // TODO: Implement this method
 }
 
 @Override
 public boolean onEditorAction(TextView p1, int actionId, KeyEvent event)
 {
  if(actionId == EditorInfo.IME_ACTION_DONE ||
     (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
  {
   int i1, i2;
   switch(p1.getId())
   {

    case R.id.edt_solid:
    case R.id.edt_space:
     if(test.edt_solid.length() > 0 && test.edt_space.length() > 0)
     {
      i1 = Integer.parseInt(test.edt_solid.getText().toString());
      i2 = Integer.parseInt(test.edt_space.getText().toString());
      test.test_dc.setDashStyle(i1, i2);
     }
     break;
    case R.id.edt_stroke:
     if(test.edt_stroke.length() > 0)
     {
      i1 = Integer.parseInt(test.edt_stroke.getText().toString());
      test.test_dc.setStrokeWidth(i1);
     }
     break;

    case R.id.edt_value:
     if(test.edt_value.length() > 0)
     {
      i1 = Integer.parseInt(test.edt_value.getText().toString());
      test.dashcircleview.setRectF(tblr, i1);
      test.txt_value.setText(
       "顶=" + test.test_dc.mRectF.top
       + "\n底=" + test.test_dc.mRectF.bottom
       + "\n左=" + test.test_dc.mRectF.left
       + "\n右=" + test.test_dc.mRectF.right);
     }
     break;
   }
   test.dashcircleview.invalidate();
   //return true;
  }
  return false;
 }
 
 
}
