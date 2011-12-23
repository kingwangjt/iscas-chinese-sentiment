package cn.ac.iscas.sentiment.featureselect;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;

public class IGFilter {
	public SmartData data;
	public static double threshold = 0.01;
	public ArrayList<IGItem> igcount = new ArrayList<IGItem>();
	public ArrayList<Integer> filters = new ArrayList<Integer>();
	
	public IGFilter(SmartData data, double threshold){
		IGFilter.threshold = threshold;
		System.out.print("Information Gain Filter Begin...(" + threshold + ")");
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
				addIGCount(tf.x, doc.sentiment);
			}	
		}
		for (int i = 0; i < igcount.size(); i++){
			igcount.get(i).setInformationGain(pcount, ncount);
		}
		sortByIG();
		generateFilterIndexs();
		System.out.println("Size: " + igcount.size());
		filter(this.data);
		System.out.print("Information Gain End...");
		System.out.println("Size: " + (igcount.size() - filters.size()));
	}
	
	
	@SuppressWarnings("unchecked")
	public void sortByIG()
	{
		Collections.sort(igcount, new SortByIG());
	}
	
	private void addIGCount(int id, int sentiment){
		for (int i = 0; i < igcount.size(); i++){
			IGItem igItem = igcount.get(i);
			if (igItem.ID == id){
				if (sentiment > 0)
					igItem.p++;
				else
					igItem.n++;
				return;
			}
		}
		IGItem newig = new IGItem();
		newig.ID = id;
		if (sentiment > 0)
			newig.p++;
		else
			newig.n++;
		igcount.add(newig);
	}
	
	void generateFilterIndexs(){
		for (int i = 0; i < igcount.size(); i++){
			IGItem igItem = igcount.get(i);
			if ((!filters.contains(igItem.ID)) && (i >= threshold * igcount.size())){
				filters.add(igItem.ID);
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
