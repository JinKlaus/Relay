package controller.v1;

import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.TimeUtil;

/**
 * @Description 客户端接口
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class ClientRequestController extends UserController {

	public ClientRequestController(ControllerContext context) {
		super(context);
	}

	/**
	 * @Description: 获取小孩请假记录
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void getStuAbsentrecord(){
		try{
			ArrayList<HashMap<String, String>> list = M("studentleave").where("pid=" + user.get("id")).select();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd HH:mm:ss"));
				list.get(i).put("enddate",  TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd HH:mm:ss"));
				list.get(i).put("create_time",  TimeUtil.stampToDate(list.get(i).get("create_time"), "yyyy-MM-dd HH:mm:ss"));
			}
			success(list);
		}catch (Exception e){
			error("获取学生请假信息失败");
		}

	}
	@action
	public void getAbsentInfo() {
		try {
			String startdate = I("post.startdate").toString();
			String enddate = I("post.startdate").toString();
			String reason = I("post.reason").toString();
			HashMap<String, String> map = new HashMap<>();
			map.put("pid", user.get("id"));
			map.put("startdate", TimeUtil.dateToStamp(startdate, "yyyy-MM-dd HH:mm:ss"));
			map.put("enddate", TimeUtil.dateToStamp(enddate, "yyyy-MM-dd HH:mm:ss"));
			map.put("reason", reason);
			map.put("create_time", TimeUtil.getShortTimeStamp() + "");
			M("studentleave").add(map);
			success("请假信息提交成功");
		} catch (Exception e) {
			error("请假信息提交失败");
		}
	}

	@action
	public void getStudentInfo() {
		String sql = "select a.id,a.name,a.sid,c.name as clazz_name from student a left join parent b on a.id=b.stu_id left join clazz c on a.clazz_id=c.id  where b.id ="
				+ user.get("id");
		try {
			ArrayList<HashMap<String, String>> list = M("student").query(sql);
			success(list);
		} catch (Exception e) {
			error("获取学生信息失败");
		}
	}

	@action
	public void getStuIorecord() {
		String sql = "select a.name,a.dateTime,a.inoutType,a.channelID from reportrecord a left join student b on a.name=b.name left join parent c on b.id=c.stu_id where c.id ="
				+ user.get("id");
		try {
			ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
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
				list.get(i).put("dateTime", TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
				list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
			}
			success(list);
		} catch (Exception e) {
			error("获取学生进出记录失败");
		}
	}

	@action
	public void getParIorecord() {
		String sql = "select name,dateTime,inoutType,channelID from reportrecord where name = '" + user.get("par_name")
				+ "'";
		try {
			ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
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
				list.get(i).put("dateTime", TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
				list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
			}
			success(list);
		} catch (Exception e) {
			error("获取家长进出记录失败");
		}
	}

}
