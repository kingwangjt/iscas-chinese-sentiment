package cn.ac.iscas.sentiment.featureselect;

import java.awt.Point;
import java.util.Comparator;

public class SortByDF implements Comparator{
	public int compare(Object obj1,Object obj2){
		Point p1=(Point)obj1;
		Point p2=(Point)obj2;
		  if(p1.y > p2.y)
		   return 0;
		  else
		   return 1;
		 }
}
