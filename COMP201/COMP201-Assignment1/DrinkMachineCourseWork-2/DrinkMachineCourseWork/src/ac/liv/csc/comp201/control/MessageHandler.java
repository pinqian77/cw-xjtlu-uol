package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IDisplay;

public class MessageHandler {
	private long hideTime;
	private long showTime;
	private String errorMessage="";
	private static final long SHOWDELAY=1*1000;
	private static final long HIDEDELAY=3*1000;
	
	public boolean showMessages(IDisplay display) {
		long now=System.currentTimeMillis();
		if (now>hideTime) {
			return(false);		// done showing
		}
		if (now<showTime) {
			return(true);		// waiting for show... so idle until showing and done
		}
		display.setTextString(errorMessage);
		return(true);
	}
	
	public void setMessage(String message) {
		errorMessage=message;
		showTime=System.currentTimeMillis()+SHOWDELAY;
		hideTime=showTime+HIDEDELAY;
	}

}
