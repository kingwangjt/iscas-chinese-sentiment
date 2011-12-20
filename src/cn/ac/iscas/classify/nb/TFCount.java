package cn.ac.iscas.classify.nb;

public class TFCount {
	public int ID;
	public int p = 0;
	public int n = 0;
	public double pProbability = 0;
	public double nProbability = 0;
	public String getString(){
		String result = "";
		result += ID + " ";
		result += " p: " + pProbability;
		result += " n: " + nProbability;
		return result;
	}
	
	public void setProbability(int pcount, int ncount, int tfcount){
		pProbability = (double)(p + 1) / (pcount + tfcount);
		nProbability = (double)(n + 1) / (ncount + tfcount);
	}
}
