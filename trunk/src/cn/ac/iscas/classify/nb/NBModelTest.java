package cn.ac.iscas.classify.nb;

import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.sentiment.featureselect.CHIFilter;
import cn.ac.iscas.sentiment.featureselect.DFFilter;
import cn.ac.iscas.sentiment.featureselect.IGFilter;
import junit.framework.TestCase;

public class NBModelTest extends TestCase {

	public void testNBModel() {
		SmartData trainData = new SmartData("test\\5-fold Cross Validation.trainning.2.txt");
//		DFFilter df = new DFFilter(trainData);
//		IGFilter ig = new IGFilter(trainData);
//		CHIFilter chi = new CHIFilter(trainData);
		SmartData testData = new SmartData("test\\5-fold Cross Validation.testing.2.txt");
//		ig.filter(testData);
		NBModel nbm = new NBModel(trainData);
		nbm.train();
//		nbm.printModel();
		nbm.test(testData);
	}
}
