package cn.ac.iscas.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
//import cn.ac.iscas.crossvalidation.FivefoldCrossValidation;

import cn.ac.iscas.crossvalidation.FivefoldCrossValidation;

public class FivefoldCrossValidationTest {
	public static void main(String[] args){
		ArrayList<Integer> num = new ArrayList<Integer>(3);
		/*for(int i=0; i<100; i++){
			num.add(i+1);
		}
		for(int i=0; i<100; i++){
			System.out.println(num.get(i));
		}
		System.out.println("----------------");
		Random seed = new Random();
		Collections.shuffle(num,seed);
		for(int i=0; i<100; i++){
			System.out.println(num.get(i));
		}*/
		FivefoldCrossValidation ffcv = new FivefoldCrossValidation();
		ffcv.getDataSet("data\\svm_tf.txt");
		ffcv.dataSetShuffle();
/*		for(int i=0; i<ffcv.size; i++){
			System.out.println(ffcv.num.get(i));
		}*/
//		System.out.println(ffcv.size);
		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 3);		

	}

}
