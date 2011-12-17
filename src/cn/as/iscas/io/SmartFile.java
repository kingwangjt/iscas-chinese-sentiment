package cn.as.iscas.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SmartFile {
	BufferedReader bfr;
	BufferedWriter bfw;
	boolean isRead;
	public SmartFile(String path, boolean isRead){
		this.isRead = isRead;
		if (isRead){
			try{
				bfr = new BufferedReader(new FileReader(path));
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			try{
				bfw = new BufferedWriter(new FileWriter(path));
			} catch (Exception e){
				e.printStackTrace();
			}			
		}		
	}
	
	public String readLine(){
		String line = null;
		try{
			line = bfr.readLine();
		} catch (Exception e){
			e.printStackTrace();
		}
		return line;
	}
	
	public void writeLine(String line){
		try{
			bfw.append(line + '\n');
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		if (this.isRead){
			try {
				bfr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				bfw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
