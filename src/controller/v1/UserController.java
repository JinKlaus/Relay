package controller.v1;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import model.Model;
import server.Controller;
import server.ControllerContext;
import util.ModelUtil;
import util.TimeUtil;

/**
 * @Description 客户端权限控制及日志记录
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class UserController extends Controller {

	protected HashMap<String, String> user;

	public UserController(ControllerContext context) {
		super(context);
		String token = I("token") == null ? "" : I("token").toString();
		if (token.isEmpty()) {
			error("非法操作");
			pri = false;
			return;
		}
		user = M("parent").where("token= '" + token +"'").find();
		if (user == null) {
			error("登录异常请重新登录");
			pri = false;
			return;
		}
		setLog();
	}

	public void setLog() {
		new Thread() {
			public void run() {
				Model logModel = ModelUtil.getModel("log");
				HashMap<String, String> res = new HashMap<>();
				res.put("controller", context.CONTROLLER);
				res.put("action", context.ACTION);
				res.put("create_time", TimeUtil.getShortTimeStamp() + "");
				res.put("type", "2");
				res.put("uid", user.get("uid"));
				res.put("getdata", JSON.toJSONString(context.GET));
				res.put("postdata", JSON.toJSONString(context.POST));
				try {
					logModel.add(res);
				} catch (Exception e) {
					e.printStackTrace();
					error("日志记录失败");
				}
			}
		}.start();

	}

}
