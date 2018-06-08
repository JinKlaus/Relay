package util;


import org.apache.commons.pool.KeyedPoolableObjectFactory;

import model.Model;

public class ModelFactory  implements KeyedPoolableObjectFactory<String, Model> {
	
	//重新初始化实例返回池 
	@Override
	public void activateObject(String arg0, Model arg1) throws Exception {
		arg1.setActive(true);
		
	}

	//销毁被破坏的实例 
	@Override
	public void destroyObject(String arg0, Model arg1) throws Exception {
		arg1=null;
		
	}

	//创建一个实例到对象池  
	@Override
	public Model makeObject(String arg0) throws Exception {
		Model model = new Model(arg0);
		return model;
	}

	//取消初始化实例返回到空闲对象池  
	@Override
	public void passivateObject(String arg0, Model arg1) throws Exception {
		arg1.setActive(false);
	}

	//验证该实例是否安全 true:正在使用  
	@Override
	public boolean validateObject(String arg0, Model arg1) {
		if(arg1.isActive) return true;
		else return false;
	}

	 
	
	
	

}
