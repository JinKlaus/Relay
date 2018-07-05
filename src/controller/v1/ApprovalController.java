package controller.v1;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.StringUtil;
import util.TimeUtil;
import util.ExcelUtil.ExcelMap;

public class ApprovalController extends AdminController {
	public ApprovalController(ControllerContext context) {
		super(context);
		if (admin_type < APPROVAER) {
			pri = false;
			return;
		}
	}

	@action
	public void list() {
		toHtml("admin_tpl/approval_list");
	}

	@action
	public void getSearchList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		@SuppressWarnings("deprecation")
		String searchname = I("get.searchname") == "" ? "" : URLDecoder.decode(I("get.searchname").toString());
		String searchstate = I("get.searchstate") == "" || I("get.searchstate").equals("3") ? ""
				: I("get.searchstate").toString();
		StringBuffer s = new StringBuffer("select * from teacherleave where 1=1");
		StringBuffer snum = new StringBuffer("select count(*) from teacherleave where 1=1");
		if (searchname != null && searchname.length() > 0) {
			s.append(" and tea_name like '%" + searchname + "%'");
			snum.append(" and tea_name like '%" + searchname + "%'");
		}
		if (searchstate != null && searchstate.length() > 0) {
			s.append(" and state = '" + searchstate + "'");
			snum.append(" and state = '" + searchstate + "'");
		}
		s.append(" limit " + limit);
		String sql = s.toString();
		String sql1 = snum.toString();
		HashMap<String, Object> res = new HashMap<>();
		@SuppressWarnings("serial")
		HashMap<Integer, String> stateTypes = new HashMap<Integer, String>() {
			{
				put(Dictionary.STATE_SUBMIT, "已提交，待审核");
				put(Dictionary.STATE_SUCCESS, "申请已通过");
				put(Dictionary.STATE_FAILURE, "申请被驳回");
			}
		};
		try {
			ArrayList<HashMap<String, String>> list = M("teacherleave").query(sql);
			ArrayList<HashMap<String, String>> list1 = M("teacherleave").query(sql1);
			String num = list1.get(0).get("count(*)");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("enddate", TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("state", stateTypes.get(Integer.parseInt(list.get(i).get("state"))));
			}
			res.put("num", num);
			res.put("list", list);
			success(res);
		} catch (Exception e) {
			error("获取请假列表错误");
		}

	}

	@SuppressWarnings("deprecation")
	@action
	public void do_excel() {
		String searchname = StringUtil.isEmpty(I("post.searchname")) ? ""
				: URLDecoder.decode(I("post.searchname").toString());
		String searchstate = StringUtil.isEmpty(I("post.searchstate")) || I("post.searchstate").equals("3") ? ""
				: I("post.searchstate").toString();
		StringBuffer s = new StringBuffer("select * from teacherleave where 1=1");
		if (searchname != null && searchname.length() > 0) {
			s.append(" and tea_name like '%" + searchname + "%'");
		}
		if (searchstate != null && searchstate.length() > 0) {
			s.append(" and state = '" + searchstate + "'");
		}
		String sql = s.toString();
		@SuppressWarnings("serial")
		HashMap<Integer, String> stateTypes = new HashMap<Integer, String>() {
			{
				put(Dictionary.STATE_SUBMIT, "已提交，待审核");
				put(Dictionary.STATE_SUCCESS, "申请已通过");
				put(Dictionary.STATE_FAILURE, "申请被驳回");
			}
		};
		try {
			ArrayList<HashMap<String, String>> list = M("teacherleave").query(sql);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("enddate", TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd HH:mm"));
				list.get(i).put("state", stateTypes.get(Integer.parseInt(list.get(i).get("state"))));
			}
			HashMap<String, String> map = new HashMap<>();
			map.put("id", "编号");
			map.put("tea_name", "教师姓名");
			map.put("startdate", "请假时间");
			map.put("enddate", "销假时间");
			map.put("reason", "请假原因");
			map.put("state", "审核状态");
			ExcelMap.exportXls(map, list, "assets/resourses/teacherleave.xls");
			success("/resourses/teacherleave.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@action
	public void approve() {
		String id = I("post.id").toString();
		try {
			HashMap<String, String> map = M("teacherleave").where("id=" + id).find();
			map.put("state", Dictionary.STATE_SUCCESS + "");
			M("teacherleave").where("id=" + id).save_string(map);
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}

	@action
	public void remove() {
		String id = I("get.id").toString();
		try {
			M("teacherleave").where("id=" + id).delete();
			success("1");
		} catch (Exception e) {
			error("0");
		}

	}

	@action
	public void refuse() {
		String id = I("post.id").toString();
		try {
			HashMap<String, String> map = M("teacherleave").where("id=" + id).find();
			map.put("state", Dictionary.STATE_FAILURE + "");
			M("teacherleave").where("id=" + id).save_string(map);
			success("1");
		} catch (Exception e) {
			error("0");
		}
	}

}
