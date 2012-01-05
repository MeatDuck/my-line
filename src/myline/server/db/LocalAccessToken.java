package myline.server.db;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION) 
public class LocalAccessToken {

	  @PrimaryKey
	  @Persistent
	  private String user;
	  @Persistent
	  private String token;
	  @Persistent
	  private String passhash;
	  
	public void setPasshash(String passhash) {
		this.passhash = passhash;
	}
	public String getPasshash() {
		return passhash;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser() {
		return user;
	}
}
