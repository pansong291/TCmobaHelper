package tencent.tmgp.sgame.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import tencent.tmgp.sgame.activity.Zactivity;
import android.os.Environment;
import android.util.Log;
import java.io.FileReader;

public class FileUtils
{
 private static String backupPath = null;
 
 public static boolean exportBackup(Zactivity z)
 {
  String s = JsonUtils.sp2Json(z.sp);
  return write2File(s, getBackupFilePath(z));
 }
 
 public static boolean importBackup(Zactivity z)
 {
  String s = getBackupFilePath(z);
  s = readFromFile(s);
  if(s == null || s.isEmpty())
   return false;
  return JsonUtils.json2SP(s, z.sp);
 }
 
 public static String getBackupFilePath(Zactivity c)
 {
  if(backupPath == null)
  {
   backupPath = Environment.getExternalStorageDirectory().getAbsolutePath();
   backupPath += "/Android/com.tencent.tmgp.sgame_preferences.json";
  }
  return backupPath;
 }
 
 public static String readFromFile(String file)
 {
  String str = null;
  File f = new File(file);
  if(!f.exists() || f.isDirectory())
  {
   Log.e(Zactivity.TAG_PACKAGE, "File "+file+" doesn't exist or it is a directory.");
   return str;
  }
  FileReader fr = null;
  StringBuffer sb = new StringBuffer();
  try
  {
   fr = new FileReader(f);
   char[] chars = new char[1024];
   int len, i;
   while((len = fr.read(chars)) != -1)
   {
    for(i = 0;i < len;i++)
    {
     sb.append(chars[i]);
    }
   }
   str = sb.toString();
  }catch(Exception e)
  {
   Log.e(Zactivity.TAG_PACKAGE, "There are errors occured.");
   e.printStackTrace();
  }
  try
  {
   if(fr != null)fr.close();
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return str;
 }
 
 public static boolean write2File(String content, String file)
 {
  boolean success = false;
  File f = new File(file);
  File pare = f.getParentFile();
  if(!(pare.exists() || pare.mkdirs()))
  {
   Log.e(Zactivity.TAG_PACKAGE, "Directory "+pare.getAbsolutePath()+" doesn't exist or it can not be create.");
   return success;
  }
//  if(f.exists() && !f.delete())
//  {
//   Log.e(Zactivity.TAG_PACKAGE, "File "+file+" is exist and it can not be delete.");
//   return success;
//  }
  FileOutputStream fos = null;
  try
  {
   fos = new FileOutputStream(f);
   fos.write(content.getBytes());
   fos.flush();
   success = true;
  }catch(Exception e)
  {
   Log.e(Zactivity.TAG_PACKAGE, "There are errors occured.");
   e.printStackTrace();
  }
  try
  {
   if(fos != null)fos.close();
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return success;
 }
 
 public static boolean copyFile(String s1, String s2)
 {
  boolean success = false;
  File f1 = new File(s1);
  if(!f1.exists() || f1.isDirectory())
  {
   Log.e(Zactivity.TAG_PACKAGE, "File "+s1+" doesn't exist or it is a directory.");
   return success;
  }
  File f2 = new File(s2);
  File pare = f2.getParentFile();
  if(!(pare.exists() || pare.mkdirs()))
  {
   Log.e(Zactivity.TAG_PACKAGE, "Directory "+pare.getAbsolutePath()+" doesn't exist or it can not be create.");
   return success;
  }
  if(f2.exists() && !f2.delete())
  {
   Log.e(Zactivity.TAG_PACKAGE, "File "+s2+" is exist and it can not be delete.");
   return success;
  }
  FileInputStream fis = null;
  FileOutputStream fos = null;
  try
  {
   fis = new FileInputStream(f1);
   fos = new FileOutputStream(f2);
   byte[] bytes = new byte[1024];
   int len;
   while((len = fis.read(bytes)) != -1)
   {
    fos.write(bytes, 0, len);
   }
   fos.flush();
   success = true;
  }catch(Exception e)
  {
   Log.e(Zactivity.TAG_PACKAGE, "There are errors occured.");
   e.printStackTrace();
  }
  try
  {
   if(fis != null)fis.close();
   if(fos != null)fos.close();
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return success;
 }
 
}
