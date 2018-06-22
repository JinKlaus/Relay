package controller.v1;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.xwpf.usermodel.UpdateEmbeddedDoc;

import com.alibaba.fastjson.JSON;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.org.apache.bcel.internal.generic.NEW;

import annotation.action;
import server.Controller;
import server.ControllerContext;
import util.TimeUtil;

public class ClazzController extends Controller{

	public ClazzController(ControllerContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@action
	public void add() {
		assign("clazz", false);
		toHtml("admin_tpl/clazz_crud");
	}
	
	@action
	public void do_add() throws ParseException {
		String name,start_Date,end_Date;
		try {
			name=I("post.name").toString();
			start_Date=I("post.start_Date").toString();
			end_Date=I("post.end_Date").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		HashMap<String, String> clazz=new HashMap<>();
		clazz.put("name",name);
		clazz.put("create_time",TimeUtil.getShortTimeStamp()+"");
		clazz.put("start_date",TimeUtil.dateToStamp(start_Date, "yyyy-MM"));
		clazz.put("end_date",TimeUtil.dateToStamp(end_Date, "yyyy-MM"));
		try {
			M("clazz").add(clazz);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
	
	@action
	public void list() {
		toHtml("admin_tpl/clazz_list");
	}
	
	@action
	public void getList() {
		String page=I("get.page").toString();
		String limit = Integer.parseInt(page)*10+",10";
		HashMap<String, Object> res = new HashMap<>();
		int num = M("clazz").count();
		ArrayList<HashMap<String, String>> list = M("clazz").limit(limit).select();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("create_time", TimeUtil.stampToMysqlDate(list.get(i).get("create_time").toString()));
			list.get(i).put("start_date", TimeUtil.stampToDate(list.get(i).get("start_date").toString(), "yyyy-MM"));
			list.get(i).put("end_date", TimeUtil.stampToDate(list.get(i).get("end_date").toString(), "yyyy-MM"));
		}
		res.put("num", num);
		res.put("list",list);
		success(res);	
	}
	
	@action
	public void remove() {
		String id=I("get.id").toString();
		M("clazz").where("id="+id).delete();
		success(1);
	}
	
	@action
	public void edit() {
		String id=I("get.id").toString();
		HashMap<String, String> res = M("clazz").where("id="+id).find();
		String start_date=TimeUtil.stampToDate(res.get("start_date"), "yyyy-MM");
		String end_date=TimeUtil.stampToDate(res.get("end_date"), "yyyy-MM");
		String startdate[]=start_date.split("-");
		String start_year=startdate[0];
		String start_month=startdate[1];
		String enddate[]=end_date.split("-");
		String end_year=enddate[0];
		String end_month=enddate[1];
		res.put("start_year", start_year);
		res.put("start_month", start_month);
		res.put("end_year", end_year);
		res.put("end_month", end_month);
		assign("clazz", JSON.toJSON(res));
		toHtml("admin_tpl/clazz_crud");
	}
	
	@action
	public void do_edit() throws ParseException {
		String id,name,start_Date,end_Date;
		try {
			id=I("post.id").toString();
			name=I("post.name").toString();
			start_Date=I("post.start_Date").toString();
			end_Date=I("post.end_Date").toString();
		} catch (Exception e) {
			error("参数提交错误");
			return;
		}
		HashMap<String, String> data=new HashMap<>();
		data.put("name",name);
		data.put("start_date",TimeUtil.dateToStamp(start_Date, "yyyy-MM"));
		data.put("end_date",TimeUtil.dateToStamp(end_Date, "yyyy-MM"));
		try {
			M("clazz").where("id="+id).save_string(data);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据加载到数据库失败");
		}
	}
}
