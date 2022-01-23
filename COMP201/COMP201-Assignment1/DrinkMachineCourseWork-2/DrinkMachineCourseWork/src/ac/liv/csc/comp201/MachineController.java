package ac.liv.csc.comp201;

import ac.liv.csc.comp201.control.CalibrationHelper;
import ac.liv.csc.comp201.control.CoinHandlerManager;
import ac.liv.csc.comp201.control.MessageHandler;
import ac.liv.csc.comp201.control.HopperManager;
import ac.liv.csc.comp201.control.KeyPadHandler;
import ac.liv.csc.comp201.control.MakeDrinkManager;
import ac.liv.csc.comp201.control.Strings;
import ac.liv.csc.comp201.control.WaterHeaterController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.model.IMachineController;
import ac.liv.csc.comp201.model.IWaterHeater;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class MachineController extends Thread implements IMachineController {

	private boolean running = true;

	private long idleTime = 0;

	// A series of singletons to controller the water, hoppers, drink making,
	// coins, key pads etc.
	private WaterHeaterController waterHeaterController = new WaterHeaterController();
	private HopperManager hopperManager = new HopperManager();
	private CoinHandlerManager coinHandlerManager = new CoinHandlerManager();
	private MakeDrinkManager makeDrinkManager = new MakeDrinkManager();
	private KeyPadHandler keyPadHandler = new KeyPadHandler();
	private MessageHandler messageHandler = new MessageHandler();

	private IMachine machine;
	
	private Watchdog watchdog;

	private static final boolean CALIBRATE = false;

	private static final String version = "1.22";

	public void startController(IMachine machine) {
		watchdog=new Watchdog(machine);
		watchdog.start();
		this.machine = machine; // Machine that is being controlled
		machine.getWaterHeater().setHeaterOff();
		if (CALIBRATE) {
			waterHeaterController.startCalibration();
			hopperManager.setCalibrating(true, machine);
		}
		machine.getKeyPad().setCaption(9,"Cancel/return change");
		machine.getKeyPad().setCaption(7,"");
		machine.getKeyPad().setCaption(8,"");
		
		
		super.start();
	}

	public MachineController() {

	}

	private void monitorTemperature(IWaterHeater heater) {
		float temp = heater.getTemperatureDegreesC();

	}

	private static final int MAX_CODE_LEN = 4;
	private static final int MIN_CODE_LEN = 3;
	private int orderCode[] = new int[MAX_CODE_LEN];
	private int keyCodeCount = 0;
	private int MEDIUM_CUP_PREFIX = 5;
	private int LARGE_CUP_PREFIX = 6;
	private int RETURN_CHANGE_BUTTON = 9;

	private String convertToMoneyDisplay(int cashAmount) {
		String returnString = "" + cashAmount / 100 + ".";
		int penceAmount = cashAmount % 100; // get the pence amount
		if (penceAmount < 10) {
			returnString = returnString + "0"; // leading zero to allow 2dp
												// display
		}
		return (returnString + penceAmount);
	}

	
	
	private synchronized void runStateMachine() {
		watchdog.rseetDeadCounter();
		boolean shut = false;
		if (machine.getWaterHeater().getTemperatureDegreesC() >= 100) {
			shut = true;
		}
		if (shut) {
			machine.shutMachineDown(); // power down machine
			machine.getDisplay().setTextString(Strings.getString(Strings.SHUTDOWN));
			System.exit(1);
			return;
		}
		if (!hopperManager.canVend(machine)) {
			machine.getDisplay().setTextString(Strings.getString(Strings.OUTOFSERVICE));
			machine.getCoinHandler().lockCoinHandler();  // lock out mechanism
			return;		// cannot use the machine
		} else {
			machine.getCoinHandler().unlockCoinHandler();
		}
		waterHeaterController.controlWaterHeater(machine);
		hopperManager.controlHoppers(machine);
		if ((waterHeaterController.isCalibrating()) || (hopperManager.isCalibrating())) {
			machine.getDisplay().setTextString(Strings.getString(Strings.CALIBRATING));
			return;
		}
		monitorTemperature(machine.getWaterHeater());
		keyPadHandler.handleKeyPresses(machine.getKeyPad());
		coinHandlerManager.handleCoinsIn(machine);
		if (keyPadHandler.returnChangePressed()) {
			coinHandlerManager.returnChange(machine);
			
		}
		if (messageHandler.showMessages(machine.getDisplay())) {
			return;
		}
		if (idleTime > System.currentTimeMillis()) {
			return; // used to allow messages to be displayed for a short time
		}
		String displayString = Strings.getString(Strings.BALANCE) + " " + convertToMoneyDisplay(machine.getBalance());
		displayString += "       ".substring(keyPadHandler.getOrderCode().length());
		displayString += keyPadHandler.getOrderCode();
		machine.getDisplay().setTextString(displayString);
		makeDrinkManager.makeDrinks(keyPadHandler, machine, this, waterHeaterController, messageHandler, hopperManager);
		if (machine.getCup()!=null) {
			if (machine.getCup().getWaterLevelLitres()==0) {
				// hot new cup
			}
		}
	}

	
	private void returnCurrentBalance() {
		// TODO Auto-generated method stub

	}

	public void run() {
		// Controlling thread for coffee machine
		int counter = 1;
		while (running) {
			// machine.getDisplay().setTextString("Running drink machine
			// controller "+counter);
			counter++;
			try {
				Thread.sleep(10); // Set this delay time to lower rate if you
									// want to increase the rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runStateMachine();
		}
	}

	public void updateController() {
		// runStateMachine();
	}

	public void stopController() {
		running = false;
	}

	/**
	 * @return the idleTime
	 */
	public final long getIdleTime() {
		return idleTime;
	}

	/**
	 * @param idleTime
	 *            the idleTime to set
	 */
	public final void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}
	
	

}


class Watchdog extends Thread {
	private int deadCounter=0;
	IMachine machine;
	public Watchdog(IMachine machine) {
		this.machine=machine;
	}
	public void rseetDeadCounter() {
		deadCounter=0;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			deadCounter++;
			if (deadCounter>=2) {
				machine.getDisplay().setTextString("Machine locked up!!!");
			}
			
		}
	}
}