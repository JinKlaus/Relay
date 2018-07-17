package controller.v1;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.Md5Util;
import util.TimeUtil;

/**
 * @Description 教师管理
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class TeacherController extends AdminController {

	public TeacherController(ControllerContext context) {
		super(context);
		if (admin_type < ADMIN) {
			pri = false;
			return;
		}
	}

	@action
	public void getClazzInfo() {
		String sql = "select id,name from clazz";
		ArrayList<HashMap<String, String>> list = M("clazz").query(sql);
		success(list);
	}

	@action
	public void list() {
		toHtml("admin_tpl/teacher_list");
	}

	@action
	public void getList() {
		String page = I("get.page") == "" ? "" : I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		HashMap<String, Object> res = new HashMap<>();
		@SuppressWarnings("deprecation")
		String key = I("get.key") == null || I("get.key").equals("") ? null
				: URLDecoder.decode(I("get.key").toString());
		try {
			if (key != null) {
				String sql1 = "SELECT count(*),a.*,b.name as clazz_name from teacher a LEFT JOIN clazz b  ON a.clazz_id=b.id where a.name LIKE '%"
						+ key + "%'";
				String sql = "SELECT count(*),a.*,b.name as clazz_name from teacher a LEFT JOIN clazz b  ON a.clazz_id=b.id where a.name LIKE '%"
						+ key + "%' limit " + limit;
				ArrayList<HashMap<String, String>> list1 = M("teacher").query(sql1);
				String num = list1.get(0).get("count(*)");
				ArrayList<HashMap<String, String>> list = M("teacher").query(sql);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).get("isadmin").equals("0")) {
						list.get(i).put("isadmin", "普通教工");
					} else if (list.get(i).get("isadmin").equals("1")) {
						list.get(i).put("isadmin", "审核教工");
					} else if (list.get(i).get("isadmin").equals("2")) {
						list.get(i).put("isadmin", "值班教工");
					} else if (list.get(i).get("isadmin").equals("3")) {
						list.get(i).put("isadmin", "管理员");
					}
				}
				res.put("num", num);
				res.put("list", list);
				success(res);
			} else {
				int num = M("teacher").count();
				String sql = "SELECT a.*,b.name as clazz_name from teacher a LEFT JOIN clazz b  ON a.clazz_id=b.id limit "
						+ limit;
				ArrayList<HashMap<String, String>> list = M("teacher").query(sql);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).get("isadmin").equals("0")) {
						list.get(i).put("isadmin", "普通教工");
					} else if (list.get(i).get("isadmin").equals("1")) {
						list.get(i).put("isadmin", "审核教工");
					} else if (list.get(i).get("isadmin").equals("2")) {
						list.get(i).put("isadmin", "值班教工");
					} else if (list.get(i).get("isadmin").equals("3")) {
						list.get(i).put("isadmin", "管理员");
					}
				}
				res.put("num", num);
				res.put("list", list);
				success(res);
			}
			return;
		} catch (Exception e) {
			error("0");
		}
	}

	@action
	public void add() {
		toHtml("admin_tpl/teacher_crud");
	}

	@action
	public void do_add() {
		String veinData1, veinData2, veinData3, tea_name, tid, clazz_id, tel, address, original_pwd, isadmin;
		try {
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			tea_name = I("post.tea_name").toString();
			tid = I("post.tid").toString();
			clazz_id = I("post.clazz_id").toString();
			tel = I("post.tel").toString();
			address = I("post.address").toString();
			original_pwd = I("post.original_pwd").toString();
			isadmin = I("post.isadmin").toString();
		} catch (Exception e) {
			error("0");
			return;
		}
		ArrayList<HashMap<String, String>> list = M("clazz").query(
				"select a.*,b.id as stu_id from clazz a left join student b on a.id=b.clazz_id where a.id=" + clazz_id);
		HashMap<String, String> date = list.get(0);
		String startDate = date.get("start_date");
		String endDate = date.get("end_date");
		HashMap<String, String> tea = new HashMap<>();
		HashMap<String, String> user = new HashMap<>();
		user.put("startDate", startDate);
		user.put("endDate", endDate);
		user.put("name", tea_name);
		user.put("personType", Dictionary.TEACHER + "");
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		user.put("create_time", TimeUtil.getShortTimeStamp() + "");
		user.put("update_time", TimeUtil.getLongTimeStamp() + "");
		user.put("state", Dictionary.STATE_ADD + "");
		user.put("passType", "11111");
		tea.put("name", tea_name);
		tea.put("tid", tid);
		tea.put("clazz_id", clazz_id);
		tea.put("address", address);
		tea.put("tel", tel);
		tea.put("original_pwd", Md5Util.MD5(original_pwd));
		tea.put("isadmin", isadmin);
		tea.put("create_time", TimeUtil.getShortTimeStamp() + "");
		try {
			long id = M("user").add(user);
			tea.put("uid", id + "");
			long id1=M("teacher").add(tea);
			user.put("cardNo",id1+"");
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}

	@action
	public void remove() {
		String id = I("get.id").toString();
		String sql = "select uid from teacher where id=" + id;
		ArrayList<HashMap<String, String>> list = M("teacher").query(sql);
		HashMap<String, String> map = list.get(0);
		HashMap<String, String> data = new HashMap<>();
		String uid = map.get("uid");
		data.put("state", "0");
		data.put("update_time", TimeUtil.getLongTimeStamp() + "");
		try {
			M("teacher").where("id=" + id).delete();
			M("user").where("id=" + uid).save_string(data);
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}

	@action
	public void edit_info() {
		String id = I("get.id").toString();
		HashMap<String, String> res = M("teacher").where("id=" + id).find();
		assign("info", JSON.toJSON(res));
		toHtml("admin_tpl/teacher_updateInfo");
	}

	@action
	public void do_edit_info() {
		String id, tid, tea_name, address, tel, clazz_id, uid, isadmin, origial_pwd;
		try {
			id = I("post.id").toString();
			tid = I("post.tid").toString();
			tea_name = I("post.tea_name").toString();
			address = I("post.address").toString();
			tel = I("post.tel").toString();
			clazz_id = I("post.clazz_id").toString();
			uid = I("post.uid").toString();
			isadmin = I("post.isadmin").toString();
			origial_pwd = I("post.original_pwd").toString();
		} catch (Exception e) {
			error("0");
			return;
		}
		HashMap<String, String> tea = new HashMap<>();
		HashMap<String, String> user = new HashMap<>();
		tea.put("name", tea_name);
		tea.put("address", address);
		tea.put("tel", tel);
		tea.put("uid", uid);
		tea.put("clazz_id", clazz_id);
		tea.put("tid", tid);
		tea.put("isadmin", isadmin);
		tea.put("original_pwd", Md5Util.MD5(origial_pwd));
		user.put("name", tea_name);
		user.put("state", "2");
		user.put("update_time", TimeUtil.getLongTimeStamp() + "");
		try {
			M("teacher").where("id=" + id).save_string(tea);
			M("user").where("id=" + uid).save_string(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据库更新失败");
		}
	}

	@action
	public void edit_vena() {
		String id = I("get.id").toString();
		HashMap<String, String> res = M("teacher").where("id=" + id).find();
		assign("info", JSON.toJSON(res));
		toHtml("admin_tpl/teacher_updateVena");
	}

	@action
	public void do_edit_vena() {
		String veinData1, veinData2, veinData3, uid;
		try {
			veinData1 = I("post.image1").toString();
			veinData2 = I("post.image2").toString();
			veinData3 = I("post.image3").toString();
			uid = I("post.uid").toString();
		} catch (Exception e) {
			error("0");
			return;
		}
		HashMap<String, String> user = new HashMap<>();
		user.put("state", "2");
		user.put("update_time", TimeUtil.getLongTimeStamp() + "");
		user.put("veinData1", veinData1);
		user.put("veinData2", veinData2);
		user.put("veinData3", veinData3);
		try {
			M("user").where("id=" + uid).save_string(user);
			success("数据库更新成功");
		} catch (Exception e) {
			error("数据库更新失败");
		}

	}
}
