package cn.ac.iscas.io;

import junit.framework.TestCase;

public class SmartDataTest extends TestCase {

	public void testGetData() {
		SmartData smartData = new SmartData("data\\svm_tf.txt");
		smartData.printData();
	}

	public void testPrintData() {
		
	}

}
