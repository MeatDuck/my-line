package myline.client.managers;

final public class VK {
	
	private VK(){
		
	}

	public static native void sendToWall(String user, String txt)/*-{
        	$wnd.sendMessage(user, txt);
    }-*/;
}
