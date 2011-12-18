package cn.ac.iscas.features;


public class TermFrequenceFeature {
	
	public double[][] termfrequenceMatrix;
	
	public TermFrequenceFeature(int docNum, int termNum)
	{
		termfrequenceMatrix = new double[docNum][termNum];
	}
	
	public double[][] getTFMatrix()
	{
		return termfrequenceMatrix;
	}
}
