package cn.ac.iscas.sentiment.featureselect;

import java.util.Comparator;

public class SortByIG implements Comparator{
	public int compare(Object obj1,Object obj2){
		IGItem ig1=(IGItem)obj1;
		IGItem ig2=(IGItem)obj2;
		  if(ig1.ig > ig2.ig)
		   return 0;
		  else
		   return 1;
		 }
}
