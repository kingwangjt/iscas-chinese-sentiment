package cn.ac.iscas.sentiment.featureselect;

public class IGItem {
	public int ID;
	public int p = 0;
	public int n = 0;
	public double ig = 0;
	public String getString(){
		String result = "";
		result += ID + " ";
		result += " ig: " + ig;
		return result;
	}
	
	public void setInformationGain(int pcount, int ncount){
		int sum = pcount + ncount;
		double entropyS = getEntropy(pcount, sum);
		double entropyP = getEntropy(p, pcount);
		double entropyN = getEntropy(n, ncount);
		double pProbility = (double)pcount / sum;
		double nProbility = (double)(sum - pcount) / sum;
		ig = entropyS - pProbility * entropyP - nProbility * entropyN;
	}
	
	private double getEntropy(int pcount, int sum){
		if (sum == 0) return 0;
		double pProbility = (double)pcount / sum;
		double nProbility = (double)(sum - pcount) / sum;
		double entropy = -pProbility * Math.log(pProbility) / Math.log(2)
		- nProbility * Math.log(nProbility) / Math.log(2);
		return entropy;
	}
}
