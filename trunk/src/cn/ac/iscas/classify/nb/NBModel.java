package cn.ac.iscas.classify.nb;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;

public class NBModel {
	SmartData trainData;
	int p = 0, n = 0;
	int pcount = 0, ncount = 0;
	int sumcount;
	double CANTJUDGETHRESHOLD = 0.2;
	//x positive, y negtive
	ArrayList<TFCount> tfcount = new ArrayList<TFCount>();
	double pPositive = 0, pNegtive = 0;
	
	
	public NBModel(SmartData trainData){
		this.trainData = trainData;
	}
	
	public NBModel(SmartData trainData, double cantJudgeThreshold){
		this.trainData = trainData;
		this.CANTJUDGETHRESHOLD = cantJudgeThreshold;
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
	
	public double test(List<Doc> doclist)
	{
		System.out.println("tfcount size:" + tfcount.size());
		int right = 0, wrong = 0;
		int right2 = 0, wrong2 = 0;
		int orlNonCommentCount = 0;
		int predictNonCommentCount = 0;
		int rightNonCommentCount = 0;
		for (int i = 0; i < doclist.size(); i++){
			Doc doc = doclist.get(i);
			int sentiment = getSentiment(doc);
			if(sentiment == 1)
				System.out.println(doc.ID + "\tyes");
			else if(sentiment == 0)
				System.out.println(doc.ID + "\tna");
			else
				System.out.println(doc.ID + "\tno");
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
//		System.out.println("rC : " + rightNonCommentCount);
//		System.out.println("aC : " + orlNonCommentCount);
//		System.out.println("gC : " + predictNonCommentCount);
//		System.out.println("Accuracy: " + (double)right/(right+wrong));
//		System.out.println("Accuracy(Without Non-Comment): " + (double)right2/(right2+wrong2));
//		System.out.println("Precise(Non Coment): " + (double)rightNonCommentCount/predictNonCommentCount);
//		System.out.println("Recall(Non Coment): " + (double)rightNonCommentCount/orlNonCommentCount);
		return (double)right/(right+wrong);
	}
	
	public double test(SmartData test){
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
//		System.out.println("rC : " + rightNonCommentCount);
//		System.out.println("aC : " + orlNonCommentCount);
//		System.out.println("gC : " + predictNonCommentCount);
		System.out.println("Accuracy: " + (double)right/(right+wrong));
		System.out.println("Accuracy(Without Non-Comment): " + (double)right2/(right2+wrong2));
		System.out.println("Precise(Non Coment): " + (double)rightNonCommentCount/predictNonCommentCount);
		System.out.println("Recall(Non Coment): " + (double)rightNonCommentCount/orlNonCommentCount);
		return (double)right/(right+wrong);
	}
	
	int getSentiment(Doc doc){
		int sentiment = 0;
		double pProbability = Math.log(pPositive);
		double nProbability = Math.log(pNegtive);
	//	System.out.println(pProbability + ":" + nProbability);
		boolean hasTerm = false;
//		System.out.println(doc.ID + " tfs size: " + doc.tfs.size());
		for (int i = 0; i < doc.tfs.size(); i++){
			
			Point tf = doc.tfs.get(i);
			int index = getIndex(tf.x);
			if (index < 0) continue;
			hasTerm = true;
			pProbability += tfcount.get(index).pProbability * tf.y;
			nProbability += tfcount.get(index).nProbability * tf.y;
		}
		if (!hasTerm){
	//		System.out.println(doc.ID + " : No available term!");
			return 0;
		}
		if (Math.abs(pProbability - nProbability) < CANTJUDGETHRESHOLD){
	//		System.out.println(doc.ID + " : Can not judge!");
			return 0;
		}
		if (pProbability > nProbability) return 1;
		if (nProbability > pProbability) return -1;
//		System.out.println(pProbability + ":" + nProbability);
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
	//		System.out.println(i + " " + tfcount.get(i).pProbability + ":" + tfcount.get(i).nProbability);
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
