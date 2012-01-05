package myline.shared.network;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UrlContaner implements IsSerializable  {
	private String url;
	private String labeString;
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setLabeString(String labeString) {
		this.labeString = labeString;
	}
	public String getLabeString() {
		return labeString;
	}
	
}
