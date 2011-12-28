package cn.ac.iscas.crossvalidation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import cn.ac.iscas.classify.nb.NBModel;
import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.io.SmartFile;
import cn.ac.iscas.sentiment.featureselect.IGFilter;

public class AutoTestNonComment {
	public static String testDir = "test\\";
	public static String resultDir = "result\\";
	public static void main(String[] args) throws Exception{
		FivefoldCrossValidation ffcv = new FivefoldCrossValidation();
		ffcv.getDataSet("data\\svm_tf.txt");
		ffcv.dataSetShuffle();
		System.out.print("....");
		ffcv.generateDataSets("data\\svm_tf.txt","test\\", 5);
		System.out.print("....");
		ffcv.dataSetShuffle();
		System.out.print("....");
		System.out.println("File Generation Done!");
		File resultOutput = new File(resultDir + "nonCommentResult.txt");
		PrintStream out = new PrintStream(new FileOutputStream(resultOutput)); 
		System.setOut(out);
		double maxAcuracy = 0;
		double maxi = 0, maxj = 0;
		for (int i = 1; i <= 10; i++){
			for (int j = 1; j <= 10; j++){
				double avgAcuracy = test((double)i / 10, (double)j / 10);
				System.out.printf("Accuracy(%f, %f)==%f<<<\n" , (double)i / 10, (double)j / 10, avgAcuracy);
				if (avgAcuracy > maxAcuracy){
					maxAcuracy = avgAcuracy;
					maxi = (double)i / 10;
					maxj = (double)j / 10;
				}
				out.flush();
			}
		}
		System.out.println("<<<<<<<<<<<<");
		System.out.printf("Max Accuracy(%f, %f)==%f<<<\n" , maxi, maxj, maxAcuracy);
//		out.close();
	}
	
	public static double test(double modelThreshold, double filterThreshold){
		try{
			System.out.println("=======================================");
			System.out.println("=====modelThreshold:" + modelThreshold + " filterThreshold:"
					+ filterThreshold + "=============");
			File dir = new File(testDir);
			File[] testFiles = dir.listFiles();
			double avgAccuray = 0;
			for (int i = 0; i < 1; i++){
				String[] temp = ((String)testFiles[i].getName()).split("\\.");
				if (temp[1].equals("testing")){
					int index = Integer.parseInt(temp[2]);
					int p = i + 1;
					while (true){
						String[] tempS = testFiles[p].getName().split("\\.");
						if (Integer.parseInt(tempS[2]) == index) break;
						p++;
					}
					SmartData trainData = new SmartData(testFiles[p].getAbsolutePath());
					IGFilter ig = new IGFilter(trainData, modelThreshold);
					SmartData testData = new SmartData(testFiles[i].getAbsolutePath(), true);
					NBModel nbm = new NBModel(trainData, modelThreshold);
					nbm.train();
//					nbm.printModel();
					avgAccuray += nbm.test(testData);
				}
				System.out.flush();
			}
			System.out.println("=======================================");
			System.out.flush();
			return avgAccuray / 5;
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
}
