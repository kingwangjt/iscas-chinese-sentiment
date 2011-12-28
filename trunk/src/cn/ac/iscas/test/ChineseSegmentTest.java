package cn.ac.iscas.test;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import cn.ac.iscas.segment.ChineseSegment;

import junit.framework.TestCase;

public class ChineseSegmentTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetSegmentResult() {
		ChineseSegment cs = new ChineseSegment();
		String text = "打雷啦，下雨收衣服啦！";
		TokenStream ts = cs.getSegmentResult(text);
		Token nt = new Token();
		try{
		    nt = ts.next(nt);
		    while (nt != null) {
		      System.out.println(nt.term());
		      nt = ts.next(nt);
		    }
		    ts.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
