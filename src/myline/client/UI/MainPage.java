package myline.client.UI;

import myline.client.managers.DecorationManager;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MainPage extends Composite {

	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);
	@UiField Button settingsButton;
	@UiField SendTwitPanel sendTwitPanel;
	@UiField public FlowPanel twitItems;
	@UiField ErrorNotificationMole errorNotificationPanel;
	@UiField Image image;
	DialogBox settingsDB;
	private Access acc; 

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {		
		this.acc = (Access) Registry.getInctance().getValue("access");
		initWidget(uiBinder.createAndBindUi(this));
		sendTwitPanel.setMainPage(this);
		settingsDB =  createSettingsDialogBox();
	}
	
	//sys panel on click settings box
	private DialogBox createSettingsDialogBox() {
	    // Create a dialog box and set the caption text
		DialogBox dialogBox = new DialogBox();
		
		//setDialogBox
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
	    dialogBox.setPopupPosition(120, 90);
	    dialogBox.addStyleName("dialog");
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(ClientConstants.SETTINGS_LABEL);

	    // Create a table to layout the content
	    SettingsPanel dialogContents = new SettingsPanel(dialogBox);
	    dialogBox.setWidget(dialogContents);
	    
	    if(Cookies.isCookieEnabled()){
	    	dialogContents.askCookieLabel.setVisible(false);
	    	dialogContents.sendToWallChb.setEnabled(true);
	    	if(Registry.getInctance().getValue("sendtowall").equals(true)){
	    		dialogContents.sendToWallChb.setValue(true);
	    	}else{
	    		dialogContents.sendToWallChb.setValue(false);
	    	}
	    }else{
	    	dialogContents.askCookieLabel.setVisible(true);
	    }
	    
	    // Return the dialog box
	    return dialogBox;
	}
	
	@UiHandler("settingsButton")
	void onSettingsButtonClick(ClickEvent event) {
		settingsDB.show();
	}
	
	public void showError(String message){
		errorNotificationPanel.setText(message);
		errorNotificationPanel.show();
	}
	
	@UiHandler("image")
	void onImageClick(ClickEvent event) {
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.logOut(acc, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				DecorationManager.getInstance().showLogin();
			}
			
			@Override
			public void onFailure(Throwable e) {
				showError(ClientConstants.LOGOUT_ERROR_MESSAGE);					
			}
		});
	}
}
