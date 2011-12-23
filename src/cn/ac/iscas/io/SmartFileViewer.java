package cn.ac.iscas.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取目录及子目录下指定文件名的路径 并放到一个数组里面返回遍历
 * 
 * @author peterstone
 * 
 */
public class SmartFileViewer {

	public static String path = "";
	// public static final String path =
	// "C:\\Users\\peterstone\\Desktop\\trainset";
	public static List<String> fileList = new ArrayList<String>();
	public static List<String> fileContent;
	public static List<String> fileLabel;
	public static List<Integer> fileId;
	public static int MaxFileNum = 0;
	static int count = 0;

	
	public SmartFileViewer(String path){
		this.path = path;
		fileContent = new ArrayList<String>();
		fileLabel = new ArrayList<String>();
		fileId = new ArrayList<Integer>();
	}

	public static void viewFiles() {
		File dir = new File(path);
		File[] t = dir.listFiles();
		MaxFileNum = t.length;
		List arrayList = SmartFileViewer.getListFiles(path, "txt", true);

		if (arrayList.isEmpty()) {
			System.out.println("没有符合要求的文件");
		} else {
			String message = "";
			message += "符合要求的文件数：" + arrayList.size() + "\r\n";
			System.out.println(message);
		}
	}

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名
	 * @param isdepth
	 *            是否遍历子目录
	 * @return
	 */
	public static List getListFiles(String path, String suffix, boolean isdepth) {
		File file = new File(path);
		return SmartFileViewer.listFile(file, suffix, isdepth);
	}

	public static List listFile(File f, String suffix, boolean isdepth) {
		// 是目录，同时需要遍历子目录
		if (f.isDirectory() && isdepth == true) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				listFile(t[i], suffix, isdepth);
			}
		} else {
			String filePath = f.getAbsolutePath();

			if (suffix != null) {
				int begIndex = filePath.lastIndexOf(".");// 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1)// 防止是文件但却没有后缀名结束的文件
				{
					tempsuffix = filePath.substring(begIndex + 1, filePath
							.length());
				}

				if (tempsuffix.equals(suffix)) {
					int k = 0;
					
					String content = readFile(filePath);
					String filename = f.getName();
					count++;
					if(count % 1000 ==0)
					System.out.println("add " + filename + " (" + count +")..");
					String[] temp = filename.split("_");
					int fileid = Integer.valueOf(temp[0]);
					for(k = 0; k < fileId.size(); k++)
					{
						if(fileid < fileId.get(k))
							break;
					}
					fileId.add(k, fileid);
					fileContent.add(k, content);
					fileList.add(k, f.getAbsolutePath());
					if (temp[1].compareTo("很好.txt") == 0
							|| temp[1].compareTo("好.txt") == 0) {
						fileLabel.add(k,fileid + "\t1");
					} else if (temp[1].compareTo("差.txt") == 0
							|| temp[1].compareTo("很差.txt") == 0) {
						fileLabel.add(k,fileid + "\t-1");
					} else {
						fileLabel.add(k,fileid + "\t0");
					}
				}
			} else {
				// 后缀名为null则为所有文件
				fileList.add(filePath);
			}
		}

		return fileList;

	}

	public static String readFile(String filename) {
		String line = "";
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			String templine = "";
			while ((templine = bfr.readLine()) != null) {
				line += templine;
			}
			bfr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}
}