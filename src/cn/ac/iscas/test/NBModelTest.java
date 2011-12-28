package cn.ac.iscas.test;

import java.util.List;

import junit.framework.TestCase;
import cn.ac.iscas.classify.nb.NBModel;
import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.SmartData;
import cn.ac.iscas.io.XmlReader;
import cn.ac.iscas.sentiment.featureselect.CHIFilter;

public class NBModelTest extends TestCase {

	public void testNBModel() {
		String path = "";
		SmartData trainData = new SmartData("test\\5-fold Cross Validation.trainning.1.txt");
		CHIFilter chi = new CHIFilter(trainData);

		NBModel nbm = new NBModel(trainData, 0.1);
		nbm.train();
		List<Doc> doclist = new XmlReader("data\\svm_term.txt").getDocs(".\\xml\\test.xml");
		nbm.test(doclist);
	}
}
