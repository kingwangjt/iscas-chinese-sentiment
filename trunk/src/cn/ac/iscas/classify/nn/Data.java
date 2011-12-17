package cn.ac.iscas.classify.nn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Data {
	public ArrayList<double[]> dataset = new ArrayList<double []>();
	public int attributeNum;
	public double[] max;
	public double[] min;
	
	public Data(String path){
		try{
			BufferedReader f = new BufferedReader(new FileReader(path));
	        // input file name goes above
					
			String line = f.readLine();
			StringTokenizer st;
			int max = 0;
			while (line != null){
				st = new StringTokenizer(line);	
				int n = st.countTokens();
				if (max < n) max = n;
				if (n < max){
					line = f.readLine();
					continue;
				}
				attributeNum = n;
				double[] tuple = new double[n];
				for (int i = 0; i < n; i++){
					tuple[i] = Double.parseDouble(st.nextToken());
				}
				dataset.add(tuple);
				line = f.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void normalize(){
		int n = dataset.get(0).length;
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
		
		for (int i = 0; i < dataset.size(); i++){
			double[] tuple = dataset.get(i);
			for (int j = 2; j < n; j++){
				tuple[j] = (tuple[j] - min[j-2])/(max[j-2] - min[j-2]);
			}
			dataset.set(i, tuple);
		}
	}
	
	public void normalize(double[] min, double[] max){
		int n = dataset.get(0).length;
		
		for (int i = 0; i < dataset.size(); i++){
			double[] tuple = dataset.get(i);
			for (int j = 2; j < n; j++){
				tuple[j] = (tuple[j] - min[j-2])/(max[j-2] - min[j-2]);
			}
			dataset.set(i, tuple);
		}
	}
	
	public double calMisRecord(Network network){
		int yes = 0;
		int no = 0;
		for (int i = 0; i < dataset.size(); i++){
			if (network.testTuple(dataset.get(i))){
				yes++;
			} else {
				no++;
			}
		}
		return (double)yes / (yes+no);
	}
}
