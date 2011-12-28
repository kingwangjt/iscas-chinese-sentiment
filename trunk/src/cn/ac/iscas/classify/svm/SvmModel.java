package cn.ac.iscas.classify.svm;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import junit.framework.TestCase;
import cn.ac.iscas.io.Doc;
import cn.ac.iscas.io.XmlReader;

public class SvmModel extends TestCase{
	public void testSvmModel()
	{
		String path = ".\\svm_test.txt";
		String line = "";
		BufferedWriter bw; 
		List<Doc> doclist = new XmlReader(".\\svm_term.txt").getDocs(".\\xml\\test.xml");
		try
		{
			bw = new BufferedWriter(new FileWriter(path));
			for(int i = 0; i < doclist.size(); i++)
			{
				Doc d = doclist.get(i);
				line += "1";
				List<Point> tf = d.tfs;
				if(tf.size() == 0)
					continue;
				for(int j = 0; j < tf.size(); j++)
				{
					line +="\t"+ tf.get(j).x + ":" + tf.get(j).y;
				}
				line += "\n";
			}
			bw.write(line);
			bw.flush();
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
