package cn.ac.iscas.crossvalidation;

import java.io.File;

import cn.ac.iscas.classify.nb.NBModel;
import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.sentiment.featureselect.CHIFilter;
import cn.ac.iscas.sentiment.featureselect.DFFilter;
import cn.ac.iscas.sentiment.featureselect.IGFilter;

public class AutoTest {
	public static String testDir = "test\\";
	public static void main(String[] args){
		FivefoldCrossValidation ffcv = new FivefoldCrossValidation();
		ffcv.getDataSet("data\\svm_tf.txt");
		ffcv.dataSetShuffle();
		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 3);
		System.out.print("....");
		ffcv.dataSetShuffle();
		System.out.print("....");
		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 5);
		System.out.print("....");
		ffcv.dataSetShuffle();
		System.out.print("....");
		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 7);
		System.out.print("....");
//		ffcv.dataSetShuffle();
//		System.out.print("....");
//		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 10);
		System.out.print("....");
		System.out.println("File Generation Done!");
		try{
			File dir = new File(testDir);
			File[] testFiles = dir.listFiles();
			for (int i = 0; i < testFiles.length; i++){
				String[] temp = ((String)testFiles[i].getName()).split("\\.");
				if (temp[1].equals("testing")){
					int index = Integer.parseInt(temp[2]);
					int p = i + 1;
					while (true){
						String[] tempS = testFiles[p].getName().split("\\.");
						if (Integer.parseInt(tempS[2]) == index) break;
						p++;
					}
					System.out.println(testFiles[p].getName());
					System.out.println(testFiles[i].getName());
					SmartData trainData = new SmartData(testFiles[p].getAbsolutePath());
//					DFFilter df = new DFFilter(trainData);
//					IGFilter ig = new IGFilter(trainData);
//					CHIFilter chi = new CHIFilter(trainData);
					SmartData testData = new SmartData(testFiles[i].getAbsolutePath(), true);
					NBModel nbm = new NBModel(trainData);
					nbm.train();
//					nbm.printModel();
					nbm.test(testData);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
