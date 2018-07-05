package controller.v1;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import server.ControllerContext;
import util.StringUtil;
import util.TimeUtil;

public class StatisticController extends AdminController {

	public StatisticController(ControllerContext context) {
		super(context);
		if (admin_type < APPROVAER) {
			pri = false;
			return;
		}
	}

	@action
	public void list() {
		toHtml("admin_tpl/statistic_list");
	}

	@SuppressWarnings("deprecation")
	@action
	public void getSearchList() throws ParseException {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		String searchname = I("get.searchname") == "" ? "" : URLDecoder.decode(I("get.searchname").toString());
		String leavedate = I("get.leavedate") == "" ? ""
				: TimeUtil.dateToStamp(URLDecoder.decode(I("get.leavedate").toString()), "yyyy-MM-dd HH:mm");
		String enddate = I("get.enddate") == "" ? ""
				: TimeUtil.dateToStamp(URLDecoder.decode(I("get.enddate").toString()), "yyyy-MM-dd HH:mm");
		String clazz_id = StringUtil.isEmpty(I("get.clazz_id")) || I("get.clazz_id").equals("0") ? ""
				: I("get.clazz_id").toString();
		StringBuffer s = new StringBuffer(
				"select a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name,d.id as clazz_id from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id where 1=1");
		StringBuffer snum = new StringBuffer(
				"select count(*),a.*,b.tel,c.name,d.name as clazz_name,e.name as tea_name,d.id as clazz_id  from studentleave a LEFT JOIN parent b on a.pid=b.id LEFT JOIN student c on b.stu_id =c.id LEFT JOIN clazz d on c.clazz_id=d.id LEFT JOIN teacher e on e.clazz_id=d.id where 1=1");
		if (!leavedate.isEmpty() && !enddate.isEmpty()) {
			s.append(" and startdate BETWEEN '" + leavedate + "' AND '" + enddate + "'");
			snum.append(" and startdate BETWEEN '" + leavedate + "' AND '" + enddate + "'");
		}
		if (!searchname.isEmpty()) {
			s.append(" and e.name like '%" + searchname + "%'");
			snum.append(" and e.name like '%" + searchname + "%'");
		}
		if (!clazz_id.isEmpty()) {
			s.append(" and d.id ='" + clazz_id + "'");
			snum.append(" and d.id ='" + clazz_id + "'");
		}
		s.append(" limit " + limit);
		String sql = s.toString();
		String sql1 = snum.toString();
		HashMap<String, Object> res = new HashMap<>();
		try {
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
			error("获取查询列表错误");
		}
	}

	@action
	public void getClazzInfo() {
		String sql = "select id,name from clazz";
		ArrayList<HashMap<String, String>> list = M("clazz").query(sql);
		success(list);
	}

}
