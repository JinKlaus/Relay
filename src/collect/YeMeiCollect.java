package collect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import util.Md5Util;

public class YeMeiCollect extends Collect {
	
	public YeMeiCollect() throws Exception {
		super();
	}

	public static String rooms_url = "http://jk.cfb800.com/1/yuncaidan.txt";
	public static String room_pre = "http://jk.cfb800.com/1/";
	
	List<HashMap<String, String>> platform; //平台列表
	HashMap<String, String> pids;//平台索引
	public static String ft = "@mc";
	HashMap<String, List<HashMap<String, String>>> rooms = new HashMap<>();
	
	@Override
	public void init() throws IOException {
		String s = UrlUtil.getYCFile(rooms_url);
		String ss[] = s.split(ft);
		platform = new ArrayList<>();
		pids = new HashMap<>();
		for(int i=0;i<ss.length;i++){
			String[] tt = ss[i].split("\\|");
			if(tt.length>=3){
				String key = Md5Util.MD5(ss[i]);
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", tt[0]);
				hashMap.put("key", key);
				hashMap.put("thumb", tt[1].replace("@tp", ""));
				platform.add(hashMap);
				pids.put(key,tt[2].replace("@dz", ""));
			}
		}
		
		
			
	}

	@Override
	public List<HashMap<String, String>> getPlatform() {
		return platform;
	}

	@Override
	public List<HashMap<String, String>> getRooms(String key) throws IOException {
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
		String url = room_pre+pid;
		
		String s = UrlUtil.getYCFile(url);
		String ss[] = s.split(ft);
		List<HashMap<String, String>> rs = new ArrayList<>();
		for(int i=0;i<ss.length;i++){
			String[] tt = ss[i].split("\\|");
			if(tt.length>=3){
				if(!tt[1].replace("@dz", "").equals("")){
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", tt[0]);
				hashMap.put("push_url", tt[2].replace("@dz", ""));
				hashMap.put("thumb", tt[1].replace("@tp", ""));
				rs.add(hashMap);
				}
				
			}
		}
		return rs;
		
	}

	@Override
	public void intveral_task() {
		rooms = new HashMap<>();
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
