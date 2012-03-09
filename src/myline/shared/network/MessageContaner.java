package myline.shared.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MessageContaner implements IsSerializable {
	private List<Message> messages = new ArrayList<Message>();
	
	public MessageContaner(){
		
	}
	
	public List<Message> getMessages(){
		return messages;
	}
	
	public void addMessage(String nick, String user, String url_img, String message, long id, boolean isEditable, Date date){
		Message tmp = new Message();
		tmp.setNick(nick);
		tmp.setUser(user);
		tmp.setUrl_img(url_img);
		tmp.setMessage(message);
		tmp.setId(id);
		tmp.setIsEditable(isEditable);
		tmp.setDate(date);
		getMessages().add(tmp);
	}
}
