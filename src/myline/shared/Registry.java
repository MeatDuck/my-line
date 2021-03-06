package myline.shared;

import java.util.HashMap;
import java.util.Map;

public final class Registry {
	final private Map<Object, Object> storage = new HashMap<Object, Object>();
	private static Registry instance;
	
	private Registry(){};
	
	public static Registry getInctance(){
		if(instance == null){
			instance = new Registry();
		}
		return instance;
	}
	
	public void setKey(Object key, Object value){
		storage.put(key, value);
	}
	
	public Object getValue(Object key){
		return storage.get(key);
	}
	
	public void removeKey(Object key){
		if(storage.containsKey(key)){
			storage.remove(key);
		}
	}
}
