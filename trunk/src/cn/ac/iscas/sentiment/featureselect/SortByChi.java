package cn.ac.iscas.sentiment.featureselect;

import java.util.Comparator;

public class SortByChi implements Comparator{
	public int compare(Object obj1,Object obj2){
		CHIItem chi1=(CHIItem)obj1;
		CHIItem chi2=(CHIItem)obj2;
		  if(chi1.chi > chi2.chi)
		   return 0;
		  else
		   return 1;
		 }
}
