package myline.client.UI;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class AddLinkButton extends PushButton {
	final static Image im = new Image("/images/add_link.png");
	
	public AddLinkButton() {
		super(im);
		im.setWidth("17px");
		im.setHeight("17px");
	}
	
	public AddLinkButton(String label){
		super(label, label);
	}
}
