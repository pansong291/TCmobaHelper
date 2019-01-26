package tencent.tmgp.sgame.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import tencent.tmgp.sgame.activity.MainActivity;
import tencent.tmgp.sgame.activity.ModelEditActivity;
import com.a4455jkjh.colorpicker.ColorPickerDialog;
import com.a4455jkjh.colorpicker.view.OnColorChangedListener;

public class ModelEditViewListener implements OnItemClickListener,
DialogInterface.OnClickListener, OnClickListener, OnItemLongClickListener
{ 
 ModelEditActivity mea;
 AlertDialog dialog_delete_circle;
 ColorPickerDialog dialog_color_picker;
 
 public ModelEditViewListener(ModelEditActivity m)
 {
  mea = m;
 }

 @Override
 public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
 {
  mea.setCircleData(p3);
 }
 
 @Override
 public boolean onItemLongClick(AdapterView<?> p1, View p2, final int p3, long p4)
 {
  if(dialog_delete_circle == null)
   dialog_delete_circle = new AlertDialog.Builder(mea)
    .setTitle("删除")
    .setMessage("")
    .setPositiveButton("确定删除", new DialogInterface.OnClickListener()
    {
     @Override
     public void onClick(DialogInterface p5, int p6)
     {
      mea.dataChanged = true;
      MainActivity.selectedModel.circles.remove(p3);
      mea.circleListAdapter.notifyDataSetChanged();
     }
    })
    .setNegativeButton("取消", null)
    .create();
  dialog_delete_circle.show();
  dialog_delete_circle.setMessage("确定删除曲线" + (p3 + 1) + "吗？");
  return true;
 }
 
 @Override
 public void onClick(DialogInterface p1, int p2)
 {
  mea.dataChanged = true;
  mea.currentCircle.setColor(mea.view_color.getColor());
  
  mea.currentCircle.mRectF.left = mea.edt_left.length() > 0 ?
   Integer.parseInt(mea.edt_left.getText().toString()) : 0;
  mea.currentCircle.mRectF.right = mea.edt_right.length() > 0 ?
   Integer.parseInt(mea.edt_right.getText().toString()) : 0;
  mea.currentCircle.mRectF.top = mea.edt_top.length() > 0 ?
   Integer.parseInt(mea.edt_top.getText().toString()) : 0;
  mea.currentCircle.mRectF.bottom = mea.edt_bottom.length() > 0 ?
   Integer.parseInt(mea.edt_bottom.getText().toString()) : 0;
  
  mea.currentCircle.setDashStyle(
   mea.edt_solid.length() > 0 ?
   Integer.parseInt(mea.edt_solid.getText().toString()) : 0,
   mea.edt_space.length() > 0 ?
   Integer.parseInt(mea.edt_space.getText().toString()) : 0);
  
  mea.currentCircle.setStrokeWidth(
   mea.edt_stroke.length() > 0 ?
   Integer.parseInt(mea.edt_stroke.getText().toString()) : 0);
  
  mea.circleListAdapter.notifyDataSetChanged();
 }
 
 @Override
 public void onClick(View p1)
 {
  if(dialog_color_picker == null)
   dialog_color_picker = new ColorPickerDialog(mea)
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
      mea.view_color.setInitColor(color);
     }

     @Override
     public void afterColorChanged()
     {
      // TODO: Implement this method
     }
    })
    .create();
  dialog_color_picker.show(mea.view_color.getColor());
 }
 
}
