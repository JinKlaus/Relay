package controller.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import annotation.action;
import config.Dictionary;
import server.Controller;
import server.ControllerContext;
import util.TimeUtil;
import util.ExcelUtil.ExcelMap;

public class IorecordController extends Controller{

	public IorecordController(ControllerContext context) {
		super(context);
	}
	
	@action
	public void list() {
		toHtml("admin_tpl/record_list");
	}
	
	@action
	public void getList() {
		String page=I("get.page").toString();
		String limit = Integer.parseInt(page)*10+",10";
		HashMap<String, Object> res = new HashMap<>();
		int num = M("reportrecord").count();
		ArrayList<HashMap<String, String>> list = M("reportrecord").limit(limit).select();
		HashMap<Integer, String> personTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(Dictionary.STUDENT, "学生");
			put(Dictionary.TEACHER, "教师");
			put(Dictionary.PARENT, "家长");
			}};
		HashMap<Integer, String> cardTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
			put(Dictionary.TOUCHEDCARD, "指静脉");
			}};
		HashMap<Integer, String> inoutTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{	
			put(Dictionary.ENTRANCE, "入口");
			put(Dictionary.EXIT, "出口");
		}};
		
		for(int i=0;i<list.size();i++) {
			String pt=personTypes.get(Integer.parseInt(list.get(i).get("personType")));
			pt= pt!= null?pt:"其它";
			list.get(i).put("cardType",cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
			list.get(i).put("personType",pt);
			list.get(i).put("dateTime",TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss") );
			list.get(i).put("inoutType",inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
		}
		res.put("num", num);
		res.put("list",list);
		success(res);	
	}
	
    
    @action
    public void do_excel(){
    	ArrayList<HashMap<String, String>> list = M("reportrecord").select();
		HashMap<Integer, String> personTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(Dictionary.STUDENT, "学生");
			put(Dictionary.TEACHER, "教师");
			put(Dictionary.PARENT, "家长");
			}};
		HashMap<Integer, String> cardTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
			put(Dictionary.TOUCHEDCARD, "指静脉");
			}};
		HashMap<Integer, String> inoutTypes = new HashMap<Integer,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{	
			put(Dictionary.ENTRANCE, "入口");
			put(Dictionary.EXIT, "出口");
		}};
		
		for(int i=0;i<list.size();i++) {
			String pt=personTypes.get(Integer.parseInt(list.get(i).get("personType")));
			pt= pt!= null?pt:"其它";
			list.get(i).put("cardType",cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
			list.get(i).put("personType",pt);
			list.get(i).put("dateTime",TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss") );
			list.get(i).put("inoutType",inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
		}
		HashMap<String, String> map=new HashMap<>();
		map.put("id", "编号");
		map.put("name", "姓名");
		map.put("cardNo", "卡号");
		map.put("cardType", "卡类型");
		map.put("personType", "人员类型");
		map.put("dateTime", "刷卡时间");
		map.put("inoutType", "进出类型");
		try {
			ExcelMap.exportXls(map, list, "src/resourses/test.xls");
			success("1");
		} catch (IOException e) {
			e.printStackTrace();
			error("0");
		}
		
		
    }
}
