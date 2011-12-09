package cn.as.iscas.sentiment.featureselect;

public class MI {
	public static double getMI(int[] tuple){
		double mi = 0;
		double [][] n = new double[2][2];
		n[0][0] = tuple[0];
		n[0][1] = tuple[1];
		n[1][0] = tuple[2];
		n[1][1] = tuple[3];
		double sum = 0;
		for (int i = 0; i < 4; i++) sum += tuple[i];

		for (int i = 0; i < 2; i++){
			for (int j = 0; j < 2; j++){
				double k1 = (double)n[i][j] / sum;
				double k2 = (double)(n[i][j] * sum);
				k2 /= (n[i][0] + n[i][1]);
				k2 /= (n[0][j] + n[1][j]);
				mi += k1 * Math.log(k2) / Math.log(2);
			}
		}
		return mi;
	}
}
