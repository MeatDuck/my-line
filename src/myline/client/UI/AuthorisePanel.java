package myline.client.UI;

import myline.shared.network.UrlContaner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;

public class AuthorisePanel extends Composite {

	private static AuthorisePanelUiBinder uiBinder = GWT
			.create(AuthorisePanelUiBinder.class);
	@UiField Anchor link;

	interface AuthorisePanelUiBinder extends UiBinder<Widget, AuthorisePanel> {
	}

	public AuthorisePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public AuthorisePanel(UrlContaner contaner) {
		initWidget(uiBinder.createAndBindUi(this));
		link.setHref(contaner.getUrl());
	}
}
