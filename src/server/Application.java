package server;

import java.sql.SQLException;




public class Application {
	public Application(){
		startMysql();
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
