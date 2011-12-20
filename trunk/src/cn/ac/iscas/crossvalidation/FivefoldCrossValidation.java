package cn.ac.iscas.crossvalidation;

import cn.ac.iscas.io.SmartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;



/**
 * @author xukexin
 *
 */

public class FivefoldCrossValidation {
	public ArrayList<double[]> dataset = new ArrayList<double []>();
	ArrayList<Integer> num = new ArrayList<Integer>();
	
	public void getDataSet(String path){
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
	
	public void dataSetShuffle(){
		int size = dataset.size();
		 
		for(int i = 0; i < size; i++){
			num.add(i+1);
		}
		Random seed = new Random();
		Collections.shuffle(num,seed);
	}
	
	public void generateDataSets(String path){
		int begin = 0;
		int last = 0;
		
		//five iterations
		int iteration = 1;
		SmartFile sf;
		String testingfilename = "5-fold Cross Validation.testing.";
		String trainningfilename = "5-fold Cross Validation.trainning.";
		/*for(int i = 0; i < dataset.size(); i++){
			System.out.println(dataset.get(i).toString());
		}*/
		while(iteration <= 5){
			begin = last;
			sf = new SmartFile(path + testingfilename + iteration + ".txt", false);
			while(last < begin + dataset.size()/5){
				sf.writeLine(dataset.get(num.get(last)-1).toString());
		/*		System.out.println(num.get(last));
				System.out.println(dataset.get(num.get(last)-1).toString());*/
				last++;
			}
			sf.close();
//			System.out.println("---------------------");
			sf = new SmartFile(path + trainningfilename + iteration + ".txt", false);
			for(int k=0; (k <= begin || k >= last) && k < dataset.size(); k++){
				if(k == begin){ 
					k = k + dataset.size()/5-1;
					continue;
				}
				sf.writeLine(dataset.get(num.get(k)-1).toString());	
			/*	System.out.println(num.get(k));
				System.out.println(dataset.get(num.get(k)-1).toString());*/
			}
			sf.close();
			/*System.out.println("---------------------");
			System.out.println("---------------------");*/
			iteration++;
		}	
	}
	
	
}
