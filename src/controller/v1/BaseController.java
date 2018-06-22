package controller.v1;

import java.util.HashMap;

import server.Controller;
import server.ControllerContext;

public class BaseController extends Controller {
	public static HashMap<String, String> t_customer = new HashMap<>();

	public BaseController(ControllerContext ctx) {
		super(ctx);
		String g = ctx.GET.get("token");// ctx.request.getHeader("token")
		String p=ctx.POST.get("token").toString();
		String t=g==null?p:g;
		if (t != null) {
			String token = t;

			t_customer = M("t_customer").where("token = '" + token + "'").find();
			if (t_customer == null) {

				error("invalid token");
				throw new RuntimeException("invalid token");
			}

		} else {

			error("invalid token");

			throw new RuntimeException("invalid token");
		}
	}

}
