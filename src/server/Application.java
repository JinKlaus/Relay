package server;

import java.sql.SQLException;

import collect.CollectManager;



public class Application {
	public Application(){
		startMysql();
		try {
			CollectManager.init(CollectManager.YEMEI);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("直播采集初始化失败:"+e.getMessage());
		}
	}
	
	public void startMysql(){
		//启动mysql驱动
			try {
				
				new com.mysql.jdbc.Driver();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("mysql driver success!");
			}
				
			
					
					
				
				
	}
	
}
