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
//	public ArrayList<String> dataset = new ArrayList<String>();
	public int size = 0;
	ArrayList<Integer> num = new ArrayList<Integer>();
	int[] mark;
	
	public void getDataSet(String path){
		try{
			BufferedReader f = new BufferedReader(new FileReader(path));
	        // input file name goes above
					    
			String line = f.readLine();
			while (line != null){
				size++;
				line = f.readLine();
			}
			f.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void dataSetShuffle(){
//		int size = dataset.size();
		 
		for(int i = 0; i < size; i++){
			num.add(i+1);
		}
		Random seed = new Random();
		Collections.shuffle(num,seed);
	}
	
	public void generateDataSets(String originalPath, String outputPath, int k_fold){
		int begin = 0;
		int last = 0;
		mark = new int[size];
		
		//k iterations
		int iteration = 1;
		SmartFile sf_write_test = null;
		SmartFile sf_write_train;
		String line;
		String testingfilename = "5-fold Cross Validation.testing.";
		String trainningfilename = "5-fold Cross Validation.trainning.";
		int increment;
		while(iteration <= k_fold){
			begin = last;
			for(int i = 0; i < size; i++){
				mark[i] = 0;                          //mark = 0 means training data
			}
			if(iteration != 5)
				increment = size/5;
			else
				increment = size - begin;
			while(last < begin + increment){
//				System.out.println("****"+(num.get(last)-1)+"****");
				mark[num.get(last)-1] = 1;            //mark = 1 means testing data
				last++;
			}
//			System.out.println("---------------------");
			/*for(int k=0; (k <= begin || k >= last) && k < size; k++){
				if(k == begin){ 
					k = k + size/5-1;
					continue;
				}
				mark[num.get(k)-1] = 0;               //mark = 0 means training data
				System.out.println(num.get(k));
//				System.out.println(dataset.get(num.get(k)-1).toString());
			}*/
//			System.out.println("---------------------");
//			System.out.println("---------------------");
			SmartFile sf_read = new SmartFile(originalPath, true);
			sf_write_train = new SmartFile(outputPath + trainningfilename + iteration + ".txt", false);
			sf_write_test = new SmartFile(outputPath + testingfilename + iteration + ".txt", false);
/*			for(int i=0;i<size;i++)
				System.out.println(mark[i]);*/
			line = sf_read.readLine();
			for(int k=0; k < size & line != null; k++){
				if(mark[k] == 0){
//					System.out.println(line);
					sf_write_train.writeLine(line);
				}
				else if(mark[k] == 1){
//					System.out.println(line);
					sf_write_test.writeLine(line);
				}
				line = sf_read.readLine();
			}
			sf_read.close();
			sf_write_train.close();
			sf_write_test.close();
//			System.out.println("---------------------");
//			System.out.println("---------------------");
			iteration++;
			/*	System.out.println(num.get(k));
				System.out.println(dataset.get(num.get(k)-1).toString());*/
			}
			/*if(last == size)
				sf_write_test.close();
			sf_write_train.close();*/
		}	
	}

