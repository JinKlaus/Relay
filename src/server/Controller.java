package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.cookie.CookieSpec;
import org.apache.poi.hssf.dev.EFBiffViewer;
import org.eclipse.jetty.server.Request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import config.Config;
import config.Entrance;

import exception.ParamsErrorException;
import exception.RouteErrorException;
import model.Model;
import util.FilterUtil;
import util.FilterUtil.FilterHandler;
import util.StringUtil;
import util.TplUtil;

public class Controller {
	
	public HashMap<String, Object> assigns;
	public String seo_title = "";
	
	public ControllerContext context;
	public String sessionID;
	
	public Controller(ControllerContext context){
		this.context = context;
		assigns = new HashMap<>();
		
	}
	
	public boolean checkPri(){
		
		return true;}
	
	void destruct(){
	}
	
	public void seo(String title){
		seo_title=title+"-"+seo_title;
	}
	

	

	
	public void makeHttp(String s){
		HttpServletResponse response = context.response;
		Request baseRequest = context.baseRequest;
		 response.setContentType("text/html;charset=utf-8");  
	        response.setStatus(HttpServletResponse.SC_OK);  
	        baseRequest.setHandled(true);
	        try {
				response.getWriter().println(s);
			} catch (IOException e) {
				e.printStackTrace();
			}  
	}
	
	
	/**
	 * 把变量输入到模版总
	 * @param key
	 * @param t
	 */
	public <T> void assign(String key,T t){
		
		assigns.put(key, t);
	}
	
	/**
	 * 显示并渲染模版文件
	 * @param tpl
	 */
	public void toHtml(String tpl){
		if(I("is_debug") != null && "1".equals(I("is_debug").toString())){
			success(assigns);
		}else{
			makeHttp(TplUtil.loadTpl(tpl,this));
		}
		
	}
	
	
	

	
	public Map filterParam(Map date) {
		return date;
	}
	
	public Model M(String model) {
		return new Model(model);
	}
	
	protected Model M() {
		return new Model("");
	}
	
	protected void error(String msg){
		HashMap<String, Object> res = new HashMap<>();
		res.put("code", "error");
		res.put("msg", msg);
		makeHttp(JSON.toJSONString(res));
		
	}
	
	protected void opmsg(boolean flag, String msg){
		HashMap<String, Object> res = new HashMap<>();
		res.put("success", flag);
		res.put("msg", msg);
		makeHttp(JSON.toJSONString(res));
	}
	
	protected void opmsg(boolean flag, String msg, Object data){
		HashMap<String, Object> res = new HashMap<>();
		res.put("success", flag);
		res.put("msg", msg);
		res.put("data", data);
		makeHttp(JSON.toJSONString(res));
	}
	
	/**
	 * 获取get或者post里面的参数值
	 * @param key
	 * @param is_num 纯数字
	 * @return
	 * @throws IOException
	 */
	protected Object I(String key,boolean is_num) throws IOException{
		
		String res = I(key).toString();
		if(StringUtil.isNumeric(res)) return res;
		
		else throw new IOException();
		
	}
	
	
	/**
	 * 获取get或者post里面的参数
	 * 如果要获取全部post的参数 post.*
	 * 如果要获取某个post的参数post.key
	 * 如果要获取某个get的参数get.key
	 * 如果要获取全部的get参数get.*
	 * @param res
	 * @return
	 */
	protected  Object I(String res) {
		 
		String[] rs = res.split("\\.");
		
		if(rs.length==1){
			if(context.POST.containsKey(res)) return filterParam(context.POST.get(res));
			else if(context.GET.containsKey(res)) return filterParam(context.GET.get(res));
			else return null;
		}else{
			if(rs[0].equals("post")) {
				if(rs[1].equals("*")){
					 return filterParam(context.POST);
				}
				else if(context.POST.containsKey(rs[1])) 
					return filterParam(context.POST.get(rs[1]));
				else return null;
			}
			else if(rs[0].equals("get")) {
				if(rs[1].equals("*")){
					 return filterParam(context.GET);	
				}
				else if(context.GET.containsKey(rs[1])) 
					return filterParam(context.GET.get(rs[1]));
				else return null;
			}
		}
		return null;
	}
	
	/**
	 * 过滤post或者get参数
	 * 添加两种过滤 html标记过滤和防止sql注入的过滤
	 * @param param
	 * @return
	 */
	protected String filterParam(String param) {
		ArrayList<FilterHandler> handlers = new ArrayList<>();
		//添加html过滤器
		handlers.add(FilterUtil.getHtmlHandler());
		//添加房sql注入过滤器
		handlers.add(FilterUtil.getSqlHandler());
		
		return FilterUtil.filter(param, handlers);
	}
	
	/**
	 * 输出错误的json数据
	 * @param msg
	 * @param msg_code
	 */
	protected void error(String msg,String msg_code) {
		HashMap<String, Object> res = new HashMap<>();
		res.put("code", "error");
		res.put("msg", msg);
		res.put("msg_code", msg_code);
		makeHttp(JSON.toJSONString(res));
	}
	
	
	/**
	 * 输出正确的json数据
	 * @param data
	 */
	protected void success(Object data) {
		HashMap<String, Object> res = new HashMap<>();
		res.put("code", "success");
		res.put("data", data);
		makeHttp(JSON.toJSONString(res,SerializerFeature.WriteMapNullValue));
	}
	

	public void redirect(String url) {
		try {
			context.response.sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	//获取字符窜参数
	protected String getPostStr(String paramName, boolean needCheck) throws ParamsErrorException {
		return _getParam("post.", paramName, needCheck);
	}
	
	protected String getGetStr(String paramName, boolean needCheck) throws ParamsErrorException {
		return _getParam("get.", paramName, needCheck);
	}
	
	protected String getStr(String paramName, boolean needCheck) throws ParamsErrorException {
		return _getParam("", paramName, needCheck);
	}
	
	//获取int型参数
	protected String getPostNum(String paramName, boolean needCheck) throws ParamsErrorException {
		String result = _getParam("post.", paramName, needCheck);
		return checkNum(result);
	}
	
	protected String getGetNum(String paramName, boolean needCheck) throws ParamsErrorException {
		String result = _getParam("get.", paramName, needCheck);
		return checkNum(result);
	}
	
	protected String getNum(String paramName, boolean needCheck) throws ParamsErrorException {
		String result = _getParam("", paramName, needCheck);
		return checkNum(result);
	}
	
/*	private String _getParam(String type, String paramName) throws ParamsErrorException {
		String paraValue;
		Object param = I(type+paramName);
		if (param == null || (paraValue = param.toString()).isEmpty()) {
			throw new ParamsErrorException("error param: "+paramName);
		} 
		return paraValue;
	}*/
	
	private String _getParam(String type, String paramName, boolean needCheck) throws ParamsErrorException {
		String paraValue;
		Object param;
		param = I(type+paramName);
		
		if (param == null || (paraValue = param.toString()).isEmpty()) {
			if (needCheck) {
				throw new ParamsErrorException("error param :"+paramName);
			} else {
				return null;
			}
		} 
		return paraValue;
	}
	
	private String checkNum(String res) throws ParamsErrorException {
		if(res != null && !StringUtil.isNumeric(res))
			throw new ParamsErrorException("illegal param");
		return res;
	}
	
	protected void setCookie() {
		Cookie cookie = new Cookie("sessionID", sessionID);
		cookie.setMaxAge(3600);
		cookie.setPath("/");
		context.response.addCookie(cookie);
	}
}
