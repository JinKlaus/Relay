package controller.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.server.Authentication.User;

import com.mongodb.Function;


import collect.CollectManager;
import collect.YeMeiCollect;
import server.Controller;
import server.ControllerContext;
import annotation.action;

import util.Md5Util;
import util.TimeUtil;

public class IndexController extends Controller {

	public IndexController(ControllerContext context) {
		super(context);
	}
	
	
	@action 
	public void index(){
		
		success(CollectManager.collect.getPlatform());
		
	}
	
	boolean is_vip(){
		HashMap<String, String> user = getUser();
		if(user==null) return false;
		long vip_time = Long.parseLong(user.get("vip_time"));
		if(vip_time>TimeUtil.getShortTimeStamp()) return true;
		else return false;
		
	}
	
	@action 
	public void get_room(){
		if(!is_vip()) {
			error("请先充值vip");
			return;
		}
		
		String key = (String) I("key");
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			list=CollectManager.collect.getRooms(key);
		} catch (Exception e) {
			
				try {
					list=CollectManager.collect.getRooms(key);
				} catch (Exception e1) {
					list=new ArrayList<>();
				}
			
		}
		
		HashMap<String, Object> res = new HashMap<>();
		res.put("list", list);
		res.put("info", CollectManager.collect.getRoomInfo(key));
		success(res);
		
	}
	
	@action
	public void get_push(){
		if(!is_vip()) {
			error("请先充值vip");
			return;
		}
		String id = I("uid").toString();
		String channel = I("channel").toString();
		try {
			success(CollectManager.collect.getPull(id, channel));
		} catch (Exception e) {
			if(e instanceof SocketTimeoutException){
				try {
					success(CollectManager.collect.getPull(id, channel));
				} catch (Exception e1) {
					success("");
				}
			}
		}
	}
	
	@action
	public void register(){
		String phone = I("phone").toString();
		String password = I("password").toString();
		String code = I("code").toString();
		
		if(phone.length()!=11) {
			error("手机号码不正确");
			return;
		}
		
		if(password.length()<6) {
			error("密码至少6位");
			return;
		}
		
		int from_id=0;
		if(!code.equals("10000")){
			HashMap<String, String> user = M("user").where("code="+code).find();
			if(user==null){
				error("不存在该邀请码");
				return;
			}
			from_id = Integer.parseInt(user.get("id"));
		}
		
		HashMap<String, String> user = M("user").where("phone="+phone).find();
		if(user!=null){
			error("该手机号码已经注册");
			return;
		}
		int vip_time = TimeUtil.getShortTimeStamp()+60*15;
		HashMap<String, String> res = new HashMap<>();
		res.put("phone", phone);
		res.put("password", password);
		res.put("from_id", from_id+"");
		res.put("create_time",TimeUtil.getShortTimeStamp()+"");
		res.put("vip_time",vip_time+"");
		
		try {
			M("user").add(res);
			
			
			
			success("注册成功");
		} catch (Exception e) {
			e.printStackTrace();
			error("注册失败");
		}
		
		
		
	}
	
	@action
	public void login(){
		String phone = I("phone").toString();
		String password = I("password").toString();
		
		HashMap<String, String> user = 
				M("user").where("phone ='"+phone+"' and password ='"+password+"'").find();
		
		if(user==null) {
			error("账号密码错误");
			return;
		}
		String token = Md5Util.MD5(phone+TimeUtil.getTime());
		
		M("user").where("id="+user.get("id")).save(new HashMap<String,Object>(){
			{
				put("token",token );
			}
		});
		
		success(token);
		
	}
	
	HashMap<String, String> getUser(){
		String token;
		try {
			 token = I("token").toString();
		} catch (Exception e) {
			token=null;
		}
		
		if(token==null || token.equals("")){
			return null;
		}
		HashMap<String, String> user = M("user").where("token='"+token+"'").find();
		return user;
	}
	
	@action
	public void get_my(){
		HashMap<String, String> user = getUser();
		if(user==null){
			error("请重新登录 ");
			return;
		}
		
		success(user);
	}
	
	@action
	public void pass(){
		HashMap<String, String> user = getUser();
		if(user==null){
			error("请重新登录 ");
			return;
		}
		String old = I("old").toString();
		String password = I("password").toString();
		if(!old.equals(user.get("password"))){
			error("旧密码不正确");
			return;
		}
		M("user").where("id="+user.get("id")).save(new HashMap<String,Object>(){
			{
				put("password", password);
			}
		});
		
		success(1);
		
	}
	
	@action
	public void tovip(){
		HashMap<String, String> user = getUser();
		if(user==null){
			error("请重新登录 ");
			return;
		}
		String card = I("card").toString();
		HashMap<String, String> cinfo = M("card").where("is_use=0 and card_num='"+card+"'").find();
		if(cinfo==null){
			error("不存在该卡号或者卡号已经被使用");
			return;
		}
		long ext_time = 0;
		int type = Integer.parseInt(cinfo.get("type"));
		switch (type) {
		case 1:
			ext_time+=7*3600*24;
			break;
		case 2:
			ext_time+=30*3600*24;
			break;
		case 3:
			ext_time+=3*30*3600*24;
			break;
		case 4:
			ext_time+=365*3600*24;
			break;
		case 5:
			ext_time+=50*365*3600*24;
			break;

		}
		final long etime= ext_time;
		M("user").where("id="+user.get("id")).save(new HashMap<String,Object>(){
			{
				put("vip_time", Long.parseLong(user.get("vip_time"))+etime);
			}
		});
		
		M("card").where("id="+cinfo.get("id")).save(new HashMap<String,Object>(){
			{
				put("is_use", 1);
			}
		});
		
		HashMap<String, String> auser = new HashMap<>();
		auser.put("card_id", cinfo.get("id")+"");
		auser.put("uid", user.get("id")+"");
		auser.put("create_time",TimeUtil.getShortTimeStamp()+"");
		
		try {
			M("card_user").add(auser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		success(1);
		
	}
	
	String get_dic(String k){
		HashMap<String, String> dic = M("dic").where("k='"+k+"'").find();
		return dic.get("v");
	}
	
	
	@action
	public void invite(){
		HashMap<String, String> user = getUser();
		if(user==null){
			error("请重新登录 ");
			return;
		}
		
	
		String code = user.get("code");
		if(code.equals("0")){
			int c=10000+Integer.parseInt(user.get("id"));
			code = c+"";
			final String scode = code;
			M("user").where("id="+user.get("id")).save(new HashMap<String,Object>(){
				{
					put("code",scode);
				}
			});
			
			
		}
		
		HashMap<String, String> res = new HashMap<>();
		res.put("tg_text", get_dic("tg_text"));
		res.put("code", code);
		res.put("apk_url", get_dic("apk_url"));
		success(res);
		
	}
	
	
	
	
	

}
