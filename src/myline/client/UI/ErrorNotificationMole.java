package myline.client.UI;

import myline.shared.ClientConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.NotificationMole;

public class ErrorNotificationMole extends Composite implements HasText {

	  Timer showTimer = new Timer() {
		    @Override
		    public void run() {
		    	hide();
		    }
		  };
		  
	private static ErrorNotificationMoleUiBinder uiBinder = GWT
			.create(ErrorNotificationMoleUiBinder.class);
	@UiField NotificationMole errorMessage;

	interface ErrorNotificationMoleUiBinder extends
			UiBinder<Widget, ErrorNotificationMole> {
	}

	public ErrorNotificationMole() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getText() {
		return errorMessage.getTitle();
	}

	@Override
	public void setText(String text) {
		errorMessage.setMessage(text);
		errorMessage.setTitle(text);
	}
	
	public void show(){
		errorMessage.show();
		showTimer.schedule(ClientConstants.TIMER_ERROR_SHOW_TIME);
	}
	
	public void hide(){
		errorMessage.hide();
	}
}
