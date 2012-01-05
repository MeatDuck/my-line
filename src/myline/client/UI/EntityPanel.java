package myline.client.UI;

import java.util.Date;

import myline.client.managers.DecorationManager;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class EntityPanel extends FlowPanel {
	final private Image avatar = new Image();
	final private FlowPanel messagePanel = new FlowPanel();
	final private Label nick_label = new Label();
	final private Label date_label = new Label();
	final private Anchor name = new Anchor();
	final private Label message = new Label();
	
	private EntityPanel(){
		super();
	}
	
	public EntityPanel(String url_img, 
			           String nick, 
			           String linkname, 
			           String mes,
			           final String id,
			           boolean ismy ,
			           String datetime ){
		this();
		init();
		avatar.setUrl(url_img);
		getName().setText(nick);
		getName().setHTML("<strong>" + nick + "</strong>");
		nick_label.setText(linkname);
		
		DateTimeFormat df;
		DateTimeFormat format =  DateTimeFormat.getFormat("dd-MM-yyyy");
		Date date = new Date(Long.parseLong(datetime));
		Date now = new Date();
		if(format.format(date).equals(format.format(now))){
			df = DateTimeFormat.getFormat("HH:mm:ss");
		}else{
			df = DateTimeFormat.getFormat("HH:mm:ss dd-MM-yyyy");			
		}
		
		date_label.setText(df.format(date));
		message.setText(mes);
		HorizontalPanel tmp2 = new HorizontalPanel();
		tmp2.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		tmp2.add(getName());
		tmp2.add(nick_label);
		messagePanel.add(tmp2);		
		messagePanel.add(message);
		final FlowPanel tmp = new FlowPanel();
		tmp.setHeight("13px");
		tmp.add(date_label);
		if(ismy){
			final Anchor w = new Anchor("<span><i></i><b>" + ClientConstants.DELETE_TEXT + "</b></span>", true);
			w.setStyleName("delete");
			tmp.add(w);
			
			w.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					w.setVisible(false);
					Access acc = (Access) Registry.getInctance().getValue("access");
					GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
					custService.deleteStatus(acc, id, new AsyncCallback<Void>() {
						@Override
						public void onSuccess(Void result) {
							DecorationManager.getInstance().showMain();		
						}
						
						@Override
						public void onFailure(Throwable e) {
							e.printStackTrace();							
						}
					});					
				}
			});
		}
		
		messagePanel.add(tmp);
	
		this.add(avatar);
		this.add(messagePanel);
	}

	private void init() {
		this.setStyleName("tweet");	
		avatar.setStyleName("tweet-image");
		nick_label.setStyleName("tweet-user-name");
		message.setStyleName("tweet-text");
		messagePanel.setStyleName("messagePanel");
		name.setStyleName("link");
		date_label.setStyleName("dateL");
	}

	public Anchor getName() {
		return name;
	}
}
