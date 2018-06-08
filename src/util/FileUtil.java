package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import annotation.action;

public class FileUtil {
	
	
	public static void saveFile(String filename,String content){
		FileWriter fw = null;
		try {
		//如果文件存在，则追加内容；如果文件不存在，则创建文件
		File f=new File(filename);
		 if (!f.exists()) {
	            try {
	                f.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
		 }
		fw = new FileWriter(f, true);
		} catch (IOException e) {
		e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(content);
		pw.flush();
		try {
		fw.flush();
		pw.close();
		fw.close();
		} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	

}
