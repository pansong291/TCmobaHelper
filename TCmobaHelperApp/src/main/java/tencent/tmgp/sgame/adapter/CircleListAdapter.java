package tencent.tmgp.sgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.a4455jkjh.colorpicker.view.TestView;
import com.tencent.tmgp.sgame.R;
import java.util.ArrayList;
import java.util.List;
import tencent.tmgp.sgame.other.DashCircle;

public class CircleListAdapter extends BaseAdapter
{
 private Context mContext;
 private List<DashCircle> list;

 public CircleListAdapter(Context mContext)
 {
  this.mContext = mContext;
  list = new ArrayList<>();
 }

 public CircleListAdapter(Context mContext, List<DashCircle> list)
 {
  this.mContext = mContext;
  this.list = list;
 }

 /**
  * 当ListView数据发生变化时,调用此方法来更新ListView
  *
  * @param list
  */
 public void updateListView(List<DashCircle> list) {
  this.list = list;
  notifyDataSetChanged();
 }

 public int getCount() {
  return list.size();
 }

 public Object getItem(int position) {
  return list.get(position);
 }

 public long getItemId(int position) {
  return position;
 }

 public View getView(final int position, View view, ViewGroup arg2) {
  ViewHolder viewHolder = null;

  if (view == null)
  {
   viewHolder = new ViewHolder();
   view = LayoutInflater.from(mContext).inflate(R.layout.listitem_circle, null);
   viewHolder.txt_left = view.findViewById(R.id.txt_left);
   viewHolder.txt_right = view.findViewById(R.id.txt_right);
   viewHolder.txt_top = view.findViewById(R.id.txt_top);
   viewHolder.txt_bottom = view.findViewById(R.id.txt_bottom);
   viewHolder.txt_solid = view.findViewById(R.id.txt_solid);
   viewHolder.txt_space = view.findViewById(R.id.txt_space);
   viewHolder.txt_stroke = view.findViewById(R.id.txt_stroke);
   viewHolder.tst_color = view.findViewById(R.id.tst_color);
   view.setTag(viewHolder);
  }else
  {
   viewHolder = (ViewHolder) view.getTag();
  }
  
  DashCircle dc = list.get(position);
  viewHolder.tst_color.setInitColor(dc.color);
  viewHolder.txt_left.setText("左距：" + (int)dc.mRectF.left);
  viewHolder.txt_right.setText("右距：" + (int)dc.mRectF.right);
  viewHolder.txt_top.setText("顶距：" + (int)dc.mRectF.top);
  viewHolder.txt_bottom.setText("底距：" + (int)dc.mRectF.bottom);
  viewHolder.txt_solid.setText("实长：" + (int)dc.solidLineLength);
  viewHolder.txt_space.setText("隙长：" + (int)dc.spaceLength);
  viewHolder.txt_stroke.setText("厚度：" + (int)dc.strokeWidth);

  return view;
 }

 private final static class ViewHolder
 {
  TextView txt_left;
  TextView txt_right;
  TextView txt_top;
  TextView txt_bottom;
  TextView txt_solid;
  TextView txt_space;
  TextView txt_stroke;
  TestView tst_color;
 }
}
