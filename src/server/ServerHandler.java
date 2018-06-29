package server;

import java.awt.event.InvocationEvent;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import config.Config;
import exception.RouteErrorException;

public class ServerHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		try {
			
			ControllerContext context = new ControllerContext(target,baseRequest,request,response);
			init(context);
			
		} catch (RouteErrorException e) {
			e.printStackTrace();
		}
		
		
	}
	
	void init(ControllerContext context) throws RouteErrorException {
		String classname = "controller."+context.MOUDLE+"."+toUpperCaseFirstOne(context.CONTROLLER)+"Controller";
		//获取指定的类
		try {
			Class<?> controller = Class.forName(classname);
			Constructor<?> c = controller.getConstructor(ControllerContext.class);
			Controller controllerInstance = (Controller) c.newInstance(context);
			if(controllerInstance.checkPri()){
				Method action = controller.getMethod(context.ACTION);
				String annotation="";
				for(Annotation an : action.getAnnotations()){
		            annotation=an.toString();
		        }
				if(!annotation.equals("@annotation.action()")){
					throw new RouteErrorException("action not find");
				}else{
					action.invoke(controllerInstance, null);
					controllerInstance.destruct();
				    
				}
				
			}
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			if(Config.IS_DEBUG){
				e.printStackTrace();
			}else{
				throw new RouteErrorException("controller not find");
			}
			
		} catch (InvocationTargetException e) {

			if(Config.IS_DEBUG) {
				e.printStackTrace();
			} else {
				String message = e.getTargetException().getMessage() != null ? e.getTargetException().getMessage() : "controller not find";
				throw new RouteErrorException(message);
			}

		}
		
	}
	
	 String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	

}
