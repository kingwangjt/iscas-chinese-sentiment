package cn.ac.iscas.features;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class FeatureGeneratorTest extends TestCase {

//	public void testGetFeatures() {
//		FeatureGenerator fg = new FeatureGenerator("D:\\dianping\\dataset2");
//		fg.writeFeatures();
//	}
	
	public void testReadFeatures() {
		FeatureGenerator fg = new FeatureGenerator();
		TermFrequenceFeature tf = fg.readFeatures();
		Set<Integer> termSet = tf.termfrequenceMatrix.keySet();
		System.out.println("KeySet Size: " + termSet.size());
		Iterator it = termSet.iterator();
		while(it.hasNext())
		{
			Integer term = (Integer)it.next();
			System.out.println(term);
		}
		
	}
}
