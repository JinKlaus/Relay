package collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;



public class UrlUtil {
	
	public static String UN_READ = "UN_READ";
	//读取远程url
	public static String ReadUrl(String FileName) throws IOException {
		System.out.println("请求url:"+FileName);
		String read;
	  String readStr ="";
		
		 URL url =new URL(FileName);
		HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
		
		 urlCon.setConnectTimeout(5000);
		 urlCon.setReadTimeout(5000);
		 urlCon.connect();
		 BufferedReader br =new BufferedReader(new InputStreamReader( urlCon.getInputStream(),"gbk"));
		while ((read = br.readLine()) !=null) {
		readStr = readStr + read;
		 }
		 br.close();
		 urlCon.disconnect();
		 System.out.println("请求url:成功");
		 return readStr;
	}
	
	public static String getYCFile(String urlPath) throws IOException {  
        String readStr = "";  
        System.out.println("请求url:"+urlPath);
                String strUrl = urlPath.trim();  
                URL url = new URL(strUrl);  
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();  
                urlCon.setConnectTimeout(2000);  
                urlCon.setReadTimeout(2000);  
                urlCon.setRequestMethod("GET");
                urlCon.setUseCaches(true);
                urlCon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
                int code = urlCon.getResponseCode();
                System.out.println("code:"+code);
                BufferedReader in = new BufferedReader(new InputStreamReader(  
                        urlCon.getInputStream(), "GBK"));  
                String inputLine = " ";  
                while ((inputLine = in.readLine()) != null) {  
                    readStr += inputLine.trim();  
                }  
                in.close();  
                System.out.println("请求url:成功");
                return readStr;  
          
    }  
	
	 
	public static String doGet(String url,String charset) throws Exception{
		 HttpGet get = new HttpGet(url);
		 RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
		 get.setConfig(requestConfig);
		 get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
		 CloseableHttpClient httpClient = HttpClients.createDefault(); 
	            HttpResponse response = httpClient.execute(get);
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	                /**读取服务器返回过来的json字符串数据**/  
	                String strResult = EntityUtils.toString(response.getEntity());  
	                  
	                return strResult;  
	            }  
	            
	            return "";	             
	      
	     
	}
	
	/** 
     * post请求（用于请求json格式的参数） 
     * @param url 
     * @param params 
     * @return 
     */  
    public static String doPost(String url, HashMap<String, Object> params,String cookie) throws Exception {  
          
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpPost httpPost = new HttpPost(url);// 创建httpPost     
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "text/plain;charset=UTF-8");
        httpPost.setHeader("Cookie",cookie);
       
        String charSet = "UTF-8";  
        System.out.println("post url"+url);
        StringEntity entity = new StringEntity(JSON.toJSONString(params), charSet);  
        httpPost.setEntity(entity);          
        CloseableHttpResponse response = null;  
          
        try {  
              
            response = httpclient.execute(httpPost);  
            StatusLine status = response.getStatusLine();  
            int state = status.getStatusCode();  
            if (state == HttpStatus.SC_OK) {  
                HttpEntity responseEntity = response.getEntity();  
                String jsonString = EntityUtils.toString(responseEntity);  
                return jsonString;  
            }  
            else{  
                System.out.println("请求返回:"+state+"("+url+")");  
            }  
        }  
        finally {  
            if (response != null) {  
                try {  
                    response.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
	
	
	public static String getYCFile(String urlPath,String charset) throws IOException {  
        String readStr = "";  
        System.out.println("请求url:"+urlPath);
                String strUrl = urlPath.trim();  
                URL url = new URL(strUrl);  
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();  
                urlCon.setConnectTimeout(2000);  
                urlCon.setReadTimeout(2000);  
                urlCon.setRequestMethod("GET");
                urlCon.setUseCaches(true);
                urlCon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
                int code = urlCon.getResponseCode();
                System.out.println("code:"+code);
                BufferedReader in = new BufferedReader(new InputStreamReader(  
                        urlCon.getInputStream(), charset));  
                String inputLine = " ";  
                while ((inputLine = in.readLine()) != null) {  
                    readStr += inputLine.trim();  
                }  
                in.close();  
                System.out.println("请求url:成功");
                return readStr;  
          
    }  
	
	
	public static String post(String filename,HashMap<String, Object> params,String cookie) throws Exception{
		URL url = new URL(filename);
		System.out.println("post url"+filename);
		 
		 //开始访问
		  String postData = JSON.toJSONString(params);
		  
//		  for (Entry<String, Object> param : params.entrySet()) {
//			  if (postData.length() != 0) postData.append('&');
//			  postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//		      postData.append('=');
//		      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//		}
		
		  byte[] postDataBytes = postData.getBytes("UTF-8");
		 
		  HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		  
		  conn.setConnectTimeout(5000);
		  conn.setReadTimeout(5000);
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
		  conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		  conn.setRequestProperty("Cookie",cookie);
		  
		  conn.setDoOutput(true);
		  conn.getOutputStream().write(postDataBytes);
		 
		  Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		 
		  StringBuilder sb = new StringBuilder();
		  for (int c; (c = in.read()) >= 0;)
		      sb.append((char)c);
		  String response = sb.toString();
		
		  System.out.println("post respone"+response);
		  return response; 
	}
	
	public static String post(String filename,HashMap<String, Object> params) throws Exception{
		URL url = new URL(filename);
		System.out.println("post url"+filename);
		 
		 //开始访问
		  StringBuilder postData = new StringBuilder();
		  
		  for (Entry<String, Object> param : params.entrySet()) {
			  if (postData.length() != 0) postData.append('&');
			  postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		      postData.append('=');
		      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
		  byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		 
		  HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		  conn.setConnectTimeout(5000);
		  conn.setReadTimeout(5000);
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		  conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		  conn.setDoOutput(true);
		  conn.getOutputStream().write(postDataBytes);
		  
		  
		 
		  Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		 
		  StringBuilder sb = new StringBuilder();
		  for (int c; (c = in.read()) >= 0;)
		      sb.append((char)c);
		  String response = sb.toString();
		  System.out.println("post respone"+response);
		  
		  return response; 
	}

	

}
