package collect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;



import util.Base64Util;
import util.Md5Util;

public class JinHuLuCollect extends Collect {
	
	public JinHuLuCollect() throws Exception {
		super();
	}

	public static String rooms_url = "http://61.160.194.167/1/ptxx.txt";
	public static String room_pre = "http://61.160.194.167/1/";
	
	List<HashMap<String, String>> platform; //平台列表
	HashMap<String, String> pids;//平台索引
	public static String ft = "@mc";
	HashMap<String, List<HashMap<String, String>>> rooms = new HashMap<>();
	
	@Override
	public void init() throws Exception {
		String s = UrlUtil.getYCFile(rooms_url);	
		s=Base64Util.decodeString(s);	
		
		String ss[] = s.split("\\r\\n");
		platform = new ArrayList<>();
		pids = new HashMap<>();
		for(int i=0;i<ss.length;i++){
			String[] tt = ss[i].split("\\|");
			if(tt.length>=3){
				
				String s_name = tt[0].replace("name:", "");
				String[] a_name = s_name.split("\\[");
				String name = a_name[0];
				String key = Md5Util.MD5(name);
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", s_name);
				hashMap.put("key", key);
				hashMap.put("thumb", tt[2].replace("img:", ""));
				platform.add(hashMap);
				pids.put(key,s_name);
			}
		}
		
		 rooms = new HashMap<>();
			
	}

	@Override
	public List<HashMap<String, String>> getPlatform() {
		return platform;
	}

	@Override
	public List<HashMap<String, String>> getRooms(String key) throws Exception {
		if( rooms.containsKey(key) && rooms.get(key)!=null) return rooms.get(key);
		else{
			List<HashMap<String, String>> r=getRoom(key);
			rooms.put(key, r);
			return r;
		}
		
		
	}
	
	List<HashMap<String, String>> getRoom(String key) throws IOException{
		if(!pids.containsKey(key)) return new ArrayList<>();
		String pid = pids.get(key);
		String url = room_pre+pid+".txt";
		
		String s = UrlUtil.getYCFile(url);
		try {
			s=Base64Util.decodeString(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String ss[] = s.split("\\r\\n");
		List<HashMap<String, String>> rs = new ArrayList<>();
		for(int i=0;i<ss.length;i++){
			String[] tt = ss[i].split("\\|");
			if(tt.length>=3){
				if(!tt[1].replace("url:", "").equals("")){
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", tt[0].replace("name:", ""));
				hashMap.put("push_url", tt[1].replace("url:", ""));
				hashMap.put("thumb", tt[2].replace("img:", ""));
				rs.add(hashMap);
				}
				
			}
		}
		return rs;
		
	}

	@Override
	public void intveral_task() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("清除房间所有信息");
	}

	@Override
	public String getPull(String id,String channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getRoomInfo(String key) {
		for(int i=0;i<platform.size();i++){
			if(platform.get(i).get("key").equals(key)) return platform.get(i);
		}
		return null;
	}
	

}
