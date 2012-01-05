package myline.client.managers;

import java.util.List;

import myline.client.UI.AddPanel;
import myline.client.UI.AuthtorisePanel;
import myline.client.UI.EntityPanel;
import myline.client.UI.MainPanel;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.network.MessageContaner;
import myline.shared.network.UrlContaner;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class DecorationManager {
	static private DecorationManager instance = null;
	final private RootPanel rootPannel;
	final private AddPanel addPanel;
	
	private DecorationManager(){		
		rootPannel = RootPanel.get("tweetsContainer");
		addPanel = new AddPanel((Access) Registry.getInctance().getValue("access"));
	}
	
	static public DecorationManager getInstance(){
		if(instance == null){
			instance = new DecorationManager();
		}
		return instance;
	}
	
	public void showLogin(){
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.getInitiationURL(new AsyncCallback<UrlContaner>(){
			
			@Override
			public void onSuccess(UrlContaner result) {
				AuthtorisePanel auth = new AuthtorisePanel(result);
				rootPannel.clear();
				rootPannel.add(auth);	
				DecorationManager.fixSize();
			}
			
			@Override
			public void onFailure(Throwable e) {
				showError(e);		
			}
		});
	}

	public void showMain() {
		Access acc = (Access) Registry.getInctance().getValue("access");
		
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.getMessages(acc, new AsyncCallback<MessageContaner>() {		
		
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(MessageContaner result) {
				if(result!=null){
					FlowPanel panelVert = new FlowPanel();
					for (Object element : result.getMessages()) {
						List item = (List) element;
						EntityPanel ent = new EntityPanel(
								item.get(2).toString(), 
								item.get(0).toString(), 
								item.get(1).toString(), 
								item.get(3).toString(),
								item.get(4).toString(),
								(Boolean) item.get(5),
								item.get(6).toString());
						ent.getName().addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								Anchor anc = (Anchor) event.getSource();
								String nick = anc.getText();
								String text = addPanel.getTextArea().getText();
								if(text.length() != 0){
									text = text + " ";
								}
								addPanel.getTextArea().setText(text + "@" + nick + " ");
								addPanel.getTextArea().setFocus(true);
								addPanel.checkLength();
							}
						});
				panelVert.add(ent);
				}
					rootPannel.setStyleName("general_bg");
					addPanel.placeElements();
					MainPanel panel = new MainPanel(addPanel, panelVert);
					rootPannel.clear();
					rootPannel.add(panel);
				}
				DecorationManager.fixSize();
			}
			
			@Override
			public void onFailure(Throwable e) {
				showError(e);				
			}
		});
		

	}
	
	public void showLoading(){
		//loading animation
		Image im = new Image();
		im.setUrl("/images/ajax-loader.gif");
		im.setStyleName("loader_img");
		
		//TWITTERRISHKO
		Label twi_lab = new Label(ClientConstants.TWI_LABEL);
		twi_lab.setStyleName("center");
		
		rootPannel.clear();
		rootPannel.setStyleName("loading");
		rootPannel.add(im);
		rootPannel.add(twi_lab);
		
		DecorationManager.fixSize();
	}
	
	public static void showLoading(VerticalPanel c){
		Image im = new Image("/images/ajax-loader.gif");
		c.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		c.clear();
		c.add(im);
	}
	
	public static void showLoading(HorizontalPanel c){
		Image im = new Image("/images/ajax-loader.gif");
		c.clear();
		c.add(im);
	}
	
	public static void stopLoading(Panel c){
		c.clear();
	}
	
	public static native void fixSize()/*-{
    	$wnd.fixSize();
	}-*/;

	public void showError(Throwable e){
		//show info about error
		if(addPanel != null)
			addPanel.stopRenew();
		
		Image im = new Image("/images/error.jpg");
		Label label = new Label(e.getMessage().substring(0, Math.min(200, e.getMessage().length())));
		label.setStyleName("error");
		rootPannel.clear();
		rootPannel.add(im);
		rootPannel.add(label);
		DecorationManager.fixSize();
	}
}
