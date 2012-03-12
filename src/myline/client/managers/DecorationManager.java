package myline.client.managers;

import java.util.ArrayList;

import myline.client.UI.AuthorisePanel;
import myline.client.UI.ItemPanel;
import myline.client.UI.MainPage;
import myline.client.service.GettingService;
import myline.client.service.GettingServiceAsync;
import myline.shared.ClientConstants;
import myline.shared.Registry;
import myline.shared.network.MessageContaner;
import myline.shared.network.Message;
import myline.shared.network.UrlContaner;
import myline.shared.security.Access;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public final class DecorationManager {
	static private DecorationManager instance = null;
	final private RootPanel rootPannel = RootPanel.get("tweetsContainer");
	final private MainPage mainPage = new MainPage();
	
	private Timer timer = new Timer() {
		@Override
		public void run() {
			DecorationManager.getInstance().refresh();
		}
	};	
	
	private Timer refreshTimer = new Timer() {
		    @Override
		    public void run() {
		    	DecorationManager.getInstance().refresh();
		    }
		  };
	
	
	private DecorationManager(){		

	}
	
	static public DecorationManager getInstance(){
		if(instance == null){
			instance = new DecorationManager();
		}
		return instance;
	}
	
	public void showLogin(){
		timer.cancel();
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.getInitiationURL(new AsyncCallback<UrlContaner>(){
			
			@Override
			public void onSuccess(UrlContaner result) {
				AuthorisePanel auth = new AuthorisePanel(result);
				rootPannel.clear();
				rootPannel.add(auth);	
				rootPannel.setStyleName("loading");
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
		
			@Override
			public void onSuccess(MessageContaner result) {
				if(result!=null){
					FlowPanel panelVert = new FlowPanel();
					for (Message element : result.getMessages()) {
						ItemPanel ent = new ItemPanel(element, mainPage);
						panelVert.add(ent);
				}					
					rootPannel.setStyleName("general_bg");
					rootPannel.clear();
					
					mainPage.twitItems.add(panelVert);
					rootPannel.add(mainPage);
				}
				DecorationManager.fixSize();
				timer.scheduleRepeating(ClientConstants.TIMER_CALL_TIME);
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
	
	public static void stopLoading(Panel c){
		c.clear();
	}
	
	public static native void fixSize()/*-{
    	$wnd.fixSize();
	}-*/;

	public void showError(Throwable e){
		//show info about error
		timer.cancel();
		
		Image im = new Image("/images/error.jpg");
		Label label = new Label(e.getMessage().substring(0, Math.min(200, e.getMessage().length())));
		label.setStyleName("error");
		rootPannel.clear();
		rootPannel.add(im);
		rootPannel.add(label);
		DecorationManager.fixSize();
	}
	
	public void refresh(){
		Access acc = (Access) Registry.getInctance().getValue("access");
		
		GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
		custService.getMessages(acc, new AsyncCallback<MessageContaner>() {		
		
			@Override
			public void onSuccess(MessageContaner result) {
				if(result!=null){
					ArrayList<String> deletedList = (ArrayList<String>) Registry.getInctance().getValue("deleted");
					FlowPanel panelVert = new FlowPanel();
					for (Message element : result.getMessages()) {
						if(deletedList == null || !deletedList.contains(element.getId())){
							ItemPanel ent = new ItemPanel(element, mainPage);
							panelVert.add(ent);
						}
				}					
					rootPannel.setStyleName("general_bg");
					rootPannel.clear();
					
					mainPage.twitItems.clear();
					mainPage.twitItems.add(panelVert);
					rootPannel.add(mainPage);
				}
				DecorationManager.fixSize();
			}
			
			@Override
			public void onFailure(Throwable e) {
				mainPage.showError(ClientConstants.UPDATE_LINE_ERROR_MESSAGE);
				//showError(e);				
			}
		});
	}
	
	public void refresh(int delayMillis){
		refreshTimer.schedule(delayMillis);
	}	
}
