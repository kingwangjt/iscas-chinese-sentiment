package cn.ac.iscas.classify.nb;

import java.awt.Point;
import java.util.ArrayList;

import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;

public class NBModel {
	SmartData trainData;
	int p = 0, n = 0;
	int pcount = 0, ncount = 0;
	int sumcount;
	//x positive, y negtive
	ArrayList<TFCount> tfcount = new ArrayList<TFCount>();
	double pPositive = 0, pNegtive = 0;
	
	
	public NBModel(SmartData trainData){
		this.trainData = trainData;
	}
	
	public void train(){
		for (int i = 0; i < trainData.doclist.size(); i++){
//			if (i % 100 == 0){
//				System.out.print(i + " datas processed...");
//				System.out.println("term size: " + tfcount.size());
//			}
			Doc doc = trainData.doclist.get(i);
			if (doc.sentiment > 0){
				p++;
			} else {
				n++;
			}
			for (int j = 0; j < doc.tfs.size(); j++){
				Point tf = doc.tfs.get(j);
				if (doc.sentiment > 0){
					pcount += tf.y;
				} else {
					ncount += tf.y;
				}
				addTrainTF(tf.x, tf.y, doc.sentiment);
			}
			
		}
		pPositive = (double)p / (p + n);
		pNegtive = (double)n / (p + n);
		setProbability();
	}
	
	public void test(SmartData test){
		System.out.println("tfcount size:" + tfcount.size());
		int right = 0, wrong = 0;
		int right2 = 0, wrong2 = 0;
		int orlNonCommentCount = 0;
		int predictNonCommentCount = 0;
		int rightNonCommentCount = 0;
		for (int i = 0; i < test.doclist.size(); i++){
			Doc doc = test.doclist.get(i);
			int sentiment = getSentiment(doc);
			if (doc.sentiment == 0) orlNonCommentCount++;
			//System.out.println(sentiment);
			if (sentiment == 0)	predictNonCommentCount++;
			if ((sentiment == 0) && (doc.sentiment == 0)) rightNonCommentCount++;
			
			if (sentiment == doc.sentiment) right++;
			else wrong++;
			if ((doc.sentiment != 0) && (sentiment != 0)){
				if (sentiment == doc.sentiment) right2++;
				else wrong2++;				
			}
		}
		System.out.println("rC : " + rightNonCommentCount);
		System.out.println("aC : " + orlNonCommentCount);
		System.out.println("gC : " + predictNonCommentCount);
		System.out.println("Accuracy: " + (double)right/(right+wrong));
		System.out.println("Accuracy(Without Non-Comment): " + (double)right2/(right2+wrong2));
		System.out.println("Precise(Non Coment): " + (double)rightNonCommentCount/predictNonCommentCount);
		System.out.println("Recall(Non Coment): " + (double)rightNonCommentCount/orlNonCommentCount);
	}
	
	int getSentiment(Doc doc){
		int sentiment = 0;
		double pProbability = pPositive;
		double nProbability = pNegtive;
		boolean hasTerm = false;
		for (int i = 0; i < doc.tfs.size(); i++){
			Point tf = doc.tfs.get(i);
			int index = getIndex(tf.x);
			if (index < 0) continue;
			hasTerm = true;
			pProbability *= Math.pow(tfcount.get(index).pProbability, tf.y);
			nProbability *= Math.pow(tfcount.get(index).nProbability, tf.y);
		}
		if (!hasTerm) return 0;
		if (pProbability > nProbability) return 1;
		if (nProbability > pProbability) return -1;
		return sentiment;
	}
	
	private void addTrainTF(int id, int freq, int sentiment){
		int index = -1;
		for (int i = 0; i < tfcount.size(); i++){

			TFCount tfc = tfcount.get(i);
			if (tfc.ID == id){
				addTF(tfc, freq, sentiment);
				return;
			}
		}
		TFCount newtfc = new TFCount();
		newtfc.ID = id;
		addTF(newtfc, freq, sentiment);
		tfcount.add(newtfc);
	}
	
	private int getIndex(int id){
		int index = -1;
		for (int i = 0; i < tfcount.size(); i++){
			TFCount tfc = tfcount.get(i);
			if (tfc.ID == id){
				return i;
			}
		}
		return index;
	}
	
	void addTF(TFCount tfc, int freq, int sentiment){
		if (sentiment > 0){
			tfc.p += freq;
		} else {
			tfc.n += freq;
		}
	}
	
	public void setProbability(){
		for (int i = 0; i < tfcount.size(); i++){
			tfcount.get(i).setProbability(pcount, ncount, tfcount.size());
		}
	}
	
	public void printModel(){
		System.out.println("Positive Probablity: " + pPositive);
		System.out.println("Negtive Probablity: " + pNegtive);
		for (int i = 0; i < tfcount.size(); i++){
			System.out.println(tfcount.get(i).getString());
		}
	}
}
