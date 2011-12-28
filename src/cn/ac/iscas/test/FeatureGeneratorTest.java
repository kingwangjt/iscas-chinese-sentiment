package cn.ac.iscas.test;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.ac.iscas.features.FeatureGenerator;
import cn.ac.iscas.features.TermFrequenceFeature;

import junit.framework.TestCase;

public class FeatureGeneratorTest extends TestCase {
	public void testReadFeatures() {
		FeatureGenerator fg = new FeatureGenerator();
		TermFrequenceFeature tf = fg.readFeatures();
		Set<Integer> termSet = tf.termfrequenceMatrix.keySet();
		System.out.println("KeySet Size: " + termSet.size());
		Iterator it = termSet.iterator();
		while(it.hasNext())
		{
			Integer term = (Integer)it.next();
			List<Point> tfs = tf.termfrequenceMatrix.get(term);
			System.out.println(term);
			for (int i = 0; i < tfs.size(); i++){
				System.out.print(tfs.get(i).x + ":" + tfs.get(i).y + " ");
			}
			System.out.println();
		}	
	}
	
	public void testGetFeatures() {
		FeatureGenerator fg = new FeatureGenerator("D:\\dianping\\dataset2");
		fg.writeFeatures();
	}
}
