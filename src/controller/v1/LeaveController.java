package controller.v1;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.TimeUtil;

/**
 * @Description 教师请假管理
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class LeaveController extends AdminController {
	public LeaveController(ControllerContext context) {
		super(context);
	}

	@action
	public void add() {
		toHtml("admin_tpl/leave_form_add");
	}

	@action
	public void do_add() throws ParseException {
		String startdate, enddate, reanson;
		startdate = I("post.startdate").toString();
		enddate = I("post.enddate").toString();
		reanson = I("post.reason").toString();
		HashMap<String, String> res = new HashMap<>();
		res.put("tea_name", user.get("name"));
		res.put("startdate", TimeUtil.dateToStamp(startdate, "yyyy-MM-dd HH:mm"));
		res.put("enddate", TimeUtil.dateToStamp(enddate, "yyyy-MM-dd HH:mm"));
		res.put("reason", reanson);
		res.put("state", Dictionary.STATE_SUBMIT + "");
		try {
			M("teacherleave").add(res);
			success("1");
		} catch (Exception e) {
			e.printStackTrace();
			error("0");
		}
	}

	@action
	public void list() {
		toHtml("admin_tpl/leave_form_list");
	}

	@action
	public void getList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
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
			ArrayList<HashMap<String, String>> list = M("teacherleave").where("tea_name='" + user.get("name") + "'")
					.limit(limit).select();
			int num = M("teacherleave").where("tea_name='" + user.get("name") + "'").count();
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
}
