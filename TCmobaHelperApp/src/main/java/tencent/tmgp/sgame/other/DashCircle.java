package tencent.tmgp.sgame.other;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

public class DashCircle
{ 
 public int color; // 颜色
 
 public float solidLineLength; // 实线长度
 
 public float spaceLength; // 间隔长度
 
 public float strokeWidth; // 宽带、粗细
 
 public RectF mRectF = null;
 
 public Paint mPaint = null;
  
 public DashCircle()
 {
  mRectF = new RectF();
  mPaint = new Paint();
  mPaint.setStyle(Paint.Style.STROKE);
  mPaint.setAntiAlias(true);
 }
 
 public DashCircle(DashCircle dc)
 {
  mRectF = new RectF();
  mPaint = new Paint();
  mPaint.setStyle(Paint.Style.STROKE);
  mPaint.setAntiAlias(true);
  
  setColor(dc.color);
  setStrokeWidth(dc.strokeWidth);
  setDashStyle(dc.solidLineLength,dc.spaceLength);
  setDistance(dc.mRectF.left,dc.mRectF.top,dc.mRectF.bottom);
 }
 
 public void defInit()
 {
  setColor(Color.CYAN);
  setDashStyle(30, 20);
  setDistance(20, 20, 1000);
  setStrokeWidth(10);
 }
 
 // 设置轴长
 public void setDistance(float l, float t, float b)
 {
  mRectF.top = t;
  mRectF.bottom = b;
  mRectF.left = l;
 }
 
 // 设置颜色
 public void setColor(int c)
 {
  color = c;
  mPaint.setColor(color);
 }
 
 // 设置虚线的实线长度和间隔长度
 public void setDashStyle(float solid, float space)
 {
  solidLineLength = solid;
  spaceLength = space;
  mPaint.setPathEffect(new DashPathEffect(new float[]{solidLineLength, spaceLength}, 0));
 }
 
 // 设置宽度、粗细
 public void setStrokeWidth(float stroke)
 {
  strokeWidth = stroke;
  mPaint.setStrokeWidth(strokeWidth);
 }
 
}
