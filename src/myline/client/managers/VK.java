package myline.client.managers;

public class VK {

	public static native void sendToWall(String user, String txt)/*-{
        	$wnd.sendMessage(user, txt);
    }-*/;
}
