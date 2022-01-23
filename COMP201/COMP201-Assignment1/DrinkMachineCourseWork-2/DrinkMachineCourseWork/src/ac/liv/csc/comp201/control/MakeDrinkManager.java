package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;

public class MakeDrinkManager {
	
	private static final int IDLE=0;
	private static final int PREHEATING=1;
	private static final int MIXING=2;
	private static final int TOPINGUP=3;
	
	private int state=IDLE;
	
	private Recipe recipe=null;
	
	public void makeDrinks(KeyPadHandler keyPadHandler, IMachine machine, MachineController machineController,
			WaterHeaterController waterHeaterController, MessageHandler messageHandler, HopperManager hopperManager) {
		switch (state) {
		case IDLE:
			checkForNewDrinks(keyPadHandler, machine, machineController, messageHandler);
			break;
		case PREHEATING:
			waterHeaterController.doPreHeat(recipe);
			if (machine.getWaterHeater().getTemperatureDegreesC() >= recipe.getMakeTemperature()) {
				// now start to add ingredients and some hot water
				// we can only starting vending 
				if (machine.getCup()!=null) {
				    hopperManager.vendDrinks(recipe, machine); // start the process
					   										// of adding in the
															// ingredients
				machine.getWaterHeater().setHotWaterTap(true); // start adding
																// in some hot
																// water
				
				state = MIXING;
				}
			}
			break;

		case MIXING:
			if ((waterHeaterController.finishedDrink()) && (!hopperManager.isVending())) {
				keyPadHandler.setOrderCode(""); // ready for another code..
                dumpCup(machine.getCup());		
                hopperManager.recalibrate(recipe, machine.getCup());
                messageHandler.setMessage(Strings.getString(Strings.DRINKREADY));
				state=IDLE;		// all done now
			}
			break;
		}

	}

	private void dumpCup(Cup cup) {
		System.out.println("Temp is "+cup.getTemperatureInC());
		System.out.println("Coffee level "+cup.getCoffeeGrams());
		System.out.println("Water level is "+cup.getWaterLevelLitres());
		System.out.println("Sugar level is "+cup.getSugarGrams());
		System.out.println("Milk level is "+cup.getMilkGrams());
		System.out.println("Choc level is "+cup.getChocolateGrams());
	}


	private void checkForNewDrinks(KeyPadHandler keyPadHandler, IMachine machine, MachineController machineController, MessageHandler messageHandler) {
		if (keyPadHandler.getDrinkCode()!=null) {
			recipe=Recipe.getRecipeFromCode(keyPadHandler.getDrinkCode());
			if (recipe==null) {
				messageHandler.setMessage(Strings.getString(Strings.INVALID));
				keyPadHandler.setOrderCode("");  // reset bad code
				return;
			}
			recipe.setCupSize(keyPadHandler.getCupTypeSelected()); // set the cup size for the recipe
			System.out.println("Cup size is "+keyPadHandler.getCupTypeSelected());
			if (machine.getBalance()<recipe.getPriceOfDrink()) {
				messageHandler.setMessage(Strings.getString(Strings.CASHLOW));
				keyPadHandler.setOrderCode("");  // reset bad code
				return;
			}
			if (!recipe.available(machine)) {
				messageHandler.setMessage(Strings.getString(Strings.UNAVAILABLE));
				keyPadHandler.setOrderCode("");  // reset bad code
				return;	
			}
			// we can now make the drink
			// so first pay for drink
			machine.setBalance(machine.getBalance()-recipe.getPriceOfDrink());
			state=PREHEATING;
			System.out.println("Vending cup "+keyPadHandler.getCupTypeSelected());
			machine.vendCup(keyPadHandler.getCupTypeSelected());
		}
	}

}
