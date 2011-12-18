package cn.ac.iscas.features;

import java.util.ArrayList;
import java.util.List;

public class InvertedDocumentFrequencyFeature {

	public double[] idfs;
	
	public InvertedDocumentFrequencyFeature(int termNum)
	{
		idfs = new double[termNum];
	}
		
	public double getidfs(int id)
	{
		return idfs[id];
	}
}
