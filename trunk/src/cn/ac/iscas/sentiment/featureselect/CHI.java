package cn.ac.iscas.sentiment.featureselect;

public class CHI {
	public static double getCHI(int[] tuple){
		double chi = 0;
		double [][] n = new double[2][2];
		n[0][0] = tuple[0];
		n[0][1] = tuple[1];
		n[1][0] = tuple[2];
		n[1][1] = tuple[3];
		double sum = 0;
		for (int i = 0; i < 4; i++) sum += tuple[i];

		chi = sum * Math.pow((n[1][1] * n[0][0] - n[1][0] * n[0][1]), 2);
		for (int i = 0; i < 2; i++){
			chi /= (n[i][i] + n[i][1-i]);
			chi /= (n[i][i] + n[1-i][i]);
		}
		return chi;
	}
}
