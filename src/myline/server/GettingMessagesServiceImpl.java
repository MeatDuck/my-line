package myline.server;

import static com.rosaloves.bitlyj.Bitly.shorten;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.log.*;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import myline.client.service.GettingService;
import myline.server.managers.ConnectionManager;
import myline.server.managers.SessionManager;
import myline.server.security.SecurityConstants;
import myline.shared.ClientConstants;
import myline.shared.exceptions.ServiceException;
import myline.shared.network.MessageContaner;
import myline.shared.network.UrlContaner;
import myline.shared.security.Access;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Bitly.Provider;
import com.rosaloves.bitlyj.Url;

public class GettingMessagesServiceImpl extends RemoteServiceServlet implements
		GettingService {

	private static final long serialVersionUID = 3625750779791025487L;
	private static Logger LOG=Logger.getLogger(GettingMessagesServiceImpl.class.toString());

	@Override
	public MessageContaner getMessages(Access acc) throws ServiceException {
		return getMessages(acc, null);
	}
	
	@Override
	public MessageContaner getMessages(Access acc, Integer page)	throws ServiceException {
		LOG.info("getMessages started");
		AccessToken token = SessionManager.setRequest(getThreadLocalRequest()).getToken(acc);
		if(token == null){
			return new MessageContaner();
		}
			
		Twitter twitter = new TwitterFactory().getInstance(token);
	    LOG.info("=============================");
		LOG.info("AccessToken.getToken = " + token.getToken());	
		LOG.info("AccessToken.getTokenSecret = " + token.getTokenSecret());	
		LOG.info("==============================");
		ArrayList<Status> statuses = null;
		
		try {
			if(page == null){
				statuses = new ArrayList<Status>(twitter.getUserTimeline());
			}else{
				Paging pageing = new Paging(page);
				statuses = new ArrayList<Status>(twitter.getUserTimeline(pageing));
			}
		} catch (TwitterException e) {
			throw new ServiceException(e);
		} catch (IllegalStateException e) {
			throw new ServiceException(e);
		}
		
		String svName = SessionManager.setRequest(getThreadLocalRequest()).getScreenName(acc);
		MessageContaner contaner = new MessageContaner();
		for (Iterator<Status> iterator = statuses.iterator(); iterator.hasNext();) {
			Status status = (Status) iterator.next();
			boolean isEditable = false;
			String screenName = status.getUser().getScreenName();
			if(svName != null && screenName != null && screenName.equals(svName)){
				isEditable = true;
			}
			contaner.addMessage(
					status.getUser().getScreenName(), 
					status.getUser().getName(), 
					status.getUser().getProfileImageURL().toString(), 
					status.getText(),
					status.getId(),
					isEditable,
					status.getCreatedAt()
					);			
		}
		
		
		return contaner;
		}

	@Override
	public UrlContaner getInitiationURL() throws ServiceException{
		HttpSession session = getThreadLocalRequest().getSession(true);
		SessionManager.setRequest(getThreadLocalRequest());
		UrlContaner contaner = new UrlContaner(); 
		Twitter twitter = new TwitterFactory().getInstance();
		try{
			RequestToken requestToken = twitter.getOAuthRequestToken();
			session.setAttribute(ClientConstants.REQUEST_TOKEN, requestToken.getToken());
			session.setAttribute(ClientConstants.REQUEST_HASH, requestToken.getTokenSecret());
			LOG.info("try save RequestToken to session = " + session.getAttribute(ClientConstants.REQUEST_TOKEN) + " & hash = " + session.getAttribute(ClientConstants.REQUEST_HASH));
			contaner.setUrl(requestToken.getAuthorizationURL());
		}catch(TwitterException e){
			throw new ServiceException(e);
		}
		return contaner;
	}
	
	@Override
	public Boolean isAuth(Access vkToken) throws ServiceException {
		LOG.info("isAuth started with token = " + vkToken + " & uid = " + vkToken.getUid());
		
		//check if already auth & try to verify
		HttpSession session = getThreadLocalRequest().getSession(true);		 
		AccessToken tok = SessionManager.setRequest(getThreadLocalRequest()).getToken(vkToken);
		if(tok != null){
			LOG.info("token is exist");
			Twitter twitter = new TwitterFactory().getInstance(tok);
			try {
				twitter.verifyCredentials();
				return true;
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		
		//check try to reauth
		AccessToken accessToken = null;		
		LOG.info("try to reauth");
		try {
			SessionManager.setRequest(getThreadLocalRequest()).resetScreenName(vkToken);
			LOG.info("try to getOAuthAccessToken");
			String requestToken = (String) session.getAttribute(ClientConstants.REQUEST_TOKEN);
			String requestHash = (String) session.getAttribute(ClientConstants.REQUEST_HASH);
			LOG.info("try RequestToken from session = " + requestToken + " & sec " + requestHash);
			if(requestToken == null || requestHash == null){
				return false;
			}
			
			RequestToken rtObj = new RequestToken(requestToken, requestHash);
			session.removeAttribute(ClientConstants.REQUEST_TOKEN);
			session.removeAttribute(ClientConstants.REQUEST_HASH);
			//!!!!
			Twitter twitter = new TwitterFactory().getInstance();
			if (!(twitter.getAuthorization() instanceof OAuthAuthorization)){
				twitter.setOAuthConsumer(System.getProperty("oauth.consumerKey"), System.getProperty("oauth.consumerSecret"));
			}
			accessToken = twitter.getOAuthAccessToken(rtObj);
			LOG.info("accessToken = " + accessToken.getToken());
		} catch (TwitterException e) {
			return false;
		} catch (IllegalStateException e) {
			throw new ServiceException(e);
		}
		
		ConnectionManager manager = new ConnectionManager();

		return manager.isAuth(accessToken, vkToken.getUid(), getThreadLocalRequest());
	}

	@Override
	public void sendMessage(Access acc, String txt) throws ServiceException {
		LOG.info("sendMessage started");
		AccessToken token = SessionManager.setRequest(getThreadLocalRequest()).getToken(acc);
		Twitter twitter = new TwitterFactory().getInstance(token);	
		
		try {
			twitter.updateStatus(txt);
		} catch (TwitterException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteStatus(Access acc, String statusId) throws ServiceException{
		LOG.info("deletestatus started");
		AccessToken token = SessionManager.setRequest(getThreadLocalRequest()).getToken(acc);
		Twitter twitter = new TwitterFactory().getInstance(token);
		try {
			twitter.destroyStatus(Long.parseLong(statusId));
		} catch (TwitterException e) {
			throw new ServiceException(e);
		}
		
		
	}

	public String takeShortLink(String str) {
		LOG.info("tru to get shorl link for " + str);
		if(str == null || str.isEmpty()){
			return str;
		}
		
		String regex = "^(http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		if(!isMatch(str, regex)){
			return str;
		}
		//check url
		
		Provider bitly = Bitly.as(SecurityConstants.SHORT_API_LOGIN, SecurityConstants.SHORT_API_KEY);
		Url newUrl = bitly.call(shorten(str));
		return newUrl.getShortUrl();		
	}
	
	public int getStatusNumber(Access acc) throws ServiceException{
		LOG.info("getStatusNumber started");
		AccessToken token = SessionManager.setRequest(getThreadLocalRequest()).getToken(acc);
		Twitter twitter = new TwitterFactory().getInstance(token);
		
		try {			
			return twitter.verifyCredentials().getStatusesCount();
		} catch (Exception e) {
			throw new ServiceException(e);
		} 
	}
	
    private static boolean isMatch(String str, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(str);
            return matcher.matches();
        } catch (RuntimeException e) {
        	return false;
        }
    }

	@Override
	public MessageContaner getAllMessages(Access acc, Integer numMessage) throws ServiceException {
		if(numMessage < 1){
			throw new ServiceException("Bad page numer");
		}
		
		LOG.info("getAllMessages started");
		AccessToken token = SessionManager.setRequest(getThreadLocalRequest()).getToken(acc);
		Twitter twitter = new TwitterFactory().getInstance(token);
		
		List<Status> statuses = new ArrayList<Status>();
		int page = 1;
		Paging pageing = new Paging(page);
		
		try {		
			for(int i = 0; i <= 3; i++){
				statuses.addAll(new ArrayList<Status>(twitter.getUserTimeline(pageing)));
				page++;
				pageing = new Paging(page);
				if(statuses.size() >= numMessage) {
					break;
				}
			}
		} catch (Exception exception) {
			LOG.info(exception.getMessage());
		}
		Integer numLimit = Math.min(numMessage, statuses.size());
		
		int tempCount = 0;
		MessageContaner contaner = new MessageContaner();
		for (Iterator<Status> iterator = statuses.iterator(); iterator.hasNext();) {
			if(tempCount >= numLimit){
				break;
			}
			Status status = (Status) iterator.next();
			boolean isEditable = false;
			contaner.addMessage(
					status.getUser().getScreenName(), 
					status.getUser().getName(), 
					status.getUser().getProfileImageURL().toString(), 
					status.getText(),
					status.getId(),
					isEditable,
					status.getCreatedAt()
					);		
			tempCount++;
		}
		
		
		return contaner;
	}

	@Override
	public void logOut(Access acc) throws ServiceException {
		SessionManager.setRequest(getThreadLocalRequest()).logout(acc);		
	}

	@Override
	public void logError(String exception) {
		LOG.severe(exception);
	}

}
