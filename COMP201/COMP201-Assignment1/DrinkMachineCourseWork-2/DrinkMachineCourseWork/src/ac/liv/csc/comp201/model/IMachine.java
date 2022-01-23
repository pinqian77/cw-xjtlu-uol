package ac.liv.csc.comp201.model;

import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.test.Tester;

public interface IMachine {

	public IDisplay getDisplay();
	
	public IKeyPad getKeyPad();
	
	public IWaterHeater getWaterHeater();
	
	void start(IMachineController controller,Tester tester);		// Start's machine up
	
	/**
	 * This method is called everytime a key is pressed on the key pad
	 */
	void keyPadPressed();
	
	public void shutdownEnable();
	
	public Cup getCup();
	
	public void vendCup(int cupType);
	
	public long getCurrentTime();
	
	public IHoppers getHoppers();
	
	public void resetCoinHandler();
	
	public ICoinHandler getCoinHandler();
	
	public void shutMachineDown();
	
	public boolean isDead();
	
	public int getBalance();
	
	public void setBalance(int balance);
	
	public void showbanlance(String m);
	
	
	
}
