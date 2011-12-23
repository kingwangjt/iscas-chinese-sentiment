package cn.ac.iscas.sentiment.featureselect;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;

public class DFFilter {
	public SmartData data;
	public static double threshold = 0.6;
	public ArrayList<Point> dfcount = new ArrayList<Point>();
	public ArrayList<Integer> filters = new ArrayList<Integer>();
	
	public DFFilter(SmartData data){
		System.out.print("DF Filter Begin...");
		this.data = data;
		for (int i = 0; i < data.doclist.size(); i++){
			Doc doc = data.doclist.get(i);
//			if (i % 100 == 0){
//				System.out.print(i + " datas processed...");
//				System.out.println("document term size: " + dfcount.size());
//			}
			for (int j = 0; j < doc.tfs.size(); j++){
				Point tf = doc.tfs.get(j);
				addDF(tf.x, tf.y);
			}	
		}
		sortByDF();
		generateFilterIndexs();
		System.out.println("Size: " + dfcount.size());
		filter(this.data);
		System.out.print("DF Filter End...");
		System.out.println("Size: " + (dfcount.size() - filters.size()));
	}
	
	@SuppressWarnings("unchecked")
	public void sortByDF()
	{
		Collections.sort(dfcount, new SortByDF());
	}
	
	private void addDF(int id, int freq){
		for (int i = 0; i < dfcount.size(); i++){

			Point df = dfcount.get(i);
			if (df.x == id){
				df.y += freq;
				return;
			}
		}
		Point newdf = new Point();
		newdf.x = id;
		newdf.y += freq;
		dfcount.add(newdf);
	}
	
	void generateFilterIndexs(){
		for (int i = 0; i < dfcount.size(); i++){
			Point df = dfcount.get(i);
			if ((!filters.contains(df.x)) && (i >= dfcount.size() * threshold)){
				filters.add(dfcount.get(i).x);
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
