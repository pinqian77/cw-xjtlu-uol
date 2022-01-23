package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IHoppers;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class HopperManager {

	// How many hoppers can we handle
	private static final int MAX_HOPPERS = 4;  // hoppers for drinks
	// How many milliseconds for each hoppers
	private long hopperTimes[] = new long[MAX_HOPPERS];
	
	private static final int IDLE=0;
	private static final int VENDING=1;
	private int state=IDLE;
	

	// How many milliseconds per gram of hopper material
	private double timePerGram[]={1172,1070.69,1571,117};
	private long calibrationTime = 0;

	private boolean calibrating = false;

	public boolean isVending() {
		return(state==VENDING);
	}
	
	public void recalibrate(Recipe recipe,Cup cup) {
		for (int idx=0;idx<recipe.getIngredientAmounts().length;idx++) {
			IngredientAmount amount=recipe.getIngredientAmounts()[idx];
			if (amount==null) continue;
			double newTime=timePerGram[amount.getIngredient()];
			int product=amount.getIngredient();
			switch (product) {
			case Hoppers.COFFEE :
				newTime=newTime*amount.getAmountInGrams()/cup.getCoffeeGrams();
				break;
			case Hoppers.SUGAR :
				newTime=newTime*amount.getAmountInGrams()/cup.getSugarGrams();
				break;
			case Hoppers.MILK :
				newTime=newTime*amount.getAmountInGrams()/cup.getMilkGrams();
				break;
			case Hoppers.CHOCOLATE :
				newTime=newTime*amount.getAmountInGrams()/cup.getChocolateGrams();
				break;
			
			
			}
			newTime=newTime*recipe.getCupCapacity()/Cup.SMALL;
			System.out.println("prod is "+product+" time is "+newTime);
		}
	}
	
	private void calibrate(IMachine machine) {
		if ((System.currentTimeMillis() - calibrationTime) > 10000) {
				long time = System.currentTimeMillis() - calibrationTime;
				timePerGram[Hoppers.COFFEE] = time / machine.getCup().getCoffeeGrams();
				timePerGram[Hoppers.CHOCOLATE] = time / machine.getCup().getChocolateGrams();
				timePerGram[Hoppers.MILK] = time / machine.getCup().getMilkGrams();
				timePerGram[Hoppers.SUGAR] = time / machine.getCup().getSugarGrams();
				machine.getHoppers().setHopperOff(Hoppers.CHOCOLATE);
				machine.getHoppers().setHopperOff(Hoppers.MILK);
				machine.getHoppers().setHopperOff(Hoppers.SUGAR);
				machine.getHoppers().setHopperOff(Hoppers.COFFEE);
				calibrating = false;
				for (int index=0;index<timePerGram.length;index++) {
					System.out.println("time per gr is "+this.timePerGram[index]);
				}
			}
	}

	
	
	public void controlHoppers(IMachine machine) {
		if (calibrating) {
			calibrate(machine);
		} else {
			switch (state) {
			   case VENDING :
				   checkHopperTimes(machine.getHoppers());
				   break;
			   case IDLE :
				   break;
			}
		}
	}
	
	private void checkHopperTimes(IHoppers hoppers) {
		long now=System.currentTimeMillis();
		boolean stillVending=false;				// if true then we are still making the drink
		for (int index=0;index<hopperTimes.length;index++) {
			if (hopperTimes[index]==0) continue;  // hopper not involved
			if (now<hopperTimes[index]) {
				stillVending=true;				  // this hopper remains on
				continue;
			}
			hoppers.setHopperOff(index);  // switch it off time up
		}
		if (!stillVending) {
			state=IDLE;
		}
		
	}
	
	public boolean canVend(IMachine machine) {
		// true if any drink can be vended... checks to see if large drinks are possible
		Recipe recipeList[]=Recipe.getAllrecipes();
		for (int index=0;index<recipeList.length;index++) {
			Recipe recipe = recipeList[index];
			if (recipe.available(machine,Cup.SMALL_CUP)) {
				return(true);
			}
		}
		return(false);
	}

	public void vendDrinks(Recipe recipe, IMachine machine) {
		// first need to calculate the stop times..
		// get scaling factor depending on cup size..
		double scale=recipe.getCupCapacity()/Cup.SMALL;
		System.out.println("Scale is "+scale);
		long now=System.currentTimeMillis();
		// first we set all hopper times to zero, this indicates these hoppers are not active
		for (int index=0;index<hopperTimes.length;index++) {
			hopperTimes[index]=0;
		}
		// Now we go through the recipe looking for ingredients
		for (int index=0;index<recipe.getIngredientAmounts().length;index++) {
			IngredientAmount amount=recipe.getIngredientAmounts()[index];
			if (amount==null) continue;		// not all recipes have all ingredients set
			double amountInGrams=amount.getAmountInGrams()*scale;
			System.out.println("a in grams is "+amountInGrams+" ing is "+amount.getIngredient());
			double delay=(long)(amountInGrams*this.timePerGram[amount.getIngredient()]);
			hopperTimes[amount.getIngredient()]=(long)(delay+now);
			System.out.println("Time is "+delay);
			machine.getHoppers().setHopperOn(amount.getIngredient());
			state=VENDING;
		}
	}

	/**
	 * @return the calibrating
	 */
	public final boolean isCalibrating() {
		return calibrating;
	}

	/**
	 * @param calibrating
	 *            the calibrating to set
	 */
	public final void setCalibrating(boolean calibrating,IMachine machine) {
		if (calibrating) {
		   IHoppers hoppers=machine.getHoppers();
		   calibrationTime = System.currentTimeMillis();
		   machine.vendCup(Cup.LARGE_CUP);
		   hoppers.setHopperOn(Hoppers.CHOCOLATE);
		   hoppers.setHopperOn(Hoppers.MILK);
		   hoppers.setHopperOn(Hoppers.SUGAR);
		   hoppers.setHopperOn(Hoppers.COFFEE);
		}
		this.calibrating = calibrating;
	}

}
