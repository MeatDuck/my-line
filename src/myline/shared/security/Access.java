package myline.shared.security;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Access implements IsSerializable {
	private String uid;
	private String sign;
	
	public Access(){
		super();
	}
	
	public Access(String uid, String sign){
		setUid(uid);
		setSign(sign);
	}
	
	private void setUid(String uid) {
		this.uid = uid;
	}
	public String getUid() {
		return uid;
	}

	private void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign() {
		return sign;
	}
}
