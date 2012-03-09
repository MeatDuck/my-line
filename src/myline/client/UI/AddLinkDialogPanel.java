package myline.client.UI;

import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkDialogPanel extends Composite  {

	public interface AddLinkDialogPanelUiBinder extends UiBinder<Widget, AddLinkDialogPanel> {

	}
	private static AddLinkDialogPanelUiBinder uiBinder = GWT
			.create(AddLinkDialogPanelUiBinder.class);
	@UiField Button okButton;
	@UiField TextBox linkTextBox;
	DialogBox dialogBox;
	SendTwitPanel twitPanel;

	public AddLinkDialogPanel(DialogBox dialogBox, SendTwitPanel twitPanel) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dialogBox = dialogBox;
		this.twitPanel = twitPanel;
	}


	public AddLinkDialogPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("okButton")
	void onOkButtonClick(ClickEvent event) {
		String linkTxt = linkTextBox.getText();
		if(isRigthLink(linkTxt)){
			String text = twitPanel.textArea.getText();
			if(text.length() != 0){
				text = text + " ";
			}
			final String text2 = text;
			
			//RPC get Link
			GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
			custService.takeShortLink(linkTxt, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					twitPanel.textArea.setText(text2 + result + " ");
					twitPanel.textArea.setFocus(true);
					twitPanel.checkLength();								
				}
				
				@Override
				public void onFailure(Throwable caught) {	
				}
			});


		}
        dialogBox.hide();
        linkTextBox.setText("");
	}
	
	private boolean isRigthLink(String linkTxt) {
		String regex = "^(http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		RegExp patt = RegExp.compile(regex);
		MatchResult exRes = patt.exec(linkTxt);
        return (exRes != null);  
	}
}
