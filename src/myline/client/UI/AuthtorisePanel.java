package myline.client.UI;

import myline.shared.network.UrlContaner;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;

public class AuthtorisePanel extends FlowPanel {
	final private Anchor link = new Anchor();
	
	private AuthtorisePanel() {
		super();
	}
	
	public AuthtorisePanel(UrlContaner contaner){
		this();
		link.setText(contaner.getLabeString());
		link.setHref(contaner.getUrl());
		this.setStyleName("label_center");
		link.setTarget("_top");
		this.add(link);
		
	}
}
