package controller.v1;

import java.util.ArrayList;
import java.util.HashMap;


import com.alibaba.fastjson.JSON;

import annotation.action;
import server.Controller;
import server.ControllerContext;
import util.Md5Util;
import util.RandomUtil;
import util.TimeUtil;

public class StudentController extends Controller{

	public StudentController(ControllerContext context) {
		super(context);
	}
	
	@action
	public void test() {
		String aString = I("s").toString();
		success(aString);
		
	}

	@action
	public void list() {
		toHtml("admin_tpl/student_list");
	}
	
	@action
	public void getList() {
		String page=I("get.page").toString();
		String limit = Integer.parseInt(page)*10+",10";
		HashMap<String, Object> res = new HashMap<>();
		int num = M("student").count();
		String sql = "select a.*,b.name as clazz_name from student a left join clazz b on a.clazz_id = b.id limit "+limit;
		ArrayList<HashMap<String, String>> list = M("student").query(sql);
		res.put("num", num);
		res.put("list",list);
		success(res);	
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
		String name,sid,clazz_id;
		try {
			name=I("post.name").toString();
			sid=I("post.sid").toString();
			clazz_id=I("post.clazz_id").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		HashMap<String, String> data=new HashMap<>();
		data.put("name", name);
		data.put("sid",sid);
		data.put("clazz_id", clazz_id);
		try {
			M("student").add(data);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
		

	
	@action
	public void remove() {
		String id=I("get.id").toString();
		M("student").where("id="+id).delete();
		success(1);
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
		String id,name,sid,clazz_id;
		try {
			id=I("post.id").toString();
			name=I("post.name").toString();
			sid=I("post.sid").toString();
			clazz_id=I("post.clazz_id").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		HashMap<String, String> data=new HashMap<>();
		data.put("name", name);
		data.put("sid",sid);
		data.put("clazz_id", clazz_id);
		try {
			M("student").where("id="+id).save_string(data);
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
			long id = M("user").add(user);
			HashMap<String, Object> sHashMap = new HashMap<>();
			sHashMap.put("uuid", Md5Util.MD5(id+RandomUtil.getRandomString(10)));
			M("user").where("id="+id).save(sHashMap);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
	
}
