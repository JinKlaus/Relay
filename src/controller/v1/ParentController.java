package controller.v1;

import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import annotation.action;
import config.Dictionary;
import server.Controller;
import server.ControllerContext;
import util.Md5Util;
import util.RandomUtil;
import util.TimeUtil;

public class ParentController extends Controller{

	public ParentController(ControllerContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@action
	public void list() {
		toHtml("admin_tpl/parent_list");
	}
	
	@action
	public void getList() {
		String page=I("get.page").toString();
		String limit = Integer.parseInt(page)*10+",10";
		HashMap<String, Object> res = new HashMap<>();
		int num = M("parent").count();
		String sql = "select a.*,c.name as stu_name,b.name as clazz_name from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id limit "+limit;
		ArrayList<HashMap<String, String>> list = M("parent").query(sql);
		res.put("num", num);
		res.put("list",list);
		success(res);	
	}
	
	@action
	public void add() {
		toHtml("admin_tpl/parent_crud");
	}
	
	@action
	public void do_add() {
		String veinData1,veinData2,veinData3,stu_id,par_name,par_photo,address,tel;
		try {
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			stu_id=I("post.stu_id").toString();
			par_photo=I("post.par_photo").toString();
			par_name=I("post.par_name").toString();
			address=I("post.address").toString();
			tel=I("post.tel").toString();
		} catch (Exception e) {
			error("0");
			return;
		}
		ArrayList<HashMap<String, String>> list = M("clazz").query("select a.*,b.id as stu_id from clazz a left join student b on a.id=b.clazz_id where b.id="+stu_id);
		HashMap<String, String> date=list.get(0);
		String startDate=date.get("start_date");
		String endDate=date.get("end_date");
		HashMap<String,String> par=new HashMap<>();
		HashMap<String,String> user=new HashMap<>();
		user.put("cardNo","1");
		user.put("startDate",startDate);
		user.put("endDate",endDate);
		user.put("name",par_name);
		user.put("personType",Dictionary.PARENT+"");
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		user.put("create_time",TimeUtil.getShortTimeStamp()+"");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		user.put("state",Dictionary.STATE_ADD+"");
		user.put("scenePhoto",par_photo);
		user.put("passType","6");
		par.put("stu_id",stu_id);
		par.put("par_name",par_name);
		par.put("par_photo",par_photo);
		par.put("address",address);
		par.put("tel", tel);
		try {
			long id = M("user").add(user);
			HashMap<String, Object> sHashMap = new HashMap<>();
			sHashMap.put("uuid", Md5Util.MD5(id+RandomUtil.getRandomString(10)));
			M("user").where("id="+id).save(sHashMap);
			par.put("uid", id+"");
			M("parent").add(par);
			success("数据库更新成功");
		} catch (Exception e) {
			error("0");
		}
	}
	
	@action
	public void remove() {
		String id=I("get.id").toString();
		String sql="select uid from parent where id="+id;
		ArrayList<HashMap<String, String>> list = M("parent").query(sql);
		HashMap<String, String> map = list.get(0);
		HashMap<String, String> data =new HashMap<>(); 
		String uid=map.get("uid");
		data.put("state", "0");
		data.put("update_time",TimeUtil.getLongTimeStamp()+"");
		try {
			M("parent").where("id="+id).delete();
			M("user").where("id="+uid).save_string(data);
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}
	
	@action
	public void edit_info() {
		String id=I("get.id").toString();
		String sql="select a.*,b.id as clazz_id from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id where a.id="+id;
		ArrayList<HashMap<String, String>> list = M("parent").query(sql);
		HashMap<String, String> res = list.get(0);
		assign("info", JSON.toJSON(res));
		toHtml("admin_tpl/parent_updateInfo");
	}
	
	@action
	public void edit_vena() {
		String id=I("get.id").toString();
		String sql="select a.*,b.id as clazz_id from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id where a.id="+id;
		ArrayList<HashMap<String, String>> list = M("parent").query(sql);
		HashMap<String, String> res = list.get(0);
		assign("info", JSON.toJSON(res));
		toHtml("admin_tpl/parent_updateVena");
	}
	
	@action
	public void do_edit_info() {
		String  stu_id,par_name,address,tel,uid; 
		try {
			stu_id=I("post.stu_id").toString();
			par_name=I("post.par_name").toString();
			address=I("post.address").toString();
			tel=I("post.tel").toString();
			uid=I("post.uid").toString();
		} catch (Exception e) {
			error("0");
			return;
		}	
		HashMap<String,String> par=new HashMap<>();
		HashMap<String,String> user=new HashMap<>();
		par.put("par_name",par_name);
		par.put("address", address);
		par.put("tel",tel);
		user.put("name",par_name);
		user.put("state", "2");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		try {
			M("parent").where("stu_id="+stu_id).save_string(par);
			M("user").where("id="+uid).save_string(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据库更新失败");
		}
	}
	
	@action
	public void do_edit_vena() {
		String veinData1,veinData2,veinData3,uid;
		try {
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			uid=I("post.uid").toString();
		} catch (Exception e) {
			error("0");
			return;
		}
		HashMap<String,String> user=new HashMap<>();
		user.put("state", "2");
		user.put("update_time",TimeUtil.getLongTimeStamp()+"");
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		try {
			M("user").where("id="+uid).save_string(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据库更新失败");
		}
		
	} 
	
	@action
	public void getClazzInfo() {
		String sql="select id,name from clazz";
		ArrayList<HashMap<String, String>> list = M("clazz").query(sql);
		success(list);
	}
	
	@action
	public  void getStudentInfo() {
		String clazz_id=I("get.clazz_id").toString();
		String sql="select id,name from student where clazz_id="+clazz_id;
		ArrayList<HashMap<String, String>> list = M("student").query(sql);
		success(list);
	} 

}
