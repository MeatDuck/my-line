package myline.shared.network;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Message  implements IsSerializable {
	private String nick;
	private String user;
	private String urlImg;
	private String message; 
	private long id;
	private boolean isEditable; 
	private Date date;
	
	public Message(){
		
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return nick;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return user;
	}
	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}
	public String getUrlImg() {
		return urlImg;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	public boolean getIsEditable() {
		return isEditable;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
}