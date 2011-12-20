package cn.ac.iscas.io;

import java.awt.Point;
import java.util.ArrayList;

public class Doc {
	public int ID;
	public int sentiment = 0;
	public ArrayList<Point> tfs = new ArrayList<Point>();
	
	public String getString(){
		String result = "";
		result += ID + " ";
		result += sentiment + " ";
		for (int j = 0; j < tfs.size(); j++){
			result += tfs.get(j).x + ":" + tfs.get(j).y + " ";
		}
		return result;
	}
}
