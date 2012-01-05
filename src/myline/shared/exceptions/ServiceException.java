package myline.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServiceException extends Exception implements IsSerializable {	
	private static final long serialVersionUID = 7446718505230331126L;
	private String title;
	private StackTraceElement[] stacktrace;

	  public ServiceException() {
	  }

	  public ServiceException(String title) {
	    this.title = title;
	  }
	  
	  public ServiceException(Exception e){
		  this.stacktrace = e.getStackTrace();
		  this.title = e.getMessage();
	  }

	  public String getMessage() {
	    return this.title;
	  }
	  
	  public StackTraceElement[] getStackTrace() {
		return stacktrace;
	}
}
