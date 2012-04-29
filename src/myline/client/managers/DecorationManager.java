package myline.client.managers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
	private static final String ACCESS = "access";
	static private DecorationManager instance = null;
	final private RootPanel rootPannel = RootPanel.get("tweetsContainer");
	final private MainPage mainPage = new MainPage();
	final private GettingServiceAsync custService = (GettingServiceAsync) GWT.create(GettingService.class);
	
	final private Timer timer = new Timer() {
		@Override
		public void run() {
			DecorationManager.getInstance().refresh();
		}
	};	
	
	final private Timer refreshTimer = new Timer() {
		    @Override
		    public void run() {
		    	DecorationManager.getInstance().refresh();
		    }
		  };
	
	
	private DecorationManager(){		

	}
	
	 public static DecorationManager getInstance(){
		if(instance == null){
			instance = new DecorationManager();
		}
		return instance;
	}
	
	public void showLogin(){
		timer.cancel();
		custService.getInitiationURL(new AsyncCallback<UrlContaner>(){
			
			@Override
			public void onSuccess(UrlContaner result) {
				final AuthorisePanel auth = new AuthorisePanel(result);
					rootPannel.clear();
					rootPannel.add(auth);	
					rootPannel.setStyleName("loading");
					DecorationManager.fixSize();
			}
			
			@Override
			public void onFailure(Throwable exeption) {
				showError(exeption);		
			}
		});
	}

	public void showMain() {		
		final Access acc = (Access) Registry.getInctance().getValue(ACCESS);
		
		custService.getMessages(acc, new AsyncCallback<MessageContaner>() {		
		
			@Override
			public void onSuccess(MessageContaner result) {
				if(result!=null){
					final FlowPanel panelVert = new FlowPanel();
					for (Message element : result.getMessages()) {
						final ItemPanel ent = new ItemPanel(element, mainPage);
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
			public void onFailure(Throwable exeption) {
				showError(exeption);				
			}
		});
		

	}
	
	public void showLoading(){
		//loading animation
		Image image = new Image();
		image.setUrl("/images/ajax-loader.gif");
		image.setStyleName("loader_img");
		
		//TWITTERRISHKO
		Label twi_lab = new Label(ClientConstants.TWI_LABEL);
		twi_lab.setStyleName("center");
		
		rootPannel.clear();
		rootPannel.setStyleName("loading");
		rootPannel.add(image);
		rootPannel.add(twi_lab);
		
		DecorationManager.fixSize();
	}
	
	public static void stopLoading(Panel panel){
		panel.clear();
	}
	
	public static native void fixSize()/*-{
    	$wnd.fixSize();
	}-*/;

	public void showError(Throwable exception){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(os);
		exception.printStackTrace(out);
		String output = os.toString();
		custService.logError(output, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(Void result) {}			
		});
		
		//show info about error
		timer.cancel();
		
		Image image = new Image("/images/error.jpg");
		Label label = new Label(exception.getMessage().substring(0, Math.min(200, exception.getMessage().length())));
		label.setStyleName("error");
		rootPannel.clear();
		rootPannel.add(image);
		rootPannel.add(label);
		DecorationManager.fixSize();
	}
	
	public void refresh(){
		Access acc = (Access) Registry.getInctance().getValue(ACCESS);
		
		
		custService.getMessages(acc, new AsyncCallback<MessageContaner>() {		
		
			@Override
			public void onSuccess(MessageContaner result) {
				if(result!=null){
					@SuppressWarnings("unchecked")
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
			public void onFailure(Throwable exception) {
				mainPage.showError(ClientConstants.UPDATE_LINE_ERROR_MESSAGE);
				//showError(e);				
			}
		});
	}
	
	public void refresh(int delayMillis){
		refreshTimer.schedule(delayMillis);
	}	
}
