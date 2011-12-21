package cn.ac.iscas.classify.nb;

import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.sentiment.featureselect.CHIFilter;
import cn.ac.iscas.sentiment.featureselect.DFFilter;
import cn.ac.iscas.sentiment.featureselect.IGFilter;
import junit.framework.TestCase;

public class NBModelTest extends TestCase {

	public void testNBModel() {
		SmartData trainData = new SmartData("data\\svm_tf.txt");
//		DFFilter df = new DFFilter(trainData);
//		IGFilter ig = new IGFilter(trainData);
		CHIFilter chi = new CHIFilter(trainData);
		
		NBModel nbm = new NBModel(trainData);
		nbm.train();
//		nbm.printModel();
		nbm.test(trainData);
	}
}
