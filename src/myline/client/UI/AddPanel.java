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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddPanel extends VerticalPanel {
	final private Label simbols = new Label();
	final private VerticalPanel messagePanel = new VerticalPanel();
	final private FlowPanel horisontalPanel = new FlowPanel();
	final private TextArea textArea = new TextArea();
	final private Button sendButton = new Button(ClientConstants.SEND_LABEL);
	final private Button resetButton = new Button(ClientConstants.CLEAR_LABEL);
	final private AddLinkButton addLinkButton = new AddLinkButton();
	final DialogBox dialogBox = createGetLinkDialogBox();
	private TextBox globalNormalText = null;

	private Access acc;
	//timer
	private Timer t = new Timer() {
		@Override
		public void run() {
			DecorationManager.getInstance().showMain();
		}
	};	
	
	public AddPanel(Access acc){
		this.acc = acc;
		init();
		
		placeElements();
		
		Integer length = ClientConstants.MAX_MESSAGE_LENGTH - getTextArea().getText().length();
		simbols.setText(length.toString());
		checkLength(length);
	}
	
	private DialogBox createGetLinkDialogBox() {
	    // Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(ClientConstants.cwDialogBoxCaption);

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);

	    // Add some text to the top of the dialog
	    HTML details = new HTML(ClientConstants.cwDialogBoxDetails);
	    dialogContents.add(details);
	    dialogContents.setCellHorizontalAlignment(
	        details, HasHorizontalAlignment.ALIGN_CENTER);

	    //field
	    final TextBox normalText = new TextBox();
	    globalNormalText = normalText;
	    normalText.setStyleName("linkenter");
	    
	    // Add a close button at the bottom of the dialog
	    Button closeButton = new Button(
	    		ClientConstants.cwDialogBoxClose, new ClickHandler() {
	          public void onClick(ClickEvent event) {
					String linkTxt = normalText.getText();
					if(isRigthLink(linkTxt)){
						String text = getTextArea().getText();
						if(text.length() != 0){
							text = text + " ";
						}
						final String text2 = text;
						
						//RPC get Link
						GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
						custService.takeShortLink(linkTxt, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								getTextArea().setText(text2 + result + " ");
								getTextArea().setFocus(true);
								checkLength();								
							}
							
							@Override
							public void onFailure(Throwable caught) {	
							}
						});


					}
	                dialogBox.hide();
	                normalText.setText("");
	          }

			private boolean isRigthLink(String linkTxt) {
				// TODO regexp check
				String regex = "^(http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				RegExp patt = RegExp.compile(regex);
				MatchResult exRes = patt.exec(linkTxt);
	            return (exRes != null);  
			}
	        });
	    dialogContents.add(normalText);
	    dialogContents.add(closeButton);
        dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

        // Return the dialog box
	    return dialogBox;
	}

	public void stopRenew(){
		t.cancel();
	}

	public void placeElements() {
		//linkButton
		addLinkButton.addStyleName("inline");
		addLinkButton.setHeight("19px");
		addLinkButton.setWidth("19px");			
		
		messagePanel.clear();
		messagePanel.setWidth("500px");
		messagePanel.setHorizontalAlignment(ALIGN_RIGHT);
		
		FlowPanel sendButtonPanel = new FlowPanel();
		sendButtonPanel.addStyleName("inline");
		sendButtonPanel.add(sendButton);			
		
		//FlowPanel resetButtonPanel = new FlowPanel();
		//resetButtonPanel.addStyleName("inline");
		//sendButtonPanel.add(resetButton);	
		
		
		//horisontalPanel.add(resetButtonPanel);
		horisontalPanel.add(sendButtonPanel);	
		horisontalPanel.add(addLinkButton);	
		horisontalPanel.add(simbols);
		
		FlowPanel fl = new FlowPanel();
		fl.setStyleName("clearBoth");			
		horisontalPanel.add(fl);
		
		messagePanel.add(getTextArea());
		messagePanel.add(horisontalPanel);
		
		//getTextArea().setText("");	
		final int i = 140-getTextArea().getText().length();
		simbols.setText(String.valueOf(i));
		checkLength(i);
		
		this.add(messagePanel);
	}

	private void init() {
		//dialogBox
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
	    dialogBox.setPopupPosition(120, 90);
	    dialogBox.addStyleName("dialog");

		//messagePanel
		messagePanel.setHorizontalAlignment(ALIGN_RIGHT);
		//horisontalPanel
		//horisontalPanel.setHorizontalAlignment(ALIGN_RIGHT);
		horisontalPanel.addStyleName("pt5");
	
		//symbols
		simbols.setStyleName("counter inline mr10");
		
		//sendButton
		sendButton.setTitle(ClientConstants.SENT_CTRL_ENTER);
		sendButton.addStyleName("h27");
		
		//addLinkButton
		addLinkButton.setTitle(ClientConstants.GET_SHORT_URL);
		
		//TextArea
		getTextArea().setStyleName("mytextArea");
		getTextArea().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				TextArea txt  = (TextArea) event.getSource();
				Integer length = ClientConstants.MAX_MESSAGE_LENGTH - txt.getText().length();
				checkLength(length);
			}
		});
		
		getTextArea().addKeyPressHandler(new SubmitHander());
		
		//resetButton
		resetButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getTextArea().setText("");				
				checkLength(140);
			}
		});
		
		//sendButton
		sendButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getTextArea().getText().trim().length() > 0 && getTextArea().getText().trim().length() <= 140){
					DecorationManager.showLoading(messagePanel);
					GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
					if(Registry.getInctance().getValue("sendtowall").equals(true)){
						VK.sendToWall(acc.getUid(), getTextArea().getText());
					}
					
					custService.sendMessage(acc, getTextArea().getText(), new AsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							getTextArea().setText("");
							DecorationManager.getInstance().showMain();	
						}
						
						@Override
						public void onFailure(Throwable e) {
							placeElements();
							e.printStackTrace();						
						}
					});
				}
			}
		});
		
		//addLinkButton
		addLinkButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
	            //dialogBox.center();
	            dialogBox.show();
	            if(globalNormalText != null)
	            	globalNormalText.setFocus(true);
			}
		});
	}

	public void checkLength(Integer length) {
		simbols.setText(length.toString());
		if(length >= 0 && length < 140){
			sendButton.setEnabled(true);
			resetButton.setEnabled(true);
			simbols.removeStyleName("error");
		}else{
			sendButton.setEnabled(false);
			resetButton.setEnabled(false);
			simbols.addStyleName("error");
			
		}
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public void checkLength() {
		checkLength(140 -getTextArea().getText().length());		
	}
	
	
	private class SubmitHander implements KeyPressHandler {

		@Override
		public void onKeyPress(KeyPressEvent event) {
			int keyCode = event.getNativeEvent().getKeyCode();
			if(event.isControlKeyDown() && keyCode == KeyCodes.KEY_ENTER){
				sendButton.click();
			}			
		}

	}
}
