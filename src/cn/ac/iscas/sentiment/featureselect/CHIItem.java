package cn.ac.iscas.sentiment.featureselect;

public class CHIItem {
	public int ID;
	double [][]n;
	public double chi = 0;
	
	public CHIItem()
	{
		n = new double[2][2];
	}
	
	public String getString(){
		String result = "";
		result += ID + " ";
		result += " chi: " + chi;
		return result;
	}
	
	public  void setCHI(int pcount, int ncount){
		this.chi = 0;
		
		double sum = 0;
		
		sum = pcount + ncount;
		n[1][0] = pcount - this.n[1][1];
		n[0][0] = ncount - this.n[0][1];
		
		chi = sum * Math.pow((n[1][1] * n[0][0] - n[1][0] * n[0][1]), 2);
		for (int i = 0; i < 2; i++){
			chi /= (n[i][i] + n[i][1-i]);
			chi /= (n[i][i] + n[1-i][i]);
		}
	}
}
