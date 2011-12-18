package cn.ac.iscas.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.ac.iscas.io.SmartFileViewer;
import cn.ac.iscas.segment.ChineseSegment;

public class FeatureGenerator {
	public List<String> termList;
	public Map<String, Integer> dictionaryIndex;
	public String[] fileContent;
	public String[] fileLabel;
	public int fileNum;
	public static final String path = "";
	public Map<String, List<Integer>> tempMap;
	public ChineseSegment cs;
	public InvertedDocumentFrequencyFeature idf;
	public TermFrequenceFeature tf;
	
	public FeatureGenerator()
	{
		termList = new ArrayList<String>();
		dictionaryIndex = new HashMap<String, Integer>();
		cs = new ChineseSegment();
		tempMap = new HashMap();
		readFiles();
		getFeatures();
	}
	
	public void readFiles()
	{
		SmartFileViewer sfv = new SmartFileViewer();
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
			List<String> tokenList = cst.testGetSegmentResult(content);
			for(int j = 0; j < tokenList.size(); j++)
			{	
				if(tempMap.containsKey(tokenList.get(j)))
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
			
			int k = 0;
			for(k = 0; k < termList.size(); k++)
			{
				if(termList.get(k).compareTo(term) > 0)
					break;
			}
			termList.add(k, term);
		}
		
		idf = new InvertedDocumentFrequencyFeature(termList.size());
		tf = new TermFrequenceFeature(fileNum+1, termList.size());
		
		for(int i = 0; i < termList.size(); i++)
		{
			String token = termList.get(i);
			dictionaryIndex.put(token, i);
			List<Integer> idList = tempMap.get(token);
			idf.idfs[i] = (double)fileNum/idList.size();
			int flag = idList.get(0);
			int count = 0;
			for(int j = 0; j < idList.size(); j++)
			{
				if(flag != idList.get(j))
				{
					tf.termfrequenceMatrix[flag][i] = count;
					count = 0;
				}
				else
				{
					count++;
				}
			}
		}
		
		System.out.println("end");
	}
	
	public List<String> getTokenList(String s)
	{
		
	}
}
