package cn.ac.iscas.io;

import junit.framework.TestCase;

public class SmartFileTest extends TestCase {
	public void testWriteLine() {
		SmartFile sf = new SmartFile("testSmartFile.txt", false);
		sf.writeLine("Hello World!");
		sf.writeLine("= =!");
		sf.close();
	}
	
	public void testReadLine() {
		SmartFile sf = new SmartFile("testSmartFile.txt", true);
		System.out.println(sf.readLine());
		System.out.println(sf.readLine());
		sf.close();
	}

}
