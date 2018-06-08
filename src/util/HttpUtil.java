package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	
	public static void downloadFile(String remoteFilePath, String localFilePath){
		 URL urlfile = null;
	        HttpURLConnection httpUrl = null;
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        File f = new File(localFilePath);
	        try
	        {
	            urlfile = new URL(remoteFilePath);
	            httpUrl = (HttpURLConnection)urlfile.openConnection();
	            httpUrl.connect();
	            bis = new BufferedInputStream(httpUrl.getInputStream());
	            bos = new BufferedOutputStream(new FileOutputStream(f));
	            int len = 2048;
	            byte[] b = new byte[len];
	            while ((len = bis.read(b)) != -1)
	            {
	                bos.write(b, 0, len);
	            }
	            bos.flush();
	            bis.close();
	            httpUrl.disconnect();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            try
	            {
	                bis.close();
	                bos.close();
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	        }
	}
	
	
	/**
	    * 
	    * @author  jing
	    * @date 2017年2月4日  新建  
	    * @param urlStr
	    * @return
	    * @throws Exception
	    */
	    public static String get(String urlStr) throws Exception  
	    {  
	  
	        URL url = new URL(urlStr);  
	        URLConnection urlConnection = url.openConnection(); // 打开连接  
	  
	        System.out.println(urlConnection.getURL().toString());  
	  
	        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流  
	        String line = null;  
	        StringBuilder sb = new StringBuilder();  
	        while ((line = br.readLine()) != null)  
	        {  
	            sb.append(line + "\n");  
	        }  
	        br.close();  
	        return sb.toString(); 
	    }
		/**
		 * 获取远程url的信息
		 * @param String
		 * @return HashMap
		 */
		

		public static HashMap<String, JSONObject> loadJson (String url) {  
	        StringBuilder json = new StringBuilder();  
	        try {  
	            URL urlObject = new URL(url);  
	            URLConnection uc = urlObject.openConnection();  
	            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
	            String inputLine = null;  
	            while ( (inputLine = in.readLine()) != null) {  
	                json.append(inputLine);  
	            }  
	            in.close();  
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return JsonToMapList.getMap(String.valueOf(json));  
	    }  
		
		public static HashMap<String, Integer> loadJsonToInt(String url) throws IOException  {
			 StringBuilder json = new StringBuilder();  
		      
		            URL urlObject = new URL(url);  
		            URLConnection uc = urlObject.openConnection();  
		            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
		            String inputLine = null;  
		            while ( (inputLine = in.readLine()) != null) {  
		                json.append(inputLine);  
		            }  
		            in.close();  
		        
		        return JSON.parseObject(String.valueOf(json),new HashMap<String,Integer>().getClass());  
		}
		
		
		public static String postHtpps(String urlStr, String xmlInfo) {
			try {
				URL url = new URL(urlStr);
				URLConnection con = url.openConnection();
				con.setDoOutput(true);
//				con.setRequestProperty("Pragma:", "no-cache");
				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");
				out.write(xmlInfo);
				out.flush();
				out.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuffer lines = new StringBuffer();
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine()) {
					lines.append(line);
				}
				return lines.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String postab(String urlStr, String token) {
			try {
				URL url = new URL(urlStr);
				URLConnection con = url.openConnection();
				con.setDoOutput(true);
//				con.setRequestProperty("Pragma:", "no-cache");
				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
				con.setRequestProperty("token", token);
				OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");
	 
				out.flush();
				out.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuffer lines = new StringBuffer();
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine()) {
					lines.append(line);
				}
				return lines.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
		
	

}
