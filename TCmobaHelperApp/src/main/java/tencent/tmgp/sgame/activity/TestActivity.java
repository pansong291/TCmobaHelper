package tencent.tmgp.sgame.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.a4455jkjh.colorpicker.view.TestView;
import com.tencent.tmgp.sgame.R;
import pansong291.DashCircleView;
import tencent.tmgp.sgame.listener.TestViewListener;
import tencent.tmgp.sgame.other.DashCircle;
import tencent.tmgp.sgame.other.JsonUtils;
import tencent.tmgp.sgame.other.ViewUtils;

public class TestActivity extends Zactivity
{
 public static final int TEST_REQUEST_CODE = 674;
  
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  // TODO: Implement this method
  super.onCreate(savedInstanceState);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏 
  //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
  setContentView(R.layout.activity_test);
  initView();
  initEvent();
  initValue();
 }
 
 TestViewListener listener;
 ImageView imageview;
 Bitmap mbitmap;
 public DashCircleView dashcircleview;
 public LinearLayout linearlayout_edit;
 public Switch switch_flopos, switch_oval, switch_bgc;
 Spinner spinner_tblr;
 public TestView tst_color;
 public EditText edt_solid, edt_space, edt_stroke, edt_value;
 public TextView txt_value, txt_selpic, txt[];
 
 String color;
 public DashCircle test_dc;
 
 private void initView()
 {
  linearlayout_edit = findViewById(R.id.linearlayout_edit);
  imageview = findViewById(R.id.imageview_test);
  dashcircleview = findViewById(R.id.dashcircleview);
  txt_selpic = findViewById(R.id.txt_selpic);
  txt_value = findViewById(R.id.txt_value);
  txt = new TextView[6];
  for(int i = 0;i < txt.length;i++)
  {
   txt[i] = findViewById(getResources().getIdentifier("txt_"+i,"id",getPackageName()));
  }
  switch_flopos = findViewById(R.id.switch_flopos);
  switch_bgc = findViewById(R.id.switch_bgc);
  switch_oval = findViewById(R.id.switch_oval);
  spinner_tblr = findViewById(R.id.spinner_tblr);
  tst_color = findViewById(R.id.tst_color);
  edt_solid = findViewById(R.id.edt_solid);
  edt_space = findViewById(R.id.edt_space);
  edt_stroke = findViewById(R.id.edt_stroke);
  edt_value = findViewById(R.id.edt_value);
 }
 
 private void initEvent()
 {
  listener = new TestViewListener(this);
  
  txt_selpic.setOnClickListener(listener);
  txt_value.setOnClickListener(listener);
  tst_color.setOnClickListener(listener);
  
  txt_value.setOnLongClickListener(listener);
  
  switch_flopos.setOnCheckedChangeListener(listener);
  switch_bgc.setOnCheckedChangeListener(listener);
  switch_oval.setOnCheckedChangeListener(listener);
  
  spinner_tblr.setOnItemSelectedListener(listener);
  
  edt_solid.setOnEditorActionListener(listener);
  edt_space.setOnEditorActionListener(listener);
  edt_stroke.setOnEditorActionListener(listener);
  edt_value.setOnEditorActionListener(listener);
 }
 
 private void initValue()
 {
  test_dc = JsonUtils.parseJsonObject(opString(Zactivity.TEST_CIRCLE, null));

  mbitmap = ViewUtils.getResBitmap(R.drawable.demo, this);
  imageview.setImageBitmap(mbitmap);
  
  dashcircleview.setTest(true);
  dashcircleview.setTestCircle(test_dc);
  color = ViewUtils.getHexColor(test_dc.color);

  txt_value.setText(
   "顶=" + test_dc.mRectF.top
   + "\n底=" + test_dc.mRectF.bottom
   + "\n左=" + test_dc.mRectF.left
   + "\n右=" + test_dc.mRectF.right);

  ArrayAdapter<String> adapter = new ArrayAdapter<String>(
   this,android.R.layout.simple_spinner_item,new String[]{"顶部","底部","左部","右部"});
  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  spinner_tblr.setAdapter(adapter);

  tst_color.setInitColor(test_dc.color);

  edt_solid.setText("" + (int)test_dc.solidLineLength);
  edt_space.setText("" + (int)test_dc.spaceLength);
  edt_stroke.setText("" + (int)test_dc.strokeWidth);
  edt_value.setText("" + (int)test_dc.mRectF.top);

  switch_flopos.setChecked(opBoolean(Zactivity.TEST_FLOPOS, false));
  switch_oval.setChecked(opBoolean(Zactivity.TEST_OVAL, false));
  switch_bgc.setChecked(opBoolean(Zactivity.TEST_BGC, true));
 }
 
 //重写onActivityResult以获得你需要的信息
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data)
 {
  super.onActivityResult(requestCode,resultCode,data);
  //此处的requestCode用于判断接收的Activity是不是你想要的那个
  if(resultCode == RESULT_OK && requestCode == TEST_REQUEST_CODE)
  {
   //把现在的Bmp内存释放
   if(mbitmap != null) mbitmap.recycle();
   //获得图片的uri
   Uri uri = data.getData();
   String Pcpath;
   try
   {
    String[]proj = {MediaStore.Images.Media.DATA};
    //好像是android多媒体数据库的封装接口，具体的看Android文档
    Cursor cursor = managedQuery(uri,proj,null,null,null);
    //按我个人理解 这个是获得用户选择的图片的索引值
    int column_index = cursor.getColumnIndexOrThrow(proj[0]);
    //将光标移至开头，这个很重要，不小心很容易引起越界
    cursor.moveToFirst();
    //最后根据索引值获取图片路径
    Pcpath = cursor.getString(column_index);
   }catch(Exception e)
   {
    Pcpath = uri.getPath();
   }
   //根据图片路径获取位图
   mbitmap = BitmapFactory.decodeFile(Pcpath);
   imageview.setImageBitmap(mbitmap);
   //imageview.setImageMatrix(ViewUtils.getImgFitCenterMatrix(mbitmap, imageview));
  }else if(resultCode == RESULT_OK)
  {
   toast("请重新选择图片");
  }
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  if(mbitmap != null) mbitmap.recycle();
  sp.edit()
   .putBoolean(Zactivity.TEST_FLOPOS, switch_flopos.isChecked())
   .putBoolean(Zactivity.TEST_BGC, switch_bgc.isChecked())
   .putBoolean(Zactivity.TEST_OVAL, switch_oval.isChecked())
   .putString(Zactivity.TEST_CIRCLE, JsonUtils.toJsonObject(test_dc))
   .commit();
 }
 
}
