package myline.server.managers;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import myline.shared.ClientConstants;
import myline.shared.exceptions.ServiceException;
import myline.shared.security.Access;
import myline.server.security.*;
import myline.server.db.LocalAccessToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class SessionManager {
	private static final Logger log = Logger.getLogger(SessionManager.class.getName());
	private static SessionManager instance = null;
	public HttpSession session = null;
	private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private HashMap<String, AccessToken> map = new HashMap<String, AccessToken>();

	
	private SessionManager(HttpServletRequest request){
		//setting keys to memory
		if(ClientConstants.LOCAL_ADDRESS_HOST.equals(request.getLocalAddr())){
			System.setProperty("oauth.consumerKey", SecurityConstants.KEY_TEST);
			System.setProperty("oauth.consumerSecret", SecurityConstants.HASH_TEST);			
		} else{
			System.setProperty("oauth.consumerKey", SecurityConstants.KEY_PROD);
			System.setProperty("oauth.consumerSecret", SecurityConstants.HASH_PROD);			
			
		}		
		
        //setup session
        this.session = request.getSession(true);
	}
	
	public AccessToken getToken(Access access) throws ServiceException{
		log.info("try to get token for " + access.getUid());
		if(map.containsKey(access.getUid()) && 
		   map.get(access.getUid()) != null && 
		   map.get(access.getUid()).getToken() != null &&
		   map.get(access.getUid()).getTokenSecret() != null) 
			return map.get(access.getUid());
		
		log.info("no token in memory");
		String acc_token = (String) session.getAttribute(ClientConstants.ACC_TOKEN + "_" + access.getUid());
		String acc_hash = (String) session.getAttribute(ClientConstants.ACC_SECRET + "_" + access.getUid());
		
		AccessToken token = null;		
		if(acc_token != null && acc_hash != null){
			token = new AccessToken(acc_token, acc_hash);
		}else{
			log.info("no token in session");
			if(!Security.isValid(access)){
				return null;
			}
			
			log.info("token in db");
			PersistenceManager pm = PMF.getPersistenceManager();
			try{
				Key k = KeyFactory.createKey(LocalAccessToken.class.getSimpleName(), access.getUid());
				LocalAccessToken acc = (LocalAccessToken) pm.getObjectById(LocalAccessToken.class, k);
				
				token = new AccessToken(acc.getToken(), acc.getPasshash());
				if (token != null) {
				    if (token.getToken() != null && token.getTokenSecret() != null) {
						session.setAttribute(ClientConstants.ACC_TOKEN + "_" + access.getUid(), token.getToken());
						session.setAttribute(ClientConstants.ACC_SECRET + "_" + access.getUid(), token.getTokenSecret());
				    }
				}
			}catch(JDOObjectNotFoundException e){
				log.info("no token in db");
			}
			
		}
		
		this.map.put(access.getUid(), token);
		return token;
	}
	
	public String getScreenName(Access access) throws ServiceException{
		log.info("try to get getScreenName");
		String scName = (String) session.getAttribute(ClientConstants.SCREENNAME + "_" + access.getUid());
		
		AccessToken token = getToken(access);
		Twitter twitter = new TwitterFactory().getInstance(token);
		if(scName == null){
			try {
				scName = twitter.getScreenName();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		return scName;
	}
	
	public void resetScreenName(Access access){
		session.removeAttribute(ClientConstants.SCREENNAME+ "_" + access.getUid());
	}
	
	public void setToken(AccessToken token, String uid){
		if(token == null || uid.equals("")) 
			return;
		
		log.info("store token to session");
		session.setAttribute(ClientConstants.ACC_TOKEN + "_" + uid, token.getToken());
		session.setAttribute(ClientConstants.ACC_SECRET + "_" + uid, token.getTokenSecret());
		
		PersistenceManager pm = PMF.getPersistenceManager();
		LocalAccessToken acc = null;
		try{
			acc = (LocalAccessToken) pm.getObjectById(LocalAccessToken.class, uid);
			log.info("token exist & changed");
		}catch(JDOObjectNotFoundException e){
			log.info("no token exist in db");
		}
		if(acc != null){
			//update
			acc.setToken(token.getToken());
			acc.setPasshash(token.getTokenSecret());
			pm.makePersistent(acc);
			log.info("token succesfully updated");
		}else{
	        LocalAccessToken la = new LocalAccessToken();
	        la.setUser(uid);
	        la.setToken(token.getToken());
	        la.setPasshash(token.getTokenSecret());
	        pm.makePersistent(la);
	        log.info("token succesfully created");
		}
		
	}

	public static SessionManager setRequest(
			HttpServletRequest request) {
		if(instance == null)
			instance = new SessionManager(request);
		return instance;
	}
}
