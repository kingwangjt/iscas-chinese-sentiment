package cn.ac.iscas.sentiment.featureselect;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;

public class CHIFilter {
	public SmartData data;
	public static double threshold = 0.6;
	public ArrayList<CHIItem> chicount = new ArrayList<CHIItem>();
	public ArrayList<Integer> filters = new ArrayList<Integer>();
	
	public CHIFilter(SmartData data){
		System.out.print("CHI Filter Begin...");
		this.data = data;
		int pcount = 0;
		int ncount = 0;
		for (int i = 0; i < data.doclist.size(); i++){
			Doc doc = data.doclist.get(i);
//			if (i % 100 == 0){
//				System.out.print(i + " datas processed...");
//				System.out.println("document term size: " + dfcount.size());
//			}
			if (doc.sentiment > 0){
				pcount++;
			} else {
				ncount++;
			}
			for (int j = 0; j < doc.tfs.size(); j++){
				Point tf = doc.tfs.get(j);
				addCHICount(tf.x, doc.sentiment);
			}	
		}
		
		for (int i = 0; i < chicount.size(); i++){
			chicount.get(i).setCHI(pcount, ncount);
		}
		sortByChi();
		generateFilterIndexs();
		System.out.println("Size: " + chicount.size());
		filter(this.data);
		System.out.print("CHI  End...");
		System.out.println("Size: " + (chicount.size() - filters.size()));
	}
	
	private void addCHICount(int id, int sentiment){
		for (int i = 0; i < chicount.size(); i++){
			CHIItem chiItem = chicount.get(i);
			if (chiItem.ID == id){
				if (sentiment > 0)
					chiItem.n[1][1]++;
				else
					chiItem.n[0][1]++;
				return;
			} 
		}
		CHIItem newig = new CHIItem();
		newig.ID = id;
		if (sentiment > 0)
			newig.n[1][1]++;
		else
			newig.n[0][1]++;
		chicount.add(newig);
	}
	
	@SuppressWarnings("unchecked")
	void sortByChi()
	{
		Collections.sort(chicount, new SortByChi());
	}
	
	void generateFilterIndexs(){
		for (int i = 0; i < chicount.size(); i++){
			CHIItem chiItem = chicount.get(i);
			if ((!filters.contains(chiItem.ID)) && (i >= threshold * chicount.size())){
				filters.add(chiItem.ID);
			}
		}
	}
	
	public void filter(SmartData data){
		for (int i = 0; i < data.doclist.size(); i++){
			Doc doc = data.doclist.get(i);
//			if (i % 100 == 0){
//				System.out.println(i + " data processed..");
//			}
			int j = 0;
			while(j < doc.tfs.size()){
				Point tf = doc.tfs.get(j);
				if (filters.contains(tf.x)){
					doc.tfs.remove(j);
				} else {
					j++;
				}
			}	
		}		
	}
}
