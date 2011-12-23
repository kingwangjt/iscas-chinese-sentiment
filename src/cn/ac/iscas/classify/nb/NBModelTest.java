package cn.ac.iscas.classify.nb;

import java.util.List;

import junit.framework.TestCase;
import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.io.XmlReader;
import cn.ac.iscas.sentiment.featureselect.CHIFilter;

public class NBModelTest extends TestCase {

	public void testNBModel() {
		String path = "";
		SmartData trainData = new SmartData("test\\10-fold Cross Validation.trainning.2.txt");
//		DFFilter df = new DFFilter(trainData);
//		IGFilter ig = new IGFilter(trainData, 0.8);
		CHIFilter chi = new CHIFilter(trainData);
//		SmartData testData = new SmartData("test\\10-fold Cross Validation.testing.2.txt", true);
//		ig.filter(testData);
		NBModel nbm = new NBModel(trainData, 0.1);
		nbm.train();
		List<Doc> doclist = new XmlReader(".\\svm_term.txt").getDocs(".\\xml\\test.xml");
//		nbm.printModel();
//		nbm.test(testData);
		nbm.test(doclist);
	}
}
