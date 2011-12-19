package cn.ac.iscas.features;

import junit.framework.TestCase;

public class FeatureGeneratorTest extends TestCase {

	public void testGetFeatures() {
		FeatureGenerator.setPath("D:\\dataset");
		FeatureGenerator.setFileNum(116436);
		FeatureGenerator fg = new FeatureGenerator();
	}
}
