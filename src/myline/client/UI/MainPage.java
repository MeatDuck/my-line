package myline.client.UI;

import myline.shared.ClientConstants;
import myline.shared.Registry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainPage extends Composite {

	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);
	@UiField Button settingsButton;
	@UiField SendTwitPanel sendTwitPanel;
	@UiField public FlowPanel twitItems;
	@UiField ErrorNotificationMole errorNotificationPanel;
	DialogBox settingsDB; 

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	public MainPage() {		
		initWidget(uiBinder.createAndBindUi(this));
		sendTwitPanel.setMainPage(this);
		settingsDB =  createSettingsDialogBox();
	}
	
	//sys panel on click settings box
	private DialogBox createSettingsDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
		//setDialogBox
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
	    dialogBox.setPopupPosition(120, 90);
	    dialogBox.addStyleName("dialog");
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(ClientConstants.setDialogBoxCaption);

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);
	    
	    final CheckBox setCheck = new CheckBox("Отправлять статус на стену");
	    if(Cookies.isCookieEnabled()){
	    	// Add some text to the top of the dialog
	    	HTML details = new HTML(ClientConstants.setDialogBoxDetails);
	    	dialogContents.add(details);
	    	dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_LEFT);

	    	setCheck.setEnabled(true);
	    	if(Registry.getInctance().getValue("sendtowall").equals(true)){
	    		setCheck.setValue(true);
	    	}else{
	    		setCheck.setValue(false);
	    	}
		    dialogContents.add(setCheck);
	    }else{
	    	HTML details = new HTML(ClientConstants.sytDialogCook);
	    	dialogContents.add(details);
	    	dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_LEFT);
	    }
	    
	    // Add a close button at the bottom of the dialog
	    Button closeButton = new Button(
	    		ClientConstants.cwDialogBoxClose, new ClickHandler() {
	          public void onClick(ClickEvent event) {
	                dialogBox.hide();
	                if(setCheck.getValue()){
	                	Cookies.setCookie("sendtowall", "true");
	                	Registry.getInctance().setKey("sendtowall", true);
	                }else{
	                	Cookies.setCookie("sendtowall", "false");
	                	Registry.getInctance().setKey("sendtowall", false);
	                }
	          }
        });
	    dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

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
}
