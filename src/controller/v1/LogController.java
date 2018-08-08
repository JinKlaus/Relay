package controller.v1;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import server.ControllerContext;
import util.StringUtil;
import util.TimeUtil;

/**
 * @Description 日志管理列表
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class LogController extends AdminController {

	public LogController(ControllerContext context) {
		super(context);
		if (admin_type < ADMIN) {
			pri = false;
			return;
		}
	}

	@action
	public void list() {
		toHtml("admin_tpl/log_list");
	}

	@action
	public void getSearchList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		@SuppressWarnings("deprecation")
		String searchname = StringUtil.isEmpty(I("get.searchname")) ? ""
				: URLDecoder.decode(I("get.searchname").toString());
		StringBuffer s = new StringBuffer(
				"SELECT a.*,b.name as tea_name FROM log  a LEFT JOIN teacher b on a.uid=b.uid where 1=1");
		StringBuffer snum = new StringBuffer(
				"SELECT count(*) FROM log  a LEFT JOIN teacher b on a.uid=b.uid where 1=1");
		if (searchname != null && searchname.length() > 0) {
			s.append(" and b.name like '%" + searchname + "%'");
			snum.append(" and b.name like '%" + searchname + "%'");
		}
		s.append(" order by create_time desc");
		s.append(" limit " + limit);
		String sql = s.toString();
		String sql1 = snum.toString();
		HashMap<String, Object> res = new HashMap<>();
		try {
			ArrayList<HashMap<String, String>> list = M("log").query(sql);
			String num = M("log").query(sql1).get(0).get("count(*)");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("create_time",
						TimeUtil.stampToDate(list.get(i).get("create_time"), "yyyy-MM-dd HH:mm:ss"));
			}
			res.put("num", num);
			res.put("list", list);
			success(res);
		} catch (Exception e) {
			error("查询失败");
		}
	}

	// @SuppressWarnings("deprecation")
	// @action
	// public void do_excel() {
	// String searchname=StringUtil.isEmpty(I("post.searchname"))? ""
	// :URLDecoder.decode(I("post.searchname").toString());
	// StringBuffer s=new StringBuffer("SELECT a.*,b.name as tea_name FROM log a
	// LEFT JOIN teacher b on a.uid=b.uid where 1=1");
	// StringBuffer snum=new StringBuffer("SELECT count(*) FROM log a LEFT JOIN
	// teacher b on a.uid=b.uid where 1=1");
	// if(searchname != null && searchname.length()>0) {
	// s.append(" and b.name like '%" + searchname + "%'");
	// snum.append(" and b.name like '%" + searchname + "%'");
	// }
	// String sql=s.toString();
	// try {
	// ArrayList<HashMap<String, String>> list = M("log").query(sql);
	// for(int i=0;i<list.size();i++) {
	// list.get(i).put("create_time",
	// TimeUtil.stampToDate(list.get(i).get("create_time"), "yyyy-MM-dd HH:mm:ss"));
	// }
	// HashMap<String, String> map = new HashMap<>();
	// map.put("id","日志编号");
	// map.put("controller","操作的控制器名称");
	// map.put("action","操作的方法");
	// map.put("create_time","操作时间");
	// map.put("tea_name","操作人员姓名");
	// map.put("getdata","get方式的数据");
	// map.put("postdata","post方式的数据");
	// ExcelMap.exportXls(map, list, "assets/resourses/teacherlog.xls");
	// success("/resourses/teacherlog.xls");
	// }catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
