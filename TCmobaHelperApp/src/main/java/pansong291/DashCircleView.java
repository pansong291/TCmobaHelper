package pansong291;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import tencent.tmgp.sgame.other.BaseModel;
import tencent.tmgp.sgame.other.DashCircle;
import tencent.tmgp.sgame.other.ViewUtils;

public class DashCircleView extends View
{
 BaseModel mBaseModel = null;

 boolean test = false;
 boolean drawOval = false;

 DashCircle mDashCircle = null;

 public DashCircleView(Context context)
 {
  super(context);
 }

 public DashCircleView(Context context, AttributeSet attrs)
 {
  super(context, attrs);
 }

 public DashCircleView(Context context, AttributeSet attrs, int defStyleAttr)
 {
  super(context, attrs, defStyleAttr);
 }

 public DashCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
 {
  super(context, attrs, defStyleAttr, defStyleRes);
 }

 public void setBaseModel(BaseModel bm)
 {
  mBaseModel = bm;
  for(int i = 0;i < mBaseModel.circles.size();i++)
  {
   RectF rectf = mBaseModel.circles.get(i).mRectF;
   rectf.right = ViewUtils.getScreenHeight(getContext()) - rectf.left;
  }
 }

 @Override
 protected void onDraw(Canvas canvas)
 {
  super.onDraw(canvas);
  if(test && mDashCircle != null)
  {
   if(drawOval)
    canvas.drawOval(mDashCircle.mRectF,mDashCircle.mPaint);
   else
    canvas.drawRect(mDashCircle.mRectF,mDashCircle.mPaint);
  }else if(mBaseModel != null && mBaseModel.circles != null)
   for(int i = 0;i < mBaseModel.circles.size();i++)
   {
    canvas.drawOval(mBaseModel.circles.get(i).mRectF, mBaseModel.circles.get(i).mPaint);
   }
 }

 public void setTest(boolean b)
 {
  test = b;
 }

 public void setDrawOval(boolean b)
 {
  drawOval = b;
 }

 public void setTestCircle(DashCircle dc)
 {
  mDashCircle = dc;
  mDashCircle.mRectF.right = ViewUtils.getScreenHeight(getContext()) - mDashCircle.mRectF.left;
 }

 public DashCircle getTestCircle()
 {
  return mDashCircle;
 }

 public void setRectF(int i, float f)
 {
  switch(i)
  {
   case 0:
    mDashCircle.mRectF.left = f;
    mDashCircle.mRectF.right = ViewUtils.getScreenHeight(getContext()) - f;
    break;
   case 1:
    mDashCircle.mRectF.top = f;
    break;
   case 2:
    mDashCircle.mRectF.bottom = f;
    break;
  }
 }


}
