package ac.liv.csc.comp201;

import java.security.Permission;

import ac.liv.csc.comp201.control.Coin;
//import ac.liv.csc.comp201.control.Strings;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.CountrySelector;
import ac.liv.csc.comp201.simulate.MachineSimulator;
import ac.liv.csc.comp201.test.Tester;


public class Main {
	
	private static MachineController controller; 
	
	public static void closeDown() {
		controller.stopController();
	}
	
	public static MachineController getNewController() {
		if (controller!=null) {
		   controller.stopController();
		}
		controller=new MachineController();
		return(controller);
	}
	
	public static void main(String[] args) {
		Coin.setCurrencyCode("GBP");
		System.out.println("Coin image names is "+Coin.getCoinImageNames());
		try {
			//Strings.setLanguage("en");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tester tester=null;
		IMachine machine=new MachineSimulator();
		getNewController();
		System.out.println("len is "+args.length);
		if (args.length>=1) {
			System.out.println("args[0] is "+args[0]);
			if (args[0].toLowerCase().trim().equals("test")) {
				tester=new Tester();				
			}
			
				
		}
		for (int idx=0;idx<args.length;idx++) {
			if (args[idx].indexOf("altcup")!=-1) {
				System.out.println("Alt cup logic enabled");
				tester.altCupLogic();
			}
			if (args[idx].indexOf("fourprefix")!=-1) {
				System.out.println("Four prefix enabled");
				tester.setFourPrefix();
			}
			
			if (args[idx].indexOf("confirm=")!=-1) {
				String confirm=args[idx].substring(("confirm=").length());
				System.out.println("Confirm is "+confirm);
				tester.setConfirm(confirm);
			}
		}
		
		machine.start(controller,tester); // start machine and pass control to machine controller
		
		
	}
}
