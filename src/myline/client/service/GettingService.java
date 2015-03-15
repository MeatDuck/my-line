package myline.client.service;

import myline.shared.exceptions.ServiceException;
import myline.shared.network.MessageContaner;
import myline.shared.network.UrlContaner;
import myline.shared.security.Access;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("get")
public interface GettingService extends RemoteService {
	
	MessageContaner getMessages(Access acc) throws  ServiceException;
	MessageContaner getMessages(Access acc, Integer page) throws  ServiceException; 
	MessageContaner getAllMessages(Access value, Integer count) throws ServiceException;
	
	UrlContaner getInitiationURL() throws ServiceException;
	
	Boolean isAuth(Access vkToken) throws ServiceException;
	
	void sendMessage(Access acc, String txt) throws ServiceException;
	
	void deleteStatus(Access acc, String statusId) throws ServiceException;
	
	String takeShortLink(String str);
	
	int getStatusNumber(Access acc) throws ServiceException;
	
	void logOut(Access acc) throws ServiceException;
	
	void logError(String exception);
}
