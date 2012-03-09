package myline.client;


import myline.client.managers.DecorationManager;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.Registry;
import myline.shared.security.Access;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class My_line implements EntryPoint {
	private Access access = null;
	
	public void onModuleLoad() {
		//Get params  from request
		String user_id = Window.Location.getParameter("viewer_id");
		String auth_key = Window.Location.getParameter("auth_key");
				
		access = new Access(user_id, auth_key);
		Registry.getInctance().setKey("access", access);

		if(Cookies.getCookie("sendtowall") != null){
			Registry.getInctance().setKey("sendtowall", Cookies.getCookie("sendtowall").equals("true"));
		}else{
			Registry.getInctance().setKey("sendtowall", true);
		}
		DecorationManager.getInstance().showLoading();		

		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.isAuth(access, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					DecorationManager.getInstance().showMain();					
				}else {
					DecorationManager.getInstance().showLogin();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				DecorationManager.getInstance().showError(caught);
			}
		});
	}
}
