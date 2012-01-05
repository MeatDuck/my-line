package myline.client.UI;

import java.util.List;

import myline.client.managers.VK;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.network.MessageContaner;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainPanel extends FlowPanel {
	private FlowPanel settings;
	private Panel horisontal;
	private Panel wall;
	
	DialogBox synDialogBox = createSynDialogBox();
	DialogBox setDialogBox = createSettingsDialogBox();
	
	private MainPanel(){
		super();
	}
	
	public MainPanel(Panel horisontal, Panel wall){
		this();
		this.horisontal = horisontal;
		this.wall = wall;
		this.settings = new FlowPanel();
		
		setupSettingsPanel(this.settings);
		
		this.add(this.settings);
		this.add(this.horisontal);
		this.add(this.wall);
				
	}
	
	private void setupSettingsPanel(FlowPanel settings){
		settings.setStyleName("settings_container");
		
		//synDialogBox
		synDialogBox.setGlassEnabled(true);
		synDialogBox.setAnimationEnabled(true);
		synDialogBox.setPopupPosition(120, 90);
		synDialogBox.addStyleName("dialog");
	    
		//setDialogBox
		setDialogBox.setGlassEnabled(true);
		setDialogBox.setAnimationEnabled(true);
		setDialogBox.setPopupPosition(120, 90);
		setDialogBox.addStyleName("dialog");
		
		Button button1 = new Button(ClientConstants.SYN_LABEL);
		Button button2 = new Button(ClientConstants.SETTINGS_LABEL);
		
		button1.addClickHandler(new MenuHandler(synDialogBox));
		
		button2.addClickHandler(new MenuHandler(setDialogBox));
		
		Image logo = new Image("/images/logo.png");
		logo.setStyleName("logo_padding");
		settings.add(logo);
		//settings.add(button1);
		
		//temporary
		button2.addStyleName("ml80");
		settings.add(button2);
	}
	
	//sys panel on click settings box
	private DialogBox createSettingsDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
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
	                }else{
	                	Cookies.setCookie("sendtowall", "false");
	                }
	          }
        });
	    dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

        // Return the dialog box
	    return dialogBox;
	}
	
	//sys panel on click sync
	private DialogBox createSynDialogBox() {
		
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(ClientConstants.synDialogBoxCaption);

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);
	    
	    final HTML details = new HTML("");	    
	    dialogContents.add(details);
	    dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_LEFT);
	    
	    final HorizontalPanel synpanel = new HorizontalPanel();
	    dialogContents.add(synpanel);
	    dialogContents.setCellHorizontalAlignment(synpanel, HasHorizontalAlignment.ALIGN_LEFT);
	    
	    //get mess count
		//RPC
		Button synButton = new Button(ClientConstants.synButtonLabel);
		TextBox numberText = new TextBox();
		Label txt1 = new Label(ClientConstants.syn1Label);
		Label txt2 = new Label(ClientConstants.syn2Label);
		
		numberText.setStyleName("w25 ltr5");
		txt1.setStyleName("ltr5");
		txt2.setStyleName("ltr5");
		
		numberText.setText("10");
		 // Add syn text
		details.setHTML("");
		
		synpanel.add(txt1);
		synpanel.add(numberText);
		synpanel.add(txt2);
		synpanel.add(synButton);		
		
		//syn action
		synButton.addClickHandler(new SynHandler(numberText, synButton));
	    
	    // Add a close button at the bottom of the dialog
	    Button closeButton = new Button(
	    		ClientConstants.cwDialogBoxClose, new ClickHandler() {
	          public void onClick(ClickEvent event) {
	                dialogBox.hide();
	          }
        });
	    dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

        // Return the dialog box
	    return dialogBox;
	}
	
	/**
	 * syncronisation click handler
	 * @author main
	 *
	 */
	class SynHandler implements  ClickHandler{
		TextBox numberText = null;
		Button bttn = null;
		
		public SynHandler(TextBox numberText, Button bttn) {	
			this.numberText = numberText;
			this.bttn = bttn;
		}

		@Override
		public void onClick(ClickEvent event) {
			numberText.setEnabled(false);
			bttn.setEnabled(false);
			bttn.setText(ClientConstants.WAIT_LABEL);
			
			Integer count = 0;
			try {
				count = Integer.parseInt(numberText.getText());
			} catch (NumberFormatException e) {
				//donothing
			}
			
			if(count > 0){
				GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
				custService.getAllMessages((Access)Registry.getInctance().getValue("access"), count, new AsyncCallback<MessageContaner>() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(MessageContaner mes) {
						final List messages = mes.getMessages();
						final Access acc = (Access) Registry.getInctance().getValue("access");
											
						Timer timer = new Timer() {
							int i = messages.size();	
							
							@Override
							public void run() {
								if(i <= 0) {
									this.cancel();	
									numberText.setEnabled(true);
									bttn.setEnabled(true);
									bttn.setText(ClientConstants.synButtonLabel);
									return;
								}
								List mess = (List) messages.get(i - 1);
								VK.sendToWall(acc.getUid(), mess.get(3).toString());	
								i--;
							}
						};
						timer.scheduleRepeating(1100);
						timer.run();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						numberText.setEnabled(true);
						bttn.setEnabled(true);						
					}
				});
			}		
		}
	}
	
	/**
	 * menu click handler
	 * @author main
	 *
	 */
	class MenuHandler implements  ClickHandler{
		private DialogBox dialog;

		public MenuHandler(DialogBox dialog){
			this.dialog = dialog;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			dialog.show();	
		}
		
	}
}
