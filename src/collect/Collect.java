package collect;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public abstract class Collect {
	
	public static int interval_time = 1000 * 10; //定时执行任务
	
	 TimerTask task = new TimerTask() {
         @Override
         public void run() {
        	 System.out.println("执行定时任务");
        	 intveral_task();
         }
     };
     Timer timer = new Timer();
	
	public Collect() throws Exception{
		init();
		 timer.scheduleAtFixedRate(task, interval_time, interval_time);
		
	}
	
	public abstract void init()  throws Exception;
	
	public abstract List<HashMap<String, String>> getPlatform();
	
	public abstract List<HashMap<String, String>> getRooms(String key) throws Exception;
	
	public abstract HashMap<String, String> getRoomInfo(String key);
	
	//定时任务
	public abstract void intveral_task();
	
	public void destory(){
		timer.cancel();
	
	}
	
	public abstract String getPull(String id,String channel) throws Exception;
	
	
	
	

	
	

}
