package myline.client.UI;

import myline.client.managers.DecorationManager;
import myline.client.managers.VK;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SendTwitPanel extends Composite {

	private static SendTwitPanelUiBinder uiBinder = GWT
			.create(SendTwitPanelUiBinder.class);
	@UiField TextArea textArea;
	@UiField Button sendButton;
	@UiField VerticalPanel messagePanel;
	@UiField Label simbols;
	@UiField Panel wallPanel;
	@UiField Image addLinkButton;
	@UiField VerticalPanel loadingPanel;
	DialogBox dialogBox = createGetLinkDialogBox();

	private Access acc;
	private MainPage page;
	
	interface SendTwitPanelUiBinder extends UiBinder<Widget, SendTwitPanel> {
	}

	public SendTwitPanel() {
		this.acc = (Access) Registry.getInctance().getValue("access");
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//dialogBox
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
	    dialogBox.setPopupPosition(120, 90);
	    dialogBox.addStyleName("dialog");
	}
	
	public void setMainPage(MainPage mp){
		page = mp;
	}
	
	public MainPage getMainPage(){
		return page;
	}

	@UiHandler("textArea")
	void onTextAreaKeyUp(KeyUpEvent event) {
		TextArea txt  = (TextArea) event.getSource();
		Integer length = ClientConstants.MAX_MESSAGE_LENGTH - txt.getText().length();
		checkLength(length);
	}
	
	private void checkLength(Integer length) {
		simbols.setText(length.toString());
		if(length >= 0 && length < 140){
			sendButton.setEnabled(true);
			simbols.removeStyleName("error");
		}else{
			sendButton.setEnabled(false);
			simbols.addStyleName("error");
			
		}
	}
	@UiHandler("sendButton")
	void onSendButtonClick(ClickEvent event) {
		if(textArea.getText().trim().length() > 0 && textArea.getText().trim().length() <= 140){
			showLoading();
			GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
			if(Registry.getInctance().getValue("sendtowall").equals(true)){
				VK.sendToWall(acc.getUid(), textArea.getText());
			}
			
			custService.sendMessage(acc, textArea.getText(), new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					textArea.setText("");					
					DecorationManager.getInstance().refresh(ClientConstants.TIMER_REFESH_SENDER_TIME);
					stopLoading(messagePanel);
				}
				
				@Override
				public void onFailure(Throwable e) {
					stopLoading(messagePanel);
					page.showError(ClientConstants.ADD_LINE_ERROR_MESSAGE);					
				}
			});
		}
	}
	@UiHandler("textArea")
	void onTextAreaKeyPress(KeyPressEvent event) {
		int keyCode = event.getNativeEvent().getKeyCode();
		if(event.isControlKeyDown() && keyCode == KeyCodes.KEY_ENTER){
			sendButton.click();
		}	
	}
	
	void checkLength() {
		checkLength(140 - textArea.getText().length());		
	}

	private DialogBox createGetLinkDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(ClientConstants.cwDialogBoxCaption);
  
	    AddLinkDialogPanel dialogContents = new AddLinkDialogPanel(dialogBox, this);
	    dialogBox.setWidget(dialogContents);
        
        // Return the dialog box
	    return dialogBox;
	}
	@UiHandler("addLinkButton")
	void onAddLinkButtonClick(ClickEvent event) {
		dialogBox.show();
	}
	
	private void showLoading(){
		messagePanel.setVisible(false);
		loadingPanel.setVisible(true);
	}
	
	private void stopLoading(Panel c){
		textArea.setText("");
		textArea.setFocus(true);
		messagePanel.setVisible(true);
		loadingPanel.setVisible(false);
	}
}

