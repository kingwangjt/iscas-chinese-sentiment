package cn.ac.iscas.classify.nn;
import java.util.Random;


public class Network {
	public int classNumber = 2;
	double[][] inputWeight;
	double[][] outputWeight;
	double[] hiddenBias;
	double[] outputBias;
	double[] input;
	double[] hiddenLayer;
	double[] outputLayer;
	int inputLayerNumber, hiddenLayerNumber, outputLayerNumber;
	double learningRate = 0.3;
	double hitThreshold = 0.5;
	boolean isDebug = false;
	
	public Network(int inputLayerNumber, int hiddenLayerNumber, int outputLayerNumber){
		this.inputLayerNumber = inputLayerNumber;
		this.hiddenLayerNumber = hiddenLayerNumber;
		this.outputLayerNumber = outputLayerNumber;
		inputWeight = new double[inputLayerNumber][hiddenLayerNumber];
		outputWeight = new double[hiddenLayerNumber][outputLayerNumber];
		hiddenBias = new double[hiddenLayerNumber];
		outputBias = new double[outputLayerNumber];
		initNetwork();
	}
	
	public void setLearningRate(double l){
		learningRate = l;
	}
	
	public void setClassNumber(int num){
		classNumber = num;
	}
	
	void initNetwork(){
		Random seed = new Random();
		for (int i = 0; i < inputLayerNumber; i++){
			for (int j = 0; j < hiddenLayerNumber; j++)
				inputWeight[i][j] = getRandomValue(seed);
		}
		
		for (int i = 0; i < hiddenLayerNumber; i++){
			for (int j = 0; j < outputLayerNumber; j++)
				outputWeight[i][j] = getRandomValue(seed);
		}
		
		for (int i = 0; i < hiddenLayerNumber; i++){
			hiddenBias[i] = getRandomValue(seed);
		}
		
		for (int i = 0; i < outputLayerNumber; i++){
			outputBias[i] = getRandomValue(seed);
		}
	}
	
	void initNetworkTest(){
		double[][] data1 = {
				{0.2, -0.3},
				{0.4, 0.1},
				{-0.5, 0.2}
		};
		
		inputWeight = data1;
		
		double[][] data2 = {
				{-0.3},
				{-0.2}
		};
		outputWeight = data2;
		
		double[] data3 = {
				-0.4, 0.2
		};
		hiddenBias = data3;
		
		double[] data4 = {
			0.1	
		};
		outputBias = data4;
	}
	
	public void forword(double[] input){
		this.input = input;
		hiddenLayer = new double[hiddenLayerNumber];
		for (int i = 0; i < hiddenLayerNumber; i++){
			hiddenLayer[i] = hiddenBias[i];
			for (int j = 0; j < inputLayerNumber; j++){
				hiddenLayer[i] += input[j] * inputWeight[j][i];
			}
			hiddenLayer[i] = getOutputFromInput(hiddenLayer[i]);
		}
		
		outputLayer = new double[outputLayerNumber];
		for (int i = 0; i < outputLayerNumber; i++){
			outputLayer[i] = outputBias[i];
			for (int j = 0; j < hiddenLayerNumber; j++){
				outputLayer[i] += hiddenLayer[j] * outputWeight[j][i];
			}
			outputLayer[i] = getOutputFromInput(outputLayer[i]);
		}
	}
	
	public void backpropagation(double[] output){
		double[] outputError = new double[outputLayerNumber];
		for (int i = 0; i < outputLayerNumber; i++){
			outputError[i] = outputLayer[i] * (1 - outputLayer[i]);
			outputError[i] *= (output[i] - outputLayer[i]);
		}
		
		double[] hiddenError = new double[hiddenLayerNumber];
		for (int i = 0; i < hiddenLayerNumber; i++){
			hiddenError[i] = hiddenLayer[i] * (1 - hiddenLayer[i]);
			double temp = 0;
			for (int j = 0; j < outputLayerNumber; j++){
				temp += outputError[j] * outputWeight[i][j];
			}
			hiddenError[i] *= temp;
		}
		
		for (int i = 0; i < hiddenLayerNumber; i++){
			for (int j = 0; j < outputLayerNumber; j++){
				outputWeight[i][j] += learningRate * outputError[j] * hiddenLayer[i];
			}
		}
		
		for (int i = 0; i < inputLayerNumber; i++){
			for (int j = 0; j < hiddenLayerNumber; j++){
				inputWeight[i][j] += learningRate * hiddenError[j] * input[i];
			}
		}
		
		for (int i = 0; i < outputLayerNumber; i++){
			outputBias[i] += learningRate * outputError[i];
		}
		
		for (int i = 0; i < hiddenLayerNumber; i++){
			hiddenBias[i] += learningRate * hiddenError[i];
		}
	}

	//Random double from -1 to 1
	double getRandomValue(Random seed){
		return (seed.nextDouble() - 0.5) * 2;
	}
	
	double getOutputFromInput(double input){
		double temp = 1 + Math.pow(Math.E, -input);
		return (double)1 / temp;
	}
	
	public void printNetwork(){
		System.out.println("Input Matrix");
		for (int i = 0; i < inputLayerNumber; i++){
			for (int j = 0; j < hiddenLayerNumber; j++)
			{
				System.out.printf("% .3f ", inputWeight[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("Output Matrix");
		for (int i = 0; i < hiddenLayerNumber; i++){
			for (int j = 0; j < outputLayerNumber; j++)
			{
				System.out.printf("% .3f ", outputWeight[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("HiddenLayer Bias");
		for (int i = 0; i < hiddenLayerNumber; i++){
			System.out.printf("% .3f ", hiddenBias[i]);
		}
		System.out.println();
		
		System.out.println("OutLayer Bias");
		for (int i = 0; i < outputLayerNumber; i++){
			System.out.printf("% .3f ", outputBias[i]);
		}
		System.out.println();
	}
	
	public boolean testTuple(double[] input){
		double[] tuple = new double[input.length - 2];
		for (int j = 0; j < input.length - 2; j++){
			tuple[j] = input[j+2];
		}
		this.forword(tuple);
		double[] output = getOutputFromNumber(input[1]);
		if (classNumber == 2){
			if (Math.abs(outputLayer[0] - output[0]) > hitThreshold)
				return false;
			else
				return true;
		} else{
			int maxi = 0;
			double max = outputLayer[0];
			for (int i = 1; i < outputLayerNumber; i++){
				if (outputLayer[i] > max){
					maxi = i;
					max = outputLayer[i];
				}
			}
			if (Math.abs(outputLayer[maxi] - output[maxi]) > hitThreshold)
				return false;
			else
				return true;
		}
	}
	
	public double[] getOutputFromNumber(double output){
		double[] result;
		if (classNumber == 2){
			result = new double[1];
			result[0] = output;
		} else {
			result = new double[classNumber];
			result[(int)output] = 1;
		}

		return result;
	}
}
