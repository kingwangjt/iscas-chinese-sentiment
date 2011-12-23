package cn.ac.iscas.io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import cn.ac.iscas.segment.ChineseSegment;

public class XmlReader {
	
	ChineseSegment chs = new ChineseSegment();
	Map<String, Integer> terms= new HashMap<String, Integer>();
	List<String> dic = new ArrayList<String>();
	
	public XmlReader(String termpath)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(termpath));
			String line = "";
			String[] temp = new String[2];
			while((line = br.readLine()) != null)
			{
				temp = line.split("\t");
				int linenum = Integer.valueOf(temp[0]);
				terms.put(temp[1], linenum);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void filter(String path)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = "";
			String[] temp = new String[3];
			while((line = br.readLine()) != null)
			{
				temp = line.split("\t");
				dic.add(temp[0]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		new XmlReader(".\\svm_term.txt").getDocs(".\\xml\\test.xml");
	}
	
	public List<Doc> getDocs(String path)
	{
		List<Doc> doclist = new ArrayList<Doc>();
		filter(".\\dic\\SogouLabDic.dic");
		try 
		{
			SAXBuilder sax=new SAXBuilder(); // 创建一个sa
			Document doc=sax.build(path);  //得到Document对应的XML
			Element el=doc.getRootElement(); //得到根节点，对应docs
			List<Element> list= el.getChildren("doc");  //得到doc节点集
			
			for(Element everydoc:list)
			{
				//取属性，并修改
				Doc d = new Doc();
				int docno=Integer.valueOf(everydoc.getChildTextTrim("docno"));//获取属性内容
				System.out.println(docno);
				d.ID = docno;
				String text=everydoc.getChildTextTrim("text"); //获取节点内容
				System.out.println(text);
				List<String> tempList = getTokenList(text);
				
				for(int j = 0; j < tempList.size(); j++)
				{
					if(this.terms.containsKey(tempList.get(j)))
					{
						List<Point> tf = d.tfs;
						int id = this.terms.get(tempList.get(j));
						
						if(tf.size() == 0)
						{
							tf.add(new Point(id, 1));
							continue;
						}
						int k = 0;
						for(k = 0; k < tf.size(); k++)
						{
							if(tf.get(k).x == id)
							{
								tf.get(k).y++;
								break;
							}
							else if(tf.get(k).x > id)
							{
								Point p = new Point(id, 1);
								tf.add(k, p);
								break;
							}
							else
							{
								continue;
							}
						}
						if(k >= tf.size())
						{
							tf.add(new Point(id, 1));
						}
					}
					else
					{
						continue;
					}
				}
				doclist.add(d);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return doclist;
	}
	
	public List<String> getTokenList(String s)
	{
		List<String> tokenList = new ArrayList<String>();
		TokenStream ts = chs.getSegmentResult(s);
		Token nt = new Token();
		try{
		    nt = ts.next(nt);
		    while (nt != null) {
		      String temp = nt.term().trim().toLowerCase();
		      for(int i = 0; i < dic.size(); i++)
		      {
		    	  if(dic.get(i).compareTo(temp) == 0)
		    		  {
		    		  	tokenList.add(temp);  
		    		  }
		      }
		      nt = ts.next(nt);
		    }
		    ts.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return tokenList;
	}
}
