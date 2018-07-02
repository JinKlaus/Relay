package controller.v1;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;


import com.alibaba.fastjson.JSON;

import annotation.action;
import config.Dictionary;
import server.Controller;
import server.ControllerContext;
import util.TimeUtil;

public class StudentController extends Controller{

	public StudentController(ControllerContext context) {
		super(context);
	}
	

	@action
	public void list() {
		toHtml("admin_tpl/student_list");
	}
	
	@SuppressWarnings("deprecation")
	@action
	public void getList() {
		String page=I("get.page").toString();
		String limit = Integer.parseInt(page)*10+",10";
		HashMap<String, Object> res = new HashMap<>();
		try {
			String key=URLDecoder.decode(I("get.key").toString());
			String sql1="SELECT count(*) FROM student where name like '%"+key+"%'";
			String sql="SELECT a.*,b.name as clazz_name from student a left join clazz b on a.clazz_id = b.id where a.name like '%"+key+"%' limit "+limit;
			ArrayList<HashMap<String, String>> list1=M("student").query(sql1);
			String num=list1.get(0).get("count(*)");
			ArrayList<HashMap<String, String>> list = M("student").query(sql);
			res.put("num", num);
			res.put("list",list);
			success(res);
			return;
		} catch (Exception e) {
			int num = M("student").count();
			String sql = "select a.*,b.name as clazz_name from student a left join clazz b on a.clazz_id = b.id limit "+limit;
			ArrayList<HashMap<String, String>> list = M("student").query(sql);
			res.put("num", num);
			res.put("list",list);
			success(res);
			return;
		}
	}
	
	@action
	public  void getClazzInfo() {
		String sql="select id,name from clazz";
		ArrayList<HashMap<String, String>> list = M("clazz").query(sql);
		success(list);
	}
	
	@action
	public void add() {
		assign("stu", false);
		toHtml("admin_tpl/student_crud");
	}
	
	@action
	public void do_add() {
		String name,sid,clazz_id,veinData1,veinData2,veinData3;
		try {
			name=I("post.name").toString();
			sid=I("post.sid").toString();
			clazz_id=I("post.clazz_id").toString();
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		ArrayList<HashMap<String, String>> list = M("clazz").query("select a.*,b.id as stu_id from clazz a left join student b on a.id=b.clazz_id where a.id="+clazz_id);
		HashMap<String, String> date=list.get(0);
		String startDate=date.get("start_date");
		String endDate=date.get("end_date");
		HashMap<String, String> data=new HashMap<>();
		HashMap<String,String> user=new HashMap<>();
		data.put("name", name);
		data.put("sid",sid);
		data.put("clazz_id", clazz_id);
		data.put("create_time",TimeUtil.getShortTimeStamp()+"");
		user.put("cardNo",sid);
		user.put("startDate",startDate);
		user.put("endDate",endDate);
		user.put("name",name);
		user.put("personType",Dictionary.STUDENT+"");
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		user.put("create_time",TimeUtil.getShortTimeStamp()+"");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		user.put("state",Dictionary.STATE_ADD+"");
		user.put("passType","6");
		try {
			long id=M("user").add(user);
			data.put("uid", id+"");
			M("student").add(data);
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}
		

	
	@action
	public void remove() {
		String id=I("get.id").toString();
		String sql="select uid from student where id="+id;
		ArrayList<HashMap<String, String>> list = M("student").query(sql);
		HashMap<String, String> map = list.get(0);
		HashMap<String, String> data =new HashMap<>(); 
		String uid=map.get("uid");
		data.put("state", "0");
		data.put("update_time",TimeUtil.getLongTimeStamp()+"");
		try {
			M("student").where("id="+id).delete();
			M("user").where("id="+uid).save_string(data);
			success("1");
		} catch (Exception e) {
			error("0");
		}
		
	}
	
	@action
	public void edit() {
		String id=I("get.id").toString();
		HashMap<String, String> res = M("student").where("id="+id).find();
		assign("stu", JSON.toJSON(res));
		toHtml("admin_tpl/student_crud");
	}
	
	@action
	public void do_edit() {
		String id,name,sid,clazz_id,veinData1,veinData2,veinData3,uid;
		try {
			id=I("post.id").toString();
			name=I("post.name").toString();
			sid=I("post.sid").toString();
			clazz_id=I("post.clazz_id").toString();
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			uid=I("post.uid").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		HashMap<String, String> data=new HashMap<>();
		HashMap<String, String> user=new HashMap<>();
		data.put("name", name);
		data.put("sid",sid);
		data.put("clazz_id", clazz_id);
		user.put("name",name);
		user.put("state", "2");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		user.put("veinData1",veinData1);
		user.put("veinData2",veinData2);
		user.put("veinData3",veinData3);
		try {
			M("student").where("id="+id).save_string(data);
			M("user").where("id="+uid).save_string(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
	
	
	@action
	public void add_vena() {
		toHtml("admin_tpl/student_form");
	}
	
	@action
	public void do_add_vena() {
		String veinData1,veinData2,veinData3,name,personType,scenePhoto;
		try {
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			name=I("post.name").toString();
			personType=I("post.personType").toString();
			scenePhoto=I("post.scenePhoto").toString();
		} catch (Exception e) {
			error("参数提交错误,请进行指静脉建模");
			return;
		}
		HashMap<String, String> user=new HashMap<>();
		user.put("cardNo","1");
		user.put("startDate",TimeUtil.getShortTimeStamp()+"");
		user.put("endDate",TimeUtil.getShortTimeStamp()+"");
		user.put("name",name);
		user.put("personType",personType);
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		user.put("create_time",TimeUtil.getShortTimeStamp()+"");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		user.put("state","1");
		user.put("scenePhoto",scenePhoto);
		user.put("passType","6");
		try {
			 M("user").add(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
	
}
