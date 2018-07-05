package controller.v1;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import model.Model;
import server.Controller;
import server.ControllerContext;
import util.ModelUtil;
import util.TimeUtil;

public class AdminController extends Controller {

	public static final int NORMAL = 0;
	public static final int DUTY = 1;
	public static final int APPROVAER = 2;
	public static final int ADMIN = 3;

	protected HashMap<String, String> user;
	protected int admin_type = NORMAL;

	@SuppressWarnings("unchecked")
	public AdminController(ControllerContext context) {
		super(context);
		sessionID = "rdmd7yvst7xxzuzwt6l9bgpi";// "1q9ufcvccdk75o7f33s0h46g9";////context.request.getSession().getId();
		assign("controller", context.CONTROLLER);
		HashMap<String, String> map = M("session").where("sessionid='" + sessionID + "'").find();
		if (map == null) {
			redirect("/v1/access/login");
			pri = false;
			return;
		}
		try {
			user = JSON.parseObject(map.get("object"), new HashMap<>().getClass());
			admin_type = Integer.parseInt(user.get("isadmin"));
			assign("admin_type", admin_type);
		} catch (Exception e) {
			e.printStackTrace();
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
				res.put("type", "1");
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
