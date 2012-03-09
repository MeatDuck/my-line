package myline.client.service;

import myline.shared.network.MessageContaner;
import myline.shared.network.UrlContaner;
import myline.shared.security.Access;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GettingServiceAsync {
	void getInitiationURL(AsyncCallback<UrlContaner> asyncCallback);

	void isAuth(Access acc,
			AsyncCallback<Boolean> callback);

	void getMessages(Access acc, AsyncCallback<MessageContaner> callback);
	
	void getMessages(Access acc, Integer page, AsyncCallback<MessageContaner> callback);

	void sendMessage(Access acc, String txt, AsyncCallback<Void> callback);

	void deleteStatus(Access acc, String statusId, AsyncCallback<Void> callback);
	
	void takeShortLink(String str, AsyncCallback<String> callback);

	void getStatusNumber(Access acc, AsyncCallback<Integer> callback);

	void getAllMessages(Access value, Integer count,
			AsyncCallback<MessageContaner> asyncCallback);

}
