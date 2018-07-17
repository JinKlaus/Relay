package controller.v1;

import java.util.ArrayList;
import java.util.HashMap;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.Md5Util;
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
			String page = I("get.page").toString();
			String limit = Integer.parseInt(page) * 10 + ",10";
			ArrayList<HashMap<String, String>> list = M("studentleave").order("id").where("pid=" + user.get("id")).limit(limit).select();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("startdate", TimeUtil.stampToDate(list.get(i).get("startdate"), "yyyy-MM-dd"));
				list.get(i).put("enddate",  TimeUtil.stampToDate(list.get(i).get("enddate"), "yyyy-MM-dd"));
				list.get(i).put("create_time",  TimeUtil.stampToDate(list.get(i).get("create_time"), "yyyy-MM-dd HH:mm:ss"));
			}
			success(list);
		}catch (Exception e){
			error("获取学生请假信息失败");
		}

	}

	/**
	 * @Description: 提交请假申请
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void getAbsentInfo() {
		try {
			String startdate = I("post.startdate").toString();
			String enddate = I("post.enddate").toString();
			String reason = I("post.reason").toString();
			HashMap<String, String> map = new HashMap<>();
			map.put("pid", user.get("id"));
			map.put("startdate", TimeUtil.dateToStamp(startdate, "yyyy-MM-dd"));
			map.put("enddate", TimeUtil.dateToStamp(enddate, "yyyy-MM-dd"));
			map.put("reason", reason);
			map.put("create_time", TimeUtil.getShortTimeStamp() + "");
			M("studentleave").add(map);
			success("请假信息提交成功");
		} catch (Exception e) {
			error("请假信息提交失败");
		}
	}

	/**
	 * @Description: 获取学生的信息
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void getStudentInfo() {
		String sql = "select a.id as stu_id,a.name,a.sid,c.name as clazz_name from student a left join parent b on a.id=b.stu_id left join clazz c on a.clazz_id=c.id  where b.id ="
				+ user.get("id");
		try {
			ArrayList<HashMap<String, String>> list = M("student").query(sql);
			success(list);
		} catch (Exception e) {
			error("获取学生信息失败");
		}
	}

	/**
	 * @Description: 获取当前登录家长的小孩进出记录
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void getStuIorecord() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		String sql="select name,dateTime,inoutType,channelID from reportrecord where persontype=10 and cardNo="+user.get("id")+" order by id desc limit " + limit;
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

	/**
	 * @Description: 获取当前登录家长进出记录
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void getParIorecord() {
		String page = I("get.page").toString();
		String limit = Integer.parseInt(page) * 10 + ",10";
		String sql="select name,dateTime,inoutType,channelID from reportrecord where persontype=13 and cardNo="+user.get("id")+"  order by id desc limit " + limit;
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

	/**
	 * @Description: 修改家长登录密码
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	@action
	public void changepwd(){
		try {
			String old_pwd= I("post.old_pwd").toString();
			String new_pwd = I("post.new_pwd").toString();
			HashMap<String,String> map=new HashMap<>();
			map.put("original_pwd",Md5Util.MD5(new_pwd));
			HashMap<String, String> map2 = M("parent").where("id=" + user.get("id") + " and original_pwd='" + Md5Util.MD5(old_pwd) + "'").find();
			if(map2 == null){
				error("原密码错误");
				return;
			}else{
				M("parent").where("id=" + user.get("id")).save_string(map);
						success("修改登录密码成功");
			}
		}catch (Exception e){
			e.printStackTrace();
			error("修改登录密码失败");
		}
	}

	/**
	 * @Description:  查看系统已录入的家庭成员
	 * @param:
	 * @return:
	 * @auther: CodyLongo
	 * @modified:
	 */
	 @action
	public void getMember(){
	 	try {
			ArrayList<HashMap<String, String>> list = M("parent").field("par_name,tel").where("stu_id=" + user.get("stu_id")).select();
			success(list);
		}catch (Exception e){
	 		error("获取家庭成员失败");
		}
	 }
}
