package collect;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;



import com.alibaba.fastjson.JSON;


import util.AESUtil;
import util.Base64Util;
import util.Md5Util;

public class TaoHuaDaoCollect extends Collect {
	
	public TaoHuaDaoCollect() throws Exception {
		super();
	}
	
	public HashMap<String, String> push_urls = new HashMap<>();

	public static String rooms_url = "http://www.hrtrth.com:1340/apichannel/channelList";
	public static String room_pre = "http://www.hrtrth.com:1340/";
	public static String room_detail = "http://www.hrtrth.com:1340/apichannel/getPlayList?channel=";
	public static String pull_url = "http://www.hrtrth.com:1340/apichannel/getroompull";
	public static String login_api = "apilogin";
	public static String phone = "15814443224";
	public static String password = "liangliang";
	public static String cookie="sails.sid=s%3Akc_MvJ3m0Xbt13k8tC89K8ZbJj6M1Pmm.W%2FSnmbt%2FX91d9nn7Mt39YG8UdweFvAH4Bi1ecXJXAq0; Path=/; Expires=Tue, 17 Apr 2018 10:34:11 GMT; HttpOnly";
	String login_md5="ae79177a8e743c70e0f5e0f76cab36c6";
	
	
	
	List<HashMap<String, String>> platform; //平台列表
	HashMap<String, String> pids;//平台索引
	public static String ft = "@mc";
	
	HashMap<String, List<HashMap<String, String>>> rooms = new HashMap<>();
	
	
	
	@Override
	public void init() throws Exception {
		login();
		String s = UrlUtil.getYCFile(rooms_url,"UTF-8");	
		HashMap<String, Object> res = JSON.parseObject(s,new HashMap<String,Object>().getClass());
		List<HashMap<String, Object>> data = 
				(List<HashMap<String, Object>>) JSON.parseArray(res.get("data").toString(),new HashMap<String, Object>().getClass());
		
		
		
		platform = new ArrayList<>();
		pids = new HashMap<>();
		for(int i=0;i<data.size();i++){
			
				String key = Md5Util.MD5(data.get(i).get("id")+"");
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", data.get(i).get("name").toString());
				hashMap.put("key", key);
				hashMap.put("thumb", URLDecoder.decode(data.get(i).get("image").toString()));
				platform.add(hashMap);
				pids.put(key,data.get(i).get("channel").toString());
			
		}
		
		
			
	}
	
	void login() throws Exception{
		if(true) return;
		String url =room_pre+login_api;
		HashMap<String,Object> params = new HashMap<>();
		params.put("user", phone);
		params.put("password", password);
		params.put("channel", "ywt");
		String response = UrlUtil.post(url, params);
		HashMap<String, Object> res = 
				JSON.parseObject(response,new HashMap<String,Object>().getClass());
		login_md5 = res.get("loginMd5").toString();
		
		
		
	}

	@Override
	public List<HashMap<String, String>> getPlatform() {
		return platform;
	}

	@Override
	public List<HashMap<String, String>> getRooms(String key) throws Exception {
		if( rooms.containsKey(key) && rooms.get(key)!=null && rooms.get(key).size()>0) return rooms.get(key);
		else{
			List<HashMap<String, String>> r=getRoom(key);
			rooms.put(key, r);
			return r;
		}
		
		
	}
	
	List<HashMap<String, String>> getRoom(String key) throws Exception{
		if(!pids.containsKey(key)) return new ArrayList<>();
		String pid = pids.get(key);
		String url = room_detail+pid;
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("loginMd5", login_md5);
		long tp = System.currentTimeMillis()/1000;
		params.put("timestamp", tp);
		params.put("sign", Md5Util.MD5(tp+""+login_md5+"1234"));
		
		
		String s = UrlUtil.doPost(url, params,cookie);
		String reString=AESUtil.decode(s, login_md5.substring(0, 16));
		
		HashMap<String, Object> ret = JSON.parseObject(reString,new HashMap<String,Object>().getClass());
		
		HashMap<String, Object> data = JSON.parseObject(ret.get("data").toString(),new HashMap<String,Object>().getClass());
		
		List<HashMap<String, Object>> list = 
				(List<HashMap<String, Object>>) 
				JSON.parseArray(data.get("list").toString(),new HashMap<String,Object>().getClass());
		
		
		List<HashMap<String, String>> rs = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			
				
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", list.get(i).get("title").toString());
				hashMap.put("push_url", "-1");
				hashMap.put("uid", list.get(i).get("uid").toString());
				hashMap.put("channel", pid);
				hashMap.put("thumb",list.get(i).get("avatar_thumb").toString());
				rs.add(hashMap);
				
				
			
		}
		return rs;
		
	}

	@Override
	public void intveral_task() {
		rooms = new HashMap<>();
	
		System.out.println("清除房间所有信息");
	}

	@Override
	public String getPull(String id,String channel) throws Exception {
		String key = channel+"_"+id;
		if(push_urls.containsKey(key) && push_urls.get(key)!=null && !push_urls.get(key).equals("")){
			return push_urls.get(key);
		}
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("loginMd5", login_md5);
		long tp = System.currentTimeMillis()/1000;
		params.put("timestamp", tp);
		params.put("sign", Md5Util.MD5(tp+""+login_md5+"1234"));
		params.put("channel", channel);
		params.put("room_uid", id);
		String s = UrlUtil.doPost(pull_url, params,cookie);
		String reString=AESUtil.decode(s, login_md5.substring(0, 16));
		HashMap<String, Object> res=JSON.parseObject(reString,new HashMap<String,Object>().getClass());
		if((int)res.get("ret")==-1) return "-1";
		String pull =  res.get("pull").toString();
		push_urls.put(key, pull);
		return pull;
	}

	@Override
	public HashMap<String, String> getRoomInfo(String key) {
		for(int i=0;i<platform.size();i++){
			if(platform.get(i).get("key").equals(key)) return platform.get(i);
		}
		return null;
	}

}
