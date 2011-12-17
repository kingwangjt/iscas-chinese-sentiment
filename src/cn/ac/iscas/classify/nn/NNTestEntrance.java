package cn.ac.iscas.classify.nn;
import java.util.Random;


public class NNTestEntrance {
	public static double learnRate = 0.9;
	public static int trainTimes = 10000;
	//
	//public static int [] pool = {2, 100, 167, 67, 43, 90, 23, 90, 103, 1};
	//
	
	public static void main(String[] args){
		Network network = new Network(8, 17, 1);
	//	network.setLearningRate(learnRate);
		network.setClassNumber(2);
	//	network.printNetwork();
//		double[] input = {1.0, 0.0, 1.0};
//		network.forword(input);
//		double[] output = {1.0};
//		network.backpropagation(output);
//		network.printNetwork();
		
		Data trainData = new Data("data\\cod-rna.txt");

		Normalizer normalizer = new Normalizer();
	//	normalizer.normalizeByMinMax(trainData.dataset, true);
	//	normalizer.normalizeByZScore(trainData.dataset, true);
		normalizer.normalizeByLogistic(trainData.dataset, true);

		System.out.println(trainData.calMisRecord(network));
		Random seed = new Random();
		
		for (int i = 0; i < trainTimes; i++){
			if ((i % 100 == 0)){
				System.out.println(trainData.calMisRecord(network));
	//			break;
			}
			network.setLearningRate(Math.pow(Math.E, (double)i / trainTimes * -1) * learnRate);
			int index = Math.abs(seed.nextInt() % trainData.dataset.size());
		//	int index = pool[i % 10];
		//	int index = i % trainData.dataset.size();
	//		System.out.println("=========INPUT: " + index + "=========");
			double[] input = new double[trainData.attributeNum - 2];
			for (int j = 0; j < trainData.attributeNum - 2; j++){
				input[j] = trainData.dataset.get(index)[j+2];
	//			System.out.print(input[j] + " ");
			}
			network.forword(input);
			double[] output = network.getOutputFromNumber(trainData.dataset.get(index)[1]);
	//		System.out.println("output:" + output[0]);
			network.backpropagation(output);
		//	network.printNetwork();
		}
	//	network.printNetwork();
		System.out.println(trainData.calMisRecord(network));
//		Data testData = new Data("data\\glass.t");
//		normalizer.normalizeByMinMax(testData.dataset, false);
//		normalizer.normalizeByZScore(testData.dataset, false);
//	//	normalizer.normalizeByLogistic(testData.dataset, false);
//		System.out.println(testData.calMisRecord(network));
	}
}
