package myline.server.managers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import twitter4j.auth.AccessToken;


public class ConnectionManager {
	private static final Logger log = Logger.getLogger(ConnectionManager.class.getName());
	
	static private ConnectionManager instance = null;
	private ConnectionManager(){
		
	}
	
	static public ConnectionManager getInstance(){
		if(instance == null){
			instance = new ConnectionManager();
		}
		return instance;
	}

	public Boolean isAuth(AccessToken token, String uid, HttpServletRequest request) {
		log.info("AccessToken if null?  " + token);
		if(token == null)
			return false;
		SessionManager.setRequest(request).setToken(token,uid);
		return true;
	}
}
