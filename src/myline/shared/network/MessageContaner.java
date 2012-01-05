package myline.shared.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MessageContaner implements IsSerializable {
	@SuppressWarnings("unchecked")
	private List<List> messages = new ArrayList<List>();
	
	@SuppressWarnings("unchecked")
	public List<List> getMessages(){
		return messages;
	}
	
	@SuppressWarnings("unchecked")
	public void addMessage(String nick, String user, String url_img, String message, long l, boolean b, Date date){
		List tmp = new ArrayList();
		tmp.add(nick);
		tmp.add(user);
		tmp.add(url_img);
		tmp.add(message);
		tmp.add(String.valueOf(l));
		tmp.add(b);
		tmp.add(String.valueOf(date.getTime()));
		getMessages().add(tmp);
	}
	
	public void addContainer(MessageContaner container){
		this.getMessages().add(container.getMessages());
	}
}
