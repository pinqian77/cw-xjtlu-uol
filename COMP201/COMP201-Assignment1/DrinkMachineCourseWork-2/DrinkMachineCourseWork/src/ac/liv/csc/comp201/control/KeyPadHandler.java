package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IKeyPad;
import ac.liv.csc.comp201.simulate.Cup;

public class KeyPadHandler {
	// 
	private static final int MAX_CODE_LEN = 4;
	private static final int MIN_CODE_LEN = 3;
	private String orderCode = "";
	private boolean gotPrefix=false;
	int codeLen=MIN_CODE_LEN;
	private char MEDIUM_CUP_PREFIX = '5';
	private char LARGE_CUP_PREFIX = '6';
	private int RETURN_CHANGE_BUTTON = 9;
	
	private int cupTypeSelected=Cup.SMALL_CUP;
	
	private boolean returnChange=false;

	/**
	 * 
	 * @return the status of the return change button, once returned true it resets itself back to wait for another key press
	 */
	public boolean returnChangePressed() {
		boolean ret=returnChange;
		returnChange=false;
		return(ret);
	}
	
	public int getCupTypeSelected() {
		return(cupTypeSelected);
	}
	
	public String getDrinkCode() {
		if (orderCode.length()<MIN_CODE_LEN) return(null);
		
		if (orderCode.length()==MAX_CODE_LEN) {
			return(orderCode.substring(1));
		}
		if (gotPrefix) {
			return null;
		}
		// at this point we know the prefix had been added to the begining, so strip it off
		return(orderCode);
	}
	
	
	
	public void handleKeyPresses(IKeyPad keyPad) {
		int keyCode = keyPad.getNextKeyCode();
		if (keyCode != -1) {
			if (keyCode == RETURN_CHANGE_BUTTON) {
                 returnChange=true;
                 gotPrefix=false;
                 orderCode="";			// clear out buffer
			} else {
				codeLen = MIN_CODE_LEN;
				if (orderCode.length()==0) {
					orderCode+= keyCode;
					return;					// for first char just get character
				}
				if (orderCode.charAt(0) == LARGE_CUP_PREFIX) {
					cupTypeSelected=Cup.LARGE_CUP;
					gotPrefix=true;
					codeLen = MAX_CODE_LEN;
				}
				if (orderCode.charAt(0) == MEDIUM_CUP_PREFIX) {
					codeLen = MAX_CODE_LEN;
					gotPrefix=true;
					cupTypeSelected=Cup.MEDIUM_CUP;
				}
				if (!gotPrefix) {
					cupTypeSelected=Cup.SMALL_CUP;
				}
				
				if (orderCode.length()<codeLen) {
					orderCode+= keyCode;
				}
				
			}

		}
	}

	/**
	 * @return the orderCode
	 */
	public final String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public final void setOrderCode(String orderCode) {
		gotPrefix=false;
		this.orderCode = orderCode;
	}
}
