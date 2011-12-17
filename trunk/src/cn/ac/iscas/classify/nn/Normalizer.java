package cn.ac.iscas.classify.nn;
import java.util.ArrayList;


public class Normalizer {
	double[] min, max;
	double[] mean, standardD;
	public void normalizeByMinMax(ArrayList<double[]> dataset, boolean isTrainSet){
		int n = dataset.get(0).length;
		if (isTrainSet){
			max = new double[n-2];
			min = new double[n-2];
			for (int i = 2; i < n; i++){
				max[i-2] = dataset.get(0)[i];
				min[i-2] = dataset.get(0)[i];
			}
			
			for (int i = 1; i < dataset.size(); i++){
				for (int j = 2; j < n; j++){
					if (dataset.get(i)[j] > max[j-2])
						max[j-2] = dataset.get(i)[j];
					if (dataset.get(i)[j] < min[j-2])
						min[j-2] = dataset.get(i)[j];							
				}
			}
		}
		
		for (int i = 0; i < dataset.size(); i++){
			double[] tuple = dataset.get(i);
			for (int j = 2; j < n; j++){
				tuple[j] = (tuple[j] - min[j-2])/(max[j-2] - min[j-2]);
			}
			dataset.set(i, tuple);
		}
	}
	
	public void normalizeByZScore(ArrayList<double[]> dataset, boolean isTrainSet){
		int n = dataset.get(0).length;
		if (isTrainSet){
			mean = new double[n-2];
			standardD = new double[n-2];

			
			for (int i = 0; i < dataset.size(); i++){
				for (int j = 2; j < n; j++){
					mean[j-2] += dataset.get(i)[j];						
				}
			}
			
			for (int i = 2; i < n; i++){
				mean[i-2] /= dataset.size();
			}

			for (int i = 0; i < dataset.size(); i++){
				for (int j = 2; j < n; j++){
					standardD[j-2] += Math.pow(dataset.get(i)[j] - mean[j-2], 2);						
				}
			}
			
			for (int i = 2; i < n; i++){
				standardD[i-2] /= dataset.size();
				standardD[i-2] = Math.sqrt(standardD[i-2]);
			}
		}
		
		for (int i = 0; i < dataset.size(); i++){
			double[] tuple = dataset.get(i);
			for (int j = 2; j < n; j++){
				tuple[j] = (tuple[j] - mean[j-2]) / standardD[j-2];				
			}
		}
	}
	
	public void normalizeByLogistic(ArrayList<double[]> dataset, boolean isTrainSet){
		int n = dataset.get(0).length;
		for (int i = 0; i < dataset.size(); i++){
		//	System.out.println(i);
			double[] tuple = dataset.get(i);
			for (int j = 2; j < n; j++){
				double temp = 1 + Math.pow(Math.E, -tuple[j]);
				tuple[j] = (double)1 / temp;			
			}
		}
	}
}
