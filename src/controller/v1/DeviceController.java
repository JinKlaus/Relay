package controller.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;

import annotation.action;
import config.Dictionary;
import server.Controller;
import server.ControllerContext;

import util.TimeUtil;


public class DeviceController extends Controller {

	
	public DeviceController(ControllerContext context) {
		super(context);
	}
	
	/***
	 * 获取白名单信息
	 * 控制器周期获取白名单信息，提供给控制器离线场景下的进出验证
	 */
	@action
	public void getWhiteListData() {
		String controllerID;//控制器编号
		String versionNo;//数据版本号
		int pages = 5;
		HashMap<String, Object> res = new HashMap<>();
		try {
			versionNo=I("post.versionNo").toString();
		    ArrayList<HashMap<String, String>> list = 
		    		M("user").where("update_time>"+versionNo)
		    		.field("id,uuid,cardNo,startDate,endDate,create_time,update_time,name,personType,veinData1,veinData2,veinData3,state,passType")
		    		.order("update_time asc").limit(pages+"").select();
		    System.out.println(list.size());
		    for(int i = 0 ; i < list.size() ; i++) {
		    	  list.get(i).put("id",list.get(i).get("uuid"));
		    	  list.get(i).put("startDate", TimeUtil.stampToMysqlDate(list.get(i).get("startDate").toString()));
		    	  list.get(i).put("endDate", TimeUtil.stampToMysqlDate(list.get(i).get("endDate").toString()));
		    	  list.get(i).put("create_time", TimeUtil.stampToMysqlDate(list.get(i).get("create_time").toString()));
		    	  list.get(i).put("versionNo", list.get(i).get("update_time"));
		    	  list.get(i).remove("update_time");
		    	  list.get(i).remove("uuid");
		    	  list.get(i).remove("create_time");
		    }
		    if(list.size()>=5)
				res.put("result",2);
		    else res.put("result",1);
		    res.put("whiteDataArry",list);
		    out(res);
	
		    
		   
		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", 0);
			res.put("errorCode",Dictionary.GETWHITEERROR);
			out(res);
			return;
		}
		
	}
	
	@action
	public void reportRecord(){
		String cardInfoRecordArry;
		HashMap<String,Object> res = new HashMap<>();
		
		try {
			cardInfoRecordArry = I("post.cardInfoRecordArry").toString();
			
			List<HashMap<String,String>> list = 
					(List<HashMap<String, String>>) JSON.parseArray(cardInfoRecordArry,new HashMap<String,String>().getClass());
			
			for(int i=0;i<list.size();i++) {
				HashMap<String, String> data = list.get(i);
				data.put("dateTime",TimeUtil.dateToStamp(data.get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
				M("reportrecord").add(data);
			}				
			res.put("result", 1);
			out(res);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", 0);
			res.put("errorCode",Dictionary.REPORTRECORDERROR);
			out(res);
			return;
		}
		
		
		
		
		
	}
	

}
