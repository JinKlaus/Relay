package controller.v1;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import annotation.action;
import config.Dictionary;
import server.Controller;
import server.ControllerContext;
import util.TimeUtil;
import util.ExcelUtil.ExcelMap;

public class IorecordController extends Controller {

	public IorecordController(ControllerContext context) {
		super(context);
	}

	@action
	public void list() {
		toHtml("admin_tpl/record_list");
	}

	@action
	public void getList() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		HashMap<String, Object> res = new HashMap<>();
		int num = M("reportrecord").count();
		String sql = "select a.*,b.tel,c.name as stu_name from reportrecord a LEFT JOIN parent b ON a.name=b.par_name LEFT JOIN student c ON b.stu_id=c.id limit "
				+ limit;
		ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
		HashMap<Integer, String> personTypes = new HashMap<Integer, String>() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				put(Dictionary.STUDENT, "学生");
				put(Dictionary.TEACHER, "教师");
				put(Dictionary.PARENT, "家长");
			}
		};
		HashMap<Integer, String> cardTypes = new HashMap<Integer, String>() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
				put(Dictionary.TOUCHEDCARD, "指静脉");
			}
		};
		HashMap<Integer, String> inoutTypes = new HashMap<Integer, String>() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				put(Dictionary.ENTRANCE, "入口");
				put(Dictionary.EXIT, "出口");
			}
		};

		for (int i = 0; i < list.size(); i++) {
			String pt = personTypes.get(Integer.parseInt(list.get(i).get("personType")));
			pt = pt != null ? pt : "其它";
			list.get(i).put("cardType", cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
			list.get(i).put("personType", pt);
			list.get(i).put("dateTime", TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
			list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
		}
		res.put("num", num);
		res.put("list", list);
		success(res);

	}

	@SuppressWarnings("deprecation")
	@action
	public void getSearchList() {
		try {
			String page = I("get.page").toString();
			String limit = Integer.parseInt(page) * 10 + ",10";
			String startdate = I("get.startdate") == "" ? ""
					: TimeUtil.dateToStamp(URLDecoder.decode(I("get.startdate").toString()), "yyyy-MM-dd HH:mm:ss");
			String enddate = I("get.enddate") == "" ? ""
					: TimeUtil.dateToStamp(URLDecoder.decode(I("get.enddate").toString()), "yyyy-MM-dd HH:mm:ss");
			String stu_name = I("get.stu_name") == "" ? "" : URLDecoder.decode(I("get.stu_name").toString());
			String tel = I("get.tel") == "" ? "" : I("get.tel").toString();
			StringBuffer s = new StringBuffer(
					"select a.*,b.tel,c.name as stu_name from reportrecord a LEFT JOIN parent b ON a.name=b.par_name LEFT JOIN student c ON b.stu_id=c.id where 1=1");
			StringBuffer snum = new StringBuffer(
					"select count(*),a.*,b.tel,c.name as stu_name from reportrecord a LEFT JOIN parent b ON a.name=b.par_name LEFT JOIN student c ON b.stu_id=c.id where 1=1");
			if (startdate != null && startdate.length() > 0 && enddate != null && enddate.length() > 0) {
				s.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
				snum.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
			}
			if (stu_name != null && stu_name.length() > 0) {
				s.append(" and c.name like '%" + stu_name + "%'");
				snum.append(" and c.name like '%" + stu_name + "%'");
			}
			if (tel != null && tel.length() > 0) {
				s.append(" and b.tel like '%" + tel + "%'");
				snum.append(" and b.tel like '%" + tel + "%'");
			}
			s.append(" limit " + limit);
			String sql = s.toString();
			String sql1 = snum.toString();
			try {
				String num = M("reportrecord").query(sql1).get(0).get("count(*)");
				ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
				HashMap<String, Object> res = new HashMap<>();
				HashMap<Integer, String> personTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.STUDENT, "学生");
						put(Dictionary.TEACHER, "教师");
						put(Dictionary.PARENT, "家长");
					}
				};
				HashMap<Integer, String> cardTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
						put(Dictionary.TOUCHEDCARD, "指静脉");
					}
				};
				HashMap<Integer, String> inoutTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.ENTRANCE, "入口");
						put(Dictionary.EXIT, "出口");
					}
				};

				for (int i = 0; i < list.size(); i++) {
					String pt = personTypes.get(Integer.parseInt(list.get(i).get("personType")));
					pt = pt != null ? pt : "其它";
					list.get(i).put("cardType", cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
					list.get(i).put("personType", pt);
					list.get(i).put("dateTime",
							TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
					list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
				}
				res.put("num", num);
				res.put("list", list);
				success(res);
			} catch (Exception e) {
				error("查询失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error("0");
		}

	}

	@SuppressWarnings("deprecation")
	@action
	public void do_excel() {
		try {
			String startdate = I("startdate") == null || I("startdate").equals("") ? ""
					: TimeUtil.dateToStamp(URLDecoder.decode(I("startdate").toString()), "yyyy-MM-dd HH:mm:ss");
			String enddate = I("enddate") == null || I("enddate").equals("") ? ""
					: TimeUtil.dateToStamp(URLDecoder.decode(I("enddate").toString()), "yyyy-MM-dd HH:mm:ss");
			String stu_name = I("stu_name") == null || I("stu_name").equals("") ? ""
					: URLDecoder.decode(I("stu_name").toString());
			String tel = I("tel") == null || I("tel").equals("") ? "" : I("tel").toString();
			StringBuffer s = new StringBuffer(
					"select a.*,b.tel,c.name as stu_name from reportrecord a LEFT JOIN parent b ON a.name=b.par_name LEFT JOIN student c ON b.stu_id=c.id where 1=1");
			if (startdate != null && startdate.length() > 0 && enddate != null && enddate.length() > 0) 
				s.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
			if (stu_name != null && stu_name.length() > 0)
				s.append(" and c.name like '%" + stu_name + "%'");
			if (tel != null && tel.length() > 0)
				s.append(" and b.tel like '%" + tel + "%'");
			String sql = s.toString();
			try {
				ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
				HashMap<Integer, String> personTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.STUDENT, "学生");
						put(Dictionary.TEACHER, "教师");
						put(Dictionary.PARENT, "家长");
					}
				};
				HashMap<Integer, String> cardTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
						put(Dictionary.TOUCHEDCARD, "指静脉");
					}
				};
				HashMap<Integer, String> inoutTypes = new HashMap<Integer, String>() {
					/**
					* 
					*/
					private static final long serialVersionUID = 1L;

					{
						put(Dictionary.ENTRANCE, "入口");
						put(Dictionary.EXIT, "出口");
					}
				};

				for (int i = 0; i < list.size(); i++) {
					String pt = personTypes.get(Integer.parseInt(list.get(i).get("personType")));
					pt = pt != null ? pt : "其它";
					list.get(i).put("cardType", cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
					list.get(i).put("personType", pt);
					list.get(i).put("dateTime",
							TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
					list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
				}
				HashMap<String, String> map = new HashMap<>();
				map.put("id", "编号");
				map.put("name", "姓名");
				map.put("stu_name", "学生姓名");
				map.put("cardNo", "卡号");
				map.put("cardType", "卡类型");
				map.put("personType", "人员类型");
				map.put("dateTime", "刷卡时间");
				map.put("inoutType", "进出类型");
				map.put("tel", "联系方式");
				try {
					ExcelMap.exportXls(map, list, "assets/resourses/test.xls");
					success("/resourses/test.xls");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			error("0");
		}

	}
}
