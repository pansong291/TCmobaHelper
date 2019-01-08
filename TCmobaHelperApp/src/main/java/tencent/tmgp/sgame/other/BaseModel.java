package tencent.tmgp.sgame.other;

import java.util.List;
import java.util.ArrayList;

public class BaseModel
{
 public String name = null;
 
 public List<DashCircle> circles = null;
 
 public BaseModel(String n)
 {
  name = n;
  circles = new ArrayList<>();
 }
 
 public BaseModel(BaseModel bm)
 {
  name = new String(bm.name);
  circles = new ArrayList<>();
  //circles.addAll(bm.circles);
  for(DashCircle dc : bm.circles)
  {
   circles.add(new DashCircle(dc));
  }
 }
 
}
