package myline.client.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myline.client.managers.DecorationManager;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.network.Message;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ItemPanel extends Composite {

	private static ItemPanelUiBinder uiBinder = GWT
			.create(ItemPanelUiBinder.class);
	@UiField Image avatar;
	@UiField Anchor name;
	@UiField Label nick_label;
	@UiField Label date_label;
	@UiField Label message;
	@UiField FlowPanel messagePanel;
	@UiField Anchor deleteAnchor;
	String id;
	MainPage mainPage;

	interface ItemPanelUiBinder extends UiBinder<Widget, ItemPanel> {
	}

	public ItemPanel( Message element, MainPage mainPage) {
		initWidget(uiBinder.createAndBindUi(this));
		avatar.setUrl(element.getUrl_img());
		name.setText(element.getNick());
		nick_label.setText(element.getNick());
		
		DateTimeFormat df;
		DateTimeFormat formatObj =  DateTimeFormat.getFormat("dd-MM-yyyy");
		Date date = element.getDate();
		Date now = new Date();
		if(formatObj.format(date).equals(formatObj.format(now))){
			df = DateTimeFormat.getFormat("HH:mm:ss");
		}else{
			df = DateTimeFormat.getFormat("HH:mm:ss dd-MM-yyyy");			
		}		
		date_label.setText(df.format(date));
		
		message.setText(element.getMessage());
		
		deleteAnchor.setVisible(element.getIsEditable());
		
		this.id = String.valueOf(element.getId());
		this.mainPage = mainPage;
	}

	@UiHandler("deleteAnchor")
	void onDeleteAnchorClick(ClickEvent event) {
		deleteAnchor.setVisible(false);
		this.setVisible(false);
		Access acc = (Access) Registry.getInctance().getValue("access");
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.deleteStatus(acc, this.id, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				ArrayList<Long> deletedList = (ArrayList<Long>) Registry.getInctance().getValue("deleted");
				if(deletedList == null){
					deletedList = new ArrayList<Long>();
				}
				deletedList.add(Long.parseLong(id));
				Registry.getInctance().setKey("deleted", deletedList);
				DecorationManager.getInstance().refresh();		
			}
			
			@Override
			public void onFailure(Throwable e) {
				mainPage.showError(ClientConstants.DELETE_TWIT_ERROR_MESSAGE);								
			}
		});		
	}
	@UiHandler("name")
	void onNameClick(ClickEvent event) {
		Anchor anc = (Anchor) event.getSource();
		String nick = anc.getText();
		String text = mainPage.sendTwitPanel.textArea.getText();
		if(text.length() != 0){
				text = text + " ";
		}
		mainPage.sendTwitPanel.textArea.setText(text + "@" + nick + " ");
		mainPage.sendTwitPanel.textArea.setFocus(true);
		mainPage.sendTwitPanel.checkLength();
	}
}
