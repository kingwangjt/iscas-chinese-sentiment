package cn.ac.iscas.features;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import cn.ac.iscas.io.SmartFileViewer;
import cn.ac.iscas.segment.ChineseSegment;

public class FeatureGenerator {
	public List<String> termList;
	public Map<String, Integer> dictionaryIndex;
	public String[] fileContent;
	public String[] fileLabel;
	public static int fileNum;
	public static final int DFThreshold = 1;


	public static String path = "";
	


	public Map<String, List<Integer>> tempMap;
	public ChineseSegment cs;
	public InvertedDocumentFrequencyFeature idf;
	public TermFrequenceFeature tf;
	
	public FeatureGenerator(){
		
	}
	
	public FeatureGenerator(String path)
	{
		this.path = path;
		termList = new ArrayList<String>();
		dictionaryIndex = new HashMap<String, Integer>();
		cs = new ChineseSegment();
		tempMap = new HashMap();
		readFiles();
		getFeatures();
	}
	
	public static String getPath() {
		return path;
	}

	public void readFiles()
	{
		SmartFileViewer sfv = new SmartFileViewer(path);
		sfv.viewFiles();
		fileContent = sfv.fileContent;
		fileLabel = sfv.fileLabel;
		fileNum = sfv.MaxFileNum;
	}
	
	public void getFeatures()
	{
		for(int i = 1; i <= fileNum; i++)
		{
			String content = fileContent[i];
		//	System.out.println(content + "!");
			List<String> tokenList = getTokenList(content);
			for(int j = 0; j < tokenList.size(); j++)
			{
				String s = tokenList.get(j).trim().toLowerCase();
				if(s.compareTo("") == 0)
					continue;
				if(tempMap.containsKey(s))
				{
					List<Integer> idList = tempMap.get(tokenList.get(j));
					idList.add(i);
				}
				else
				{
					List<Integer> list = new ArrayList<Integer>();
					list.add(i);
					tempMap.put(tokenList.get(j), list);
				}
			}
		}
		
		Set<String> termSet = tempMap.keySet();
		Iterator it = termSet.iterator();
		while(it.hasNext())
		{
			String term = (String)it.next();
			term = term.trim().toLowerCase();
			List<Integer> l = tempMap.get(term);
			////
			if(l.size() <= DFThreshold)
				continue;
			////
			int k = 0;
			for(k = 0; k < termList.size(); k++)
			{
				if(termList.get(k).compareTo(term) > 0)
					break;
			}
			termList.add(k, term);
		}
		
		idf = new InvertedDocumentFrequencyFeature(termList.size());
		tf = new TermFrequenceFeature(fileNum, termList.size());
		
		for(int i = 0; i < termList.size(); i++)
		{
			String token = termList.get(i);
			dictionaryIndex.put(token, i);
			List<Integer> idList = tempMap.get(token);
			idf.idfs[i] = (double)fileNum/idList.size();
			int flag = idList.get(0);
			int count = 0;
			List<Point> tfs = null;
			
			if(tf.termfrequenceMatrix.containsKey(flag))
			{
				tfs = tf.termfrequenceMatrix.get(flag);
			}
			else
			{
				tfs = new ArrayList<Point>();
				tf.termfrequenceMatrix.put(flag, tfs);
			}
			
			for(int j = 0; j < idList.size(); j++)
			{
				if(flag != idList.get(j))
				{
					Point temp = new Point(i, count);
					tfs.add(temp);
					count = 1;
					
					flag = idList.get(j);
					
					if(tf.termfrequenceMatrix.containsKey(flag))
					{
						tfs = tf.termfrequenceMatrix.get(flag);
					}
					else
					{
						tfs = new ArrayList<Point>();
						tf.termfrequenceMatrix.put(flag, tfs);
					}
				}
				else
				{
					count++;
				}
			}
			tfs.add(new Point(i, count));
		}
		
		System.out.println("Generation ends.");
		int num = this.termList.size();
		try{
			BufferedWriter bfw = new BufferedWriter(new FileWriter(".//svm_tf.txt"));
			
			for(int i = 1; i <= fileNum; i++)
			{
				String line = "";
				List<Point> tfs = tf.termfrequenceMatrix.get(i);
				line += this.fileLabel[i];
				System.out.println("文档" + i + "包括下列词：");
				if(tfs == null)
				{
					System.out.print("不含任何词");
				}
				else
				{
					for(int j = 0; j < tfs.size(); j++)
					{
						Point p = tfs.get(j);
						System.out.print(termList.get(p.x) + "  " + p.y + "  ");
						line += "\t" + p.x + ":" + p.y;
					}
				}
				line += "\n";
				bfw.write(line);
				System.out.println();
			}
			bfw.flush();
			bfw.close();
			
			/*for(int i = 1; i <= fileNum; i++)
			{
				int k = 0;
				String line = "";
				List<Point> tfs = tf.termfrequenceMatrix.get(i);
				line += this.fileLabel[i];
				System.out.println("文档" + i + "包括下列词：");
				if(tfs == null)
				{
					System.out.print("不含任何词");
					for(int q = 0; q < termList.size(); q++)
						line += "\t" + 0;
				}
				else
				{
					for(int j = 0; j < tfs.size(); j++)
					{
						Point p = tfs.get(j);
						System.out.print(termList.get(p.x) + "  " + p.y + "  ");
						while(k < p.x)
						{
							line += "\t" + 0;
							k++;
						}
						k++;
						line += "\t" + p.y;
					}
					
					while(k < this.termList.size())
					{
						line += "\t" + 0;
						k++;
					}
				}
				line += "\n";
				bfw.write(line);
				System.out.println();
			}
			bfw.flush();
			bfw.close();*/
			
			BufferedWriter bfw2 = new BufferedWriter(new FileWriter(".//svm_term.txt"));
			for(int i = 0; i < termList.size(); i++)
			{
				String line = "";
				line += i + "\t" + termList.get(i) + "\n";
				bfw2.write(line);
			}
			bfw2.flush();
			bfw2.close();
		} catch (Exception e){
			e.printStackTrace();
		}			
	}
	
	public List<String> getTokenList(String s)
	{
		List<String> tokenList = new ArrayList<String>();
		TokenStream ts = cs.getSegmentResult(s);
		Token nt = new Token();
		try{
		    nt = ts.next(nt);
		    while (nt != null) {
		      tokenList.add(nt.term());
		      nt = ts.next(nt);
		    }
		    ts.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return tokenList;
	}
	
	public static void setPath(String path) {
		FeatureGenerator.path = path;
	}
	
	public static int getFileNum() {
		return fileNum;
	}

	public static void setFileNum(int fileNum) {
		FeatureGenerator.fileNum = fileNum;
	}
	
	
	 public void writeFeatures()
	 {
	        try {
	            FileOutputStream fos = new FileOutputStream("tf");
	            ObjectOutputStream oos = new ObjectOutputStream(fos);            
	            oos.writeObject(tf);
	            oos.flush();
	            oos.close();
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	 }
	 
	 public TermFrequenceFeature readFeatures()
	 {
		 TermFrequenceFeature t = null;
		 try {
	            FileInputStream fis = new FileInputStream("tf");
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            
	            t = (TermFrequenceFeature)ois.readObject();
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
		 return t;
	 }
}
