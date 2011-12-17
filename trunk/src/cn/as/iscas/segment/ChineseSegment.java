package cn.as.iscas.segment;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.SmartChineseAnalyzer;



public class ChineseSegment {
	Analyzer ca;
	public ChineseSegment(){
	    ca = new SmartChineseAnalyzer(true);
	}
	
	public TokenStream getSegmentResult(String text){
		Reader sentence = new StringReader(text);
		TokenStream ts = ca.tokenStream("sentence", sentence);
		return ts;
	}
}
