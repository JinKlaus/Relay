package util;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPool;


import model.Model;

public class ModelUtil {
	
	public static KeyedPoolableObjectFactory<String, Model> keyFactory = new ModelFactory();  
    public static GenericKeyedObjectPool<String, Model> keyPool = new GenericKeyedObjectPool<String, Model>(keyFactory); 
    
    static{
    	Config config = new GenericKeyedObjectPool.Config();
    	 //指明能从池中借出的对象的最大数目。如果这个值不是正数，表示没有限制。
    	config.maxActive=-1;
    	
    	//whenExhaustedA ction指定在池中借出对象的数目已达极限的情况下，调用它的borrowObject方法时的行为。可以选用的值有
    	//GenericObjectPool.WHEN_EXHAUSTED_BLOCK，表示等待；
    	//GenericObjectPool.WHEN_EXHAUSTED_GROW，表示创建新的实例（不过这就使maxActive参数失去了意义）；
    	//GenericObjectPool.WHEN_EXHAUSTED_FAIL，表示抛出一个java.util.NoSuchElementException异常。
    	config.whenExhaustedAction=GenericObjectPool.WHEN_EXHAUSTED_GROW;
    	
    	//参数maxWait指明若在对象池空时调用borrowObject方法的行为被设定成等待，最多等待多少毫秒。如果等待时间超过了这个数值，则会抛出一个java.util.NoSuchElementException异常。如果这个值不是正数，表示无限期等待。
    	config.maxWait=-1;
    	
    	//参数testOnBorrow设定在借出对象时是否进行有效性检查。
    	config.testOnBorrow = false;
    	
    	//还回对象时是否进行有效性检查。
    	config.testOnReturn = false;
    	
    	//设定间隔每过多少毫秒进行一次后台对象清理的行动。如果这个值不是正数，则实际上不会进行后台对象清理。
    	config.timeBetweenEvictionRunsMillis = -1;
    	
    	
    	
    	keyPool.setConfig(config);
    }
	
	
	public static Model getModel(String table){
		try {
			return keyPool.borrowObject(table);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
