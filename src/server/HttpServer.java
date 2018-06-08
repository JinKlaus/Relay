package server;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;



import config.Config;
import config.Entrance;

public class HttpServer {
	
	
	public static void main(String[] args) throws Exception { 
		new Application();
		Server server = new Server(Entrance.java_port);
		
		HashSessionIdManager idmanager = new HashSessionIdManager();
        server.setSessionIdManager(idmanager);
		 
		HandlerList handlers = new HandlerList();
		
		ResourceHandler resource_handler = new ResourceHandler();  
		resource_handler.setResourceBase("./assets");
			 
		 ContextHandler context = new ContextHandler("/");
		
	    
	     
	     HashSessionManager manager = new HashSessionManager();
	     SessionHandler sessions = new SessionHandler(manager);
	     context.setHandler(sessions);

		 handlers.setHandlers(new Handler[]{context,resource_handler,new ServerHandler()});
		 server.setHandler(handlers);
	  	 server.start();
		 server.join();
		 
	
	
	}
	

}
