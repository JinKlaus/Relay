package controller.v1;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import server.ControllerContext;
import util.TimeUtil;

public class AbsentController extends AdminController {

	public AbsentController(ControllerContext context) {
		super(context);
	}

	@action
	public void list() {
		toHtml("admin_tpl/absent_list");
	}

	@action
	public void getList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		HashMap<String, Object> res = new HashMap<>();
		try {
			String sql = "select a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id where e.name='"
					+ user.get("name") + "'  limit " + limit;
			String sql1 = "select count(*),a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id where e.name='"
					+ user.get("name") + "'";
			ArrayList<HashMap<String, String>> list = M("studentleave").query(sql);
			String num = M("studentleave").query(sql1).get(0).get("count(*)");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("enddate", TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd HH:mm"));
			}
			res.put("num", num);
			res.put("list", list);
			success(res);
		} catch (Exception e) {
			error("获取请假列表错误");
		}
	}

	@action
	public void getSearchList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		@SuppressWarnings("deprecation")
		String searchname = I("get.searchname") == "" ? "" : URLDecoder.decode(I("get.searchname").toString());
		String sql = "select a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id  where e.name='"
				+ user.get("name") + "' and c.name like '%" + searchname + "%'  limit " + limit;
		String sql1 = "select count(*),a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id  where e.name='"
				+ user.get("name") + "' and c.name like '%" + searchname + "%'";
		HashMap<String, Object> res = new HashMap<>();
		try {
			ArrayList<HashMap<String, String>> list = M("absent").query(sql);
			String num = M("absent").query(sql1).get(0).get("count(*)");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("enddate", TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd HH:mm"));
			}
			res.put("num", num);
			res.put("list", list);
			success(res);
		} catch (Exception e) {
			error("获取查询列表错误");
		}
	}

}
