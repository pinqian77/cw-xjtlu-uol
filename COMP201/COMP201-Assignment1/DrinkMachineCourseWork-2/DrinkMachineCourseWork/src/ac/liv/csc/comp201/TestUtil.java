package ac.liv.csc.comp201;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

import java.util.Hashtable;


public class TestUtil {

	private static Hashtable <IMachine,TestUtil>  instances=new Hashtable<IMachine,TestUtil>();
	
	private TestUtil(IMachine machine) {
		this.machine=machine;		
	}
	
	public static synchronized TestUtil getInstance(IMachine machine) {
		if (!instances.containsKey(machine)) {
			instances.put(machine, new TestUtil(machine));
		} 
		return(instances.get(machine));
	}
	
	private IMachine machine;
	
	
	private void setCoinHoppersLow() {
		System.out.println("Setting hoppers low");
		machine.getCoinHandler().setHopperLevel("ab",1);
		machine.getCoinHandler().setHopperLevel("ac",1);
		machine.getCoinHandler().setHopperLevel("ba",1);
		machine.getCoinHandler().setHopperLevel("bc",1);
		machine.getCoinHandler().setHopperLevel("bd",1);
		machine.getCoinHandler().setHopperLevel("ef",1);
		System.out.println("Set hoppers low");
	}
	
	private void setCoinHoppersHigh() {
		machine.getCoinHandler().setHopperLevel("ab",100);
		machine.getCoinHandler().setHopperLevel("ac",100);
		machine.getCoinHandler().setHopperLevel("ba",100);
		machine.getCoinHandler().setHopperLevel("bc",100);
		machine.getCoinHandler().setHopperLevel("bd",100);
		machine.getCoinHandler().setHopperLevel("ef",100);
	}
	
	boolean coinsLow=false;
	
	
	private void toggleCoinHoppers() {
		if (coinsLow) {
			setCoinHoppersHigh();
		} else {
			setCoinHoppersLow();
		}
		coinsLow=(!(coinsLow));
		if (this.coinsLow) {
				machine.getDisplay().setTextString("Coins Low");
			} else {
				machine.getDisplay().setTextString("Coins High");
			}
		try {
			Thread.sleep(2000);
		} catch (Exception exc) {
		}
			
	}
	
	int testPressed=0;
	public boolean doTests(int key,int targetKey) {
		dumpCup();
		if (key!=targetKey) return(false);
		testPressed++;
		
		if (testPressed>4) {
			machine.getWaterHeater().forceError();
			machine.getDisplay().setTextString("Water heater forced  error");
			try {
				Thread.sleep(2000);
			} catch (Exception exc) {
			}
			machine.getDisplay().setTextString("");
			return(true);
		}
			toggleCoinHoppers();
		
		return(false);
	}

	private double oldWaterLevel;
	private double oldIngredientLevels[]=new double[Hoppers.CHOCOLATE+2];
	private Cup currentCup=new Cup(Cup.SMALL_CUP);
	
	private void showCup() {
		System.out.println("Water level "+currentCup.getWaterLevelLitres());
		System.out.println("Coffee level "+currentCup.getCoffeeGrams());
		System.out.println("Sugar level "+currentCup.getSugarGrams());
		System.out.println("Choc level "+currentCup.getChocolateGrams());
		System.out.println("Milk level "+currentCup.getMilkGrams());
		System.out.println("Temp "+currentCup.getTemperatureInC());
	}
	private void dumpCup() {
		boolean dump=false;
		if (machine.getCup()==null) return;	// cannot dump null Cup
		if (machine.getCup()!=currentCup) {
			oldWaterLevel=0;
			for (int idx=0;idx<this.oldIngredientLevels.length;idx++) {
				oldIngredientLevels[idx]=0;
			}
			currentCup=machine.getCup();
		}
		if (currentCup==null) return;
		if (currentCup.getWaterLevelLitres()!=oldWaterLevel) {
			oldWaterLevel=currentCup.getWaterLevelLitres();
			dump=true;
		}
		if (currentCup.getCoffeeGrams()!=this.oldIngredientLevels[Hoppers.COFFEE]) {
			oldIngredientLevels[Hoppers.COFFEE]=currentCup.getCoffeeGrams();
			dump=true;
		}
		if (currentCup.getChocolateGrams()!=this.oldIngredientLevels[Hoppers.CHOCOLATE]) {
			oldIngredientLevels[Hoppers.CHOCOLATE]=currentCup.getChocolateGrams();
			dump=true;
		}
		if (currentCup.getSugarGrams()!=this.oldIngredientLevels[Hoppers.SUGAR]) {
			oldIngredientLevels[Hoppers.SUGAR]=currentCup.getSugarGrams();
			dump=true;
		}
		if (currentCup.getMilkGrams()!=this.oldIngredientLevels[Hoppers.MILK]) {
			oldIngredientLevels[Hoppers.MILK]=currentCup.getMilkGrams();
			dump=true;
		}		
		if (dump) {
			showCup();
		}
	}

	
	
}
