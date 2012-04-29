package myline.server.managers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import twitter4j.auth.AccessToken;


public final class ConnectionManager {
	private static final Logger LOG = Logger.getLogger(ConnectionManager.class.getName());
	
	public Boolean isAuth(AccessToken token, String uid, HttpServletRequest request) {
		LOG.info("AccessToken if null?  " + token);
		if(token == null){
			return false;
		}	
		SessionManager.setRequest(request).setToken(token,uid);
		return true;
	}
}
