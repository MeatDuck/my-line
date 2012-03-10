package myline.client.UI;

import myline.shared.Registry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsPanel extends Composite {

	private static SettingsPanelUiBinder uiBinder = GWT
			.create(SettingsPanelUiBinder.class);
	@UiField CheckBox sendToWallChb;
	@UiField InlineLabel askCookieLabel;
	@UiField Button closeButton;
	DialogBox dialogBox;
	
	interface SettingsPanelUiBinder extends UiBinder<Widget, SettingsPanel> {
	}

	public SettingsPanel(DialogBox db) {
		initWidget(uiBinder.createAndBindUi(this));
		dialogBox = db;
	}

	public SettingsPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("closeButton")
	void onCloseButtonClick(ClickEvent event) {
        dialogBox.hide();
        if(sendToWallChb.getValue()){
        	Cookies.setCookie("sendtowall", "true");
        	Registry.getInctance().setKey("sendtowall", true);
        }else{
        	Cookies.setCookie("sendtowall", "false");
        	Registry.getInctance().setKey("sendtowall", false);
        }
	}
}
