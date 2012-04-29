package myline.shared.security;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Access implements IsSerializable {
	private String uid;
	private String sign;
	
	public Access(){
		super();
	}
	
	public Access(final String uid, final String sign){
		setUid(uid);
		setSign(sign);
	}
	
	private void setUid(final String uid) {
		this.uid = uid;
	}
	public String getUid() {
		return uid;
	}

	private void setSign(final String sign) {
		this.sign = sign;
	}

	public String getSign() {
		return sign;
	}
}
