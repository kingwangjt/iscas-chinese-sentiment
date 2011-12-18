package cn.ac.iscas.features;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TermFrequenceFeature {
	
	public Map<Integer, List<Point>> termfrequenceMatrix;
	
	public TermFrequenceFeature(int docNum, int termNum)
	{
		termfrequenceMatrix = new HashMap<Integer, List<Point>>();
	}
	
	public Map<Integer, List<Point>> getTFMatrix()
	{
		return termfrequenceMatrix;
	}
}
