package ac.liv.csc.comp201.test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Permission;
import java.lang.reflect.*;

import ac.liv.csc.comp201.MachineController;
import ac.liv.csc.comp201.Main;
import ac.liv.csc.comp201.model.ICoinHandler;
import ac.liv.csc.comp201.model.IHoppers;
import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;
import ac.liv.csc.comp201.simulate.MachineSimulator;


class MySecurityManager extends SecurityManager {
	  private boolean skip=false;
	  public void setSkip() {
		  skip=true;
	  }
	  public void clearSkip() {
		  skip=false;
	  }
	  @Override public void checkExit(int status) {
		if (skip) return;  
	    throw new SecurityException();
	  }

	  @Override public void checkPermission(Permission perm) {
	      // Allow other activities by default
	  }
}






class IngredientAmount {
	public IngredientAmount(int ingredient,float amountInGrams) {
		this.amountInGrams = amountInGrams;
		this.ingredient = ingredient;
	}
	private float amountInGrams;
	private int ingredient;		// index into hopper, Coffee, Sugar etc  see Hoppers.java
	/**
	 * @return the amountInGrams
	 */
	public final float getAmountInGrams() {
		return amountInGrams;
	}
	/**
	 * @return the ingredient
	 */
	public final int getIngredient() {
		return ingredient;
	}
}

/* Internal Recipe class for testing */

class Recipe implements Cloneable {
	private static final int MAX_INGREDIENTS=5;
	private IngredientAmount ingredientAmounts[]=new IngredientAmount[MAX_INGREDIENTS];
	private float makeTemperature;
	private String drinkCode;
	private int priceOfDrink;
	private int cupSize;
	private double cupCapacity;
	
	
	public Recipe(IngredientAmount ingredient1,IngredientAmount ingredient2,IngredientAmount ingredient3,float makeTemp,String code,int price) {
		ingredientAmounts[0]=ingredient1;
		ingredientAmounts[1]=ingredient2;
		ingredientAmounts[2]=ingredient3;
		makeTemperature=makeTemp;
		drinkCode=code;
		priceOfDrink=price;
	}
	
	public Recipe clone() throws CloneNotSupportedException {
		Recipe cloneObject=(Recipe)super.clone();
		return(cloneObject);
	}
	
	public final String getDrinkCode() {
		return(drinkCode);
	}
	
	public final double getScale() {
		double scale=1f;
		switch (cupSize) {
		case Cup.MEDIUM_CUP :
			scale=Cup.MEDIUM/Cup.SMALL;
			break;
		case Cup.LARGE_CUP :
			scale=Cup.LARGE/Cup.SMALL;
		}
		return(scale);
	}
	
	private double cupSizeInLitres(int cupSizeType) {
		double cupSize=Cup.SMALL;
		switch (cupSizeType) {
			case Cup.SMALL_CUP :
				cupSize=Cup.SMALL;
				break;
			case Cup.MEDIUM_CUP :
				cupSize=Cup.MEDIUM;
				break;
			case Cup.LARGE_CUP :
				cupSize=Cup.LARGE;
				break;
		}
		return(cupSize);
	}
	
	private final static Recipe allRecipes[] ={
		new Recipe(new IngredientAmount(Hoppers.COFFEE,2f),null,null,95.9f,"101",120),
		new Recipe(new IngredientAmount(Hoppers.COFFEE,2f),new IngredientAmount(Hoppers.SUGAR,5f),null,95.9f,"102",130),
		new Recipe(new IngredientAmount(Hoppers.COFFEE,2f),new IngredientAmount(Hoppers.MILK,3f),null,95.9f,"201",120),
		new Recipe(new IngredientAmount(Hoppers.COFFEE,2f),new IngredientAmount(Hoppers.MILK,3f),new IngredientAmount(Hoppers.SUGAR,5f),95.9f,"202",130),
		new Recipe(new IngredientAmount(Hoppers.CHOCOLATE,28f),null,null,90,"300",110)
	};
	
	// Careful the following definitions are coupled to list above
	public static final Recipe BLACK_COFFEE=allRecipes[0];
	public static final Recipe WHITE_COFFEE=allRecipes[2];
	public static final Recipe CHOCOLATE=allRecipes[4];
	// Invalid receipe code 103
	public static final Recipe INVALID_RECIPE=new Recipe(null,null,null,0,"103",0);
	
	
	public static Recipe getRecipeFromCode(String code) {
		for (int index=0;index<allRecipes.length;index++) {
			if (allRecipes[index].drinkCode.equals(code)) {
				return(allRecipes[index]);
			}
		}
		return(null);		// code not valid
	}


	
	
	
	
	/**
	 * @return true if this Recipe can be vended by the machine
	 */
	public boolean available(IMachine machine) {
		double scale=1f;
		switch (cupSize) {
		case Cup.MEDIUM_CUP :
			scale=Cup.MEDIUM/Cup.SMALL;
			break;
		case Cup.LARGE_CUP :
			scale=Cup.LARGE/Cup.SMALL;
		}
		for (int index=0;index<this.ingredientAmounts.length;index++) {
			IngredientAmount amount=ingredientAmounts[index];
			if (amount==null) continue;
			if (machine.getHoppers().getHopperLevelsGrams(amount.getIngredient())<amount.getAmountInGrams()*scale) {
				return(false);	// no good hopper not got enough
			}
		}
		return(true);
	}


	/**
	 * @return true if this Recipe can be vended by the machine, for a given cup size
	 */
	public boolean available(IMachine machine,int cupSize) {
		double scale=1f;
		switch (cupSize) {
		case Cup.MEDIUM_CUP :
			scale=Cup.MEDIUM/Cup.SMALL;
			break;
		case Cup.LARGE_CUP :
			scale=Cup.LARGE/Cup.SMALL;
		}
		for (int index=0;index<this.ingredientAmounts.length;index++) {
			IngredientAmount amount=ingredientAmounts[index];
			if (amount==null) continue;
			if (machine.getHoppers().getHopperLevelsGrams(amount.getIngredient())<amount.getAmountInGrams()*scale) {
				return(false);	// no good hopper not got enough
			}
		}
		return(true);
	}

	
	/**
	 * @return the ingredientAmounts
	 */
	public final IngredientAmount[] getIngredientAmounts() {
		return ingredientAmounts;
	}

	/**
	 * @param ingredientAmounts the ingredientAmounts to set
	 */
	public final void setIngredientAmounts(IngredientAmount[] ingredientAmounts) {
		this.ingredientAmounts = ingredientAmounts;
	}

	/**
	 * @return the makeTemperature
	 */
	public final float getMakeTemperature() {
		return makeTemperature;
	}
	
	public void controlHoppers() {
		// we allocate time per hopper based on amount
		
	}

	/**
	 * @return the priceOfDrink
	 */
	public final int getPriceOfDrink() {
		int price=priceOfDrink;
		switch (cupSize) {
		case Cup.MEDIUM_CUP :
			price+=20;
			break;
		case Cup.LARGE_CUP :
			price+=25;
			break;
		}
		return price;
	}



	/**
	 * @return the cupSize
	 */
	public final int getCupSize() {
		return cupSize;
	}



	/**
	 * @param cupSize the cupSize to set
	 */
	public final void setCupSize(int cupSize) {
		this.cupSize = cupSize;
		cupCapacity=this.cupSizeInLitres(cupSize);
	}

	/**
	 * @return the cupCapacity
	 */
	public final double getCupCapacity() {
		return cupCapacity;
	}

	/**
	 * @return the allrecipes
	 */
	public static final Recipe[] getAllrecipes() {
		return allRecipes;
	}

}


public class Tester extends Thread {
	
	private IMachine machine;
	
	private static boolean doneOverHeat=false;
	
	private FileWriter writer;
	
	
	private double testScore=0;
	
	private double waterScore=0;
	
	private double totalWaterScore=0;
	
	private int totalScore=0;
	
	private double coinScoreTotal=0;
	
	private Object callbackFlag=new Object();
	
	private static final int START_TIME=30;
	
	private void setIngredientsHigh() {
		IHoppers hoppers=machine.getHoppers();
		for (int idx=Hoppers.COFFEE;idx<=Hoppers.CHOCOLATE;idx++) {
			hoppers.setHopperTo(idx,1000);
		}
	}
	
	
	
	
	private void startUp() {
		// Give 10 seconds to start up
		try {
		Thread.sleep(START_TIME*1000);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void callback() {
		System.out.println("Calling back...");
		synchronized(callbackFlag) {
			callbackFlag.notify();
		}
		
	}
	
	private void delay(long delayTime) {
		try {
			Thread.sleep(delayTime);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
	}
	
	
	
	private void addCoin(String coinName,ICoinHandler coinHandler) {
		for (int idx=0;idx<coinNames.length;idx++) {
			if (coinNames[idx].equals(coinName)) {
				synchronized (coinHandler) {
					coinHandler.insertCoin(coinCodes[idx]);
				}
				break;
			}
		}
	}
	
    private static final String coinCodes[]={"ab","ac","ba","bc","bd","ef","zz" };	
	private static final String coinNames[]={"1p","5p","10p","20p","50p","100p" };

	private static final int RETURN_CHANGE = 9;

	private static final double CUP_TEMPERATURE = 80;
	
	private String testCoins[]={"100p","50p","20p","10p","5p","1p" };
	private int testBalances[]={100,50,20,10,5,1 };

	private String getAllLevelts() {
		String levels="";
		for (int idx=0;idx<coinNames.length;idx++) {
			levels=levels+coinNames[idx]+"("+getCoinLevel(coinNames[idx])+") ";
		}
		return(levels);
	}
	
	private int getCoinLevel(String name) {
		int level=0;
		for (int idx=0;idx<coinNames.length;idx++) {
			if (coinNames[idx].equals(name)) {
				level=machine.getCoinHandler().getCoinHopperLevel(coinCodes[idx]);
				break;
			}
		}		
		return(level);
	}
	
	
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
	private boolean doTests(int key) {
		testPressed++;
		if (key!=7) return(false);
		if (testPressed>4) {
			machine.getWaterHeater().forceError();
			machine.getDisplay().setTextString("Water heater error");
			try {
				Thread.sleep(2000);
			} catch (Exception exc) {
			}
				
			return(true);
		}
			toggleCoinHoppers();
		
		return(false);
	}
	
	boolean altCup=false;

	private boolean fourPrefix=false;
	
	public void altCupLogic() {
	   altCup=true;	
	}
	
	public void setFourPrefix() {
		fourPrefix=true;
	}
	
	
	private void enterDrinkCode(Recipe recipe,int cupSize) {
		System.out.println("Entering drink cup size is "+cupSize);
        String codeToAdd="";
        if ( (cupSize==Cup.SMALL_CUP) && ( (altCup) || (fourPrefix)) ) {
			codeToAdd="4";
		}
		if (cupSize==Cup.MEDIUM_CUP) {
			codeToAdd="5";
		}
		if (cupSize==Cup.LARGE_CUP) {
			codeToAdd="6";
		} 
		if (!altCup) {			
		   codeToAdd=codeToAdd+recipe.getDrinkCode();
		} else {
			codeToAdd=recipe.getDrinkCode()+codeToAdd;
		}
		System.out.println("Cup code is "+codeToAdd);
		this.logTest("Trying to make drink with code "+codeToAdd);
		machine.getKeyPad().addKeyCodes(codeToAdd);
		if (confirm.length()>0) {
			machine.getKeyPad().addKeyCodes(confirm);
				
		}

	}
	
	public final void setConfirm(String confirm) {
		this.confirm=confirm;
	}
	
	private String confirm="";
	
	
	/**
	 * Returns true if ingredient has filled up Cup too much 
	 * @param recipe
	 * @param cupSize
	 */
	private boolean overFlowed(Recipe recipe,int cupSize) {
		recipe.setCupSize(cupSize);
		boolean ok=true;
		for (int idx=0;idx<recipe.getIngredientAmounts().length;idx++) {
			IngredientAmount ingAmount=recipe.getIngredientAmounts()[idx];
			if (ingAmount!=null) { // only check non null ingredients
				double target=ingAmount.getAmountInGrams()*recipe.getScale();
				double cupAmount=0;
				cupAmount = getCupAmount(ingAmount.getIngredient());
				//System.out.println("Checking ing "+this.getIngName(ingAmount.getIngredient()));
				//System.out.println("Cup amount is "+cupAmount+" limit is "+target);
				
				if (cupAmount>target*2) {
					return(true);
				}				
			}
		}
		// Now see if we have Cup ingredients in Cup but not in recipe
		ok=true;
		int stray = findStray(recipe);
		ok=(stray!=-1);	// no stray ingredients
		return(ok);
	}




	private int findStray(Recipe recipe) {
		int stray=-1;
		for (int idx=Hoppers.COFFEE;idx<=Hoppers.CHOCOLATE;idx++) {
			double amount=getCupAmount(idx);
			if (amount>0) {
				stray=idx;
				
				for (int recipeIngredient=0;recipeIngredient<recipe.getIngredientAmounts().length;recipeIngredient++) {
					if (recipe.getIngredientAmounts()[recipeIngredient]==null) continue;
					if (recipe.getIngredientAmounts()[recipeIngredient].getIngredient()==idx) {
						stray=-1;
						break;		// found it
					}
				}
			}
		}
		return stray;
	}


	private String getIngName(int ingredient) {
		switch (ingredient) {
			case Hoppers.COFFEE :
				return("coffee");
			case Hoppers.SUGAR :
				return("sugar");
			case Hoppers.CHOCOLATE :
				return("chocolate");
			case Hoppers.MILK :
				return("milk");
		}		
		return("");
	}
	private Cup currentCup=null;
	
	private double getCupAmount(int ingredient) {
		double cupAmount=0;
		if (currentCup==null) {
			return(0);
		}
		switch (ingredient) {
			case Hoppers.COFFEE :
				cupAmount=currentCup.getCoffeeGrams();
				break;
			case Hoppers.SUGAR :
				cupAmount=currentCup.getSugarGrams();
				break;
			case Hoppers.CHOCOLATE :
				cupAmount=currentCup.getChocolateGrams();
				break;
			case Hoppers.MILK :
				cupAmount=currentCup.getMilkGrams();
				break;
		}		
		return(cupAmount);
	}
	
	private void checkIngredients(Recipe recipe,int cupSize) {
		recipe.setCupSize(cupSize);
		this.totalScore+=4;
	    if (currentCup==null) {
			logError("Test fail no cup vended for drink");
			return;
		}
		boolean ok=true;
	    int stray=findStray(recipe);
	    if (stray!=-1) {
	    	logError("Ingredient in cup but not in recipe  ingredient is "+getIngName(stray));
	    	ok=false;
	    }
		for (int idx=0;idx<recipe.getIngredientAmounts().length;idx++) {
			IngredientAmount ingAmount=recipe.getIngredientAmounts()[idx];
			if (ingAmount!=null) { // only check non null ingredients
				double target=ingAmount.getAmountInGrams()*recipe.getScale();
				double cupAmount=0;
				String ingredientName="";
				switch (ingAmount.getIngredient()) {
					case Hoppers.COFFEE :
						ingredientName="coffee";
						cupAmount=currentCup.getCoffeeGrams();
						break;
					case Hoppers.SUGAR :
						ingredientName="sugar";
						cupAmount=currentCup.getSugarGrams();
						break;
					case Hoppers.CHOCOLATE :
						ingredientName="chocolate";
						cupAmount=currentCup.getChocolateGrams();
						break;
					case Hoppers.MILK :
						ingredientName="milk";
						cupAmount=currentCup.getMilkGrams();
						System.out.println("Milk in grams target is "+target);
						break;
				}
				double lowerLimit=target*0.88;	// keep within +-12%
				double upperLimit=target*1.12;	// keep within +-12%
				System.out.println("Lower limit is "+lowerLimit);
				System.out.println("Upper limit is "+upperLimit);
				
				if (cupAmount>upperLimit) {
					logError("Too much "+ingredientName+" more than "+upperLimit+"g level was "+cupAmount+" g");
					ok=false;
				} else {
					if (cupAmount<lowerLimit) {
						logError("Too little "+ingredientName+" less than "+lowerLimit+"g level was "+cupAmount+" g");
						ok=false;
					} else {
						logTest("Ingredient "+ingredientName+" within tolerance");
					}							
				}
				
			}
		}
		if (ok) {
			testScore+=4;
		}
	}
	
	
	
	private double oldWaterLevel;
	private double oldIngredientLevels[]=new double[Hoppers.MAX_HOPPERS];
	Cup lastCup=new Cup(Cup.SMALL_CUP);
	
	private void saveState() {
		int count=0;
		while (machine.getCup()==lastCup) {
			delay(1000);
			count++;
			if (count==20) break;
		}		
		currentCup=machine.getCup();
		if (currentCup==null) return;  // failed to get new cup in 20 seconds
		lastCup=currentCup;
		oldWaterLevel=currentCup.getWaterLevelLitres();
		oldIngredientLevels[Hoppers.COFFEE]=currentCup.getCoffeeGrams();
		oldIngredientLevels[Hoppers.CHOCOLATE]=currentCup.getChocolateGrams();
		oldIngredientLevels[Hoppers.SUGAR]=currentCup.getSugarGrams();
		oldIngredientLevels[Hoppers.MILK]=currentCup.getMilkGrams();				
	}
	
	private boolean isStateChange() {
		if (currentCup==null) return(false);
		if (currentCup.getWaterLevelLitres()==0) {
			return(true); // don't allow empty cup if poss
		}
		if (currentCup.getWaterLevelLitres()!=oldWaterLevel) {
			System.err.println("Water level change.."+currentCup.getWaterLevelLitres());
			oldWaterLevel=currentCup.getWaterLevelLitres();
			return(true);
		}
		if (currentCup.getCoffeeGrams()!=this.oldIngredientLevels[Hoppers.COFFEE]) {
			System.err.println("coff level change.."+currentCup.getCoffeeGrams());
			oldIngredientLevels[Hoppers.COFFEE]=currentCup.getCoffeeGrams();
			return(true);
		}
		if (currentCup.getChocolateGrams()!=this.oldIngredientLevels[Hoppers.CHOCOLATE]) {
			System.err.println("choc level change.."+currentCup.getChocolateGrams());
			
			oldIngredientLevels[Hoppers.CHOCOLATE]=currentCup.getChocolateGrams();
			return(true);
		}
		if (currentCup.getSugarGrams()!=this.oldIngredientLevels[Hoppers.SUGAR]) {
			System.err.println("sug level change.."+currentCup.getSugarGrams());
			
			oldIngredientLevels[Hoppers.SUGAR]=currentCup.getSugarGrams();
			return(true);
		}
		if (currentCup.getMilkGrams()!=this.oldIngredientLevels[Hoppers.MILK]) {
			System.err.println("milk level change.."+currentCup.getMilkGrams());			
			oldIngredientLevels[Hoppers.MILK]=currentCup.getMilkGrams();
			return(true);
		}		
		return(false);
	}
	
	private int oldBalance=0;
	
	private void testDrinkMaking(Recipe recipe,int cupSize) {
		oldBalance=machine.getBalance();
		System.out.println("Balance for making drink is "+machine.getBalance());
		recipe.setCupSize(cupSize);
		lastCup=machine.getCup();
		enterDrinkCode(recipe, cupSize);
		long oldTime=System.currentTimeMillis();
		// Now wait till new Cup is vended
		delay(4000);
		saveState();		// new cup so save cup state this will be the new Vended up..
		delay(1000);		// another 4 seconds to allow water to heat
		int loopCounter=0;	// to make sure loop below terminates
		while (isStateChange()) {	// wait till Cup has finished changing state
			delay(3000);
			/*if (overFlowed(recipe, cupSize)) {
				break;
			}*/
			if (loopCounter++>20) {
				break;		// ensure termination of drink marking logic
			}
		}
		double makeTime=System.currentTimeMillis()-oldTime;
		this.totalScore+=2;
		   if (currentCup!=null) {
		     if (makeTime>30000) {
			      logError("Time to make drink very slow, performance failure > 30s time to make is "+makeTime/1000+" seconds");
		     } else {
			    this.testScore+=2;
			    this.logTest("Drink made within 16 seconds");
		     }
		}
		// Now test the drink
		checkIngredients(recipe,cupSize);
		checkTemperatures(recipe);
		logTest("New balance is "+machine.getBalance());
		checkPrice(recipe,cupSize);
	}
	
	private void checkPrice(Recipe recipe, int cupSize) {
		recipe.setCupSize(cupSize);
		totalScore+=2;
		if (currentCup==null) {
			return; // didn't make drink
		}
		int price=oldBalance-machine.getBalance();
		if (price!=recipe.getPriceOfDrink()) {
			logError("Price charged for drink incorrect price charged is "+price+"p  price should be "+recipe.getPriceOfDrink()+"p");
		} else {
			testScore+=2;
			logTest("Price charged for drink correct");
		}		
	}




	private void checkTemperatures(Recipe recipe) {
		// first check make temperature
		if (currentCup==null) {
			totalWaterScore+=2;
			totalWaterScore+=2;
			logError("Test fail no cup vended for drink");
			return;
		}
		
		double lowMakeTemperature=recipe.getMakeTemperature()*.95;
		double highMakeTemperature=recipe.getMakeTemperature()*1.05;
		Cup cup=currentCup;
		totalWaterScore+=2;
		if (cup.getMakeTemperature()<lowMakeTemperature) {
			logError("Drink make temperature too low should be at least "+lowMakeTemperature+" temp was "+cup.getMakeTemperature());
		} else {
			if (cup.getMakeTemperature()>highMakeTemperature) {
				logError("Drink make temperature too high should be at maximum "+highMakeTemperature+" temp was "+cup.getMakeTemperature());
			} else {
				logTest("Drink make temperature within tolerance");
				waterScore+=2;
			}
		}
		double lowCupTemperature=CUP_TEMPERATURE*.95;
		double highCupTemperature=CUP_TEMPERATURE*1.05;
		totalWaterScore+=2;
		logTest("Drink liquid level is "+cup.getWaterLevelLitres());
		if (cup.getTemperatureInC()<lowCupTemperature) {
			logError("Drink cup temperature of final drink too low should be at least "+lowCupTemperature+" temp is "+cup.getTemperatureInC()+"");
		} else {
			if (cup.getTemperatureInC()>highCupTemperature) {
				logError("Drink cup temperature of final drink too high should be at maximum "+highCupTemperature+" temp is "+cup.getTemperatureInC());
			} else {
				logTest("Drink cup temperature within tolerance");
				waterScore+=2;
			}
		}								
	}




	private void testMakeDrinkLogic() {
		totalScore=0;
		testScore=0;
		setIngredientsHigh();		        
		setCoinHoppersHigh();
		setCoinAmount(3000);	// 30 pounds plenty of balance.. belts and braces in case coin entry fails
		machine.setBalance(3000); // belts and braces to set up balance totally correct
		// First try black coffee no sugar, normal size
		Recipe recipe=Recipe.BLACK_COFFEE;
		logTest("Making large black coffee no sugar and waiting 10 seconds");
		testDrinkMaking(recipe,Cup.LARGE_CUP);
		logTest("Making small black coffee no sugar and waiting 10 seconds");
		testDrinkMaking(recipe,Cup.SMALL_CUP);		
		recipe=Recipe.WHITE_COFFEE;
		logTest("Making large white coffee no sugar and waiting 10 seconds");		
		testDrinkMaking(recipe,Cup.MEDIUM_CUP);
		recipe=Recipe.CHOCOLATE;
		logTest("Making small chocolate and waiting 10 seconds");		
		testDrinkMaking(recipe,Cup.SMALL_CUP);				
		// Now check drink logic with low balance
		logTest("Trying to make drink with low balance");
		// fetch the old Cup
		Cup oldCup=machine.getCup();
		recipe.setCupSize(Cup.SMALL_CUP);
		setCoinAmount(recipe.getPriceOfDrink()-1);	// not enough balance
		int oldBalance=machine.getBalance();
		enterDrinkCode(recipe,Cup.SMALL_CUP);
		// Now wait ten seconds to make drink (this will fail)
		delay(5000);		// short delay
		boolean ok=true;
		totalScore+=4;
		logTest("Testing low balance");				
		if (oldBalance!=machine.getBalance()) {
			logError("Balance changed when too low for drink, incorrect");
			ok=false;
		}
		if (machine.getCup()!=oldCup) {
			logError("Error new cup vended but not enough balance");
			ok=false;
		}		
		if (ok) {
			logTest("Drink vend low balance test passed");
			testScore+=4;
		}
		logTest("Testing low coffee, drink unavailable");				
		// Now test for drink unavailable
		IHoppers hoppers=machine.getHoppers();
		// set coffee to low level, but near the boundary
		hoppers.setHopperTo(Hoppers.COFFEE,1.99);
		// Now try and make a coffee
		setCoinAmount(recipe.getPriceOfDrink()-1);	// enough cash balance
		oldBalance=machine.getBalance();
		oldCup=machine.getCup();
		recipe=Recipe.BLACK_COFFEE;
		enterDrinkCode(recipe,Cup.SMALL_CUP);
		// Now wait ten seconds to make drink (this will fail)
		delay(1000);		// short delay
		ok=true;
		totalScore+=4;
		if (oldBalance!=machine.getBalance()) {
			logError("Balance changed when coffee too low for drink, incorrect");
			ok=false;
		}
		if (machine.getCup()!=oldCup) {
			logError("Error new cup vended but not enough coffee to make drink");
			ok=false;
		}		
		if (ok) {
			logTest("Drink vend low coffee test passed");
			testScore+=4;
		}
		logTest("Testing invalid drink code");				
		recipe=Recipe.INVALID_RECIPE;
		oldCup=currentCup; // save old cup		
		setCoinAmount(300);	// enough cash balance
		oldBalance=machine.getBalance();
		enterDrinkCode(recipe,Cup.SMALL_CUP);
		// Now wait ten seconds to make drink (this will fail)
		delay(1000);	// short delay
		ok=true;
		totalScore+=4;
		if (oldBalance!=machine.getBalance()) {
			logError("Balance changed when drink code invalid, incorrect");
			ok=false;
		}
		if (machine.getCup()!=oldCup) {
			logError("Error new cup vended but drink code invalid");
			ok=false;
		}		
		if (ok) {
			logTest("Invalid drink code test passed");
			testScore+=4;
		}		
	}
	
	private void testWaterHeaterLogic() {
		// Belts and braces for code which isn't complying
				// with set and get balance
		setCoinHoppersHigh();
		addCoin("100p",machine.getCoinHandler());
		addCoin("100p",machine.getCoinHandler());
		delay(1000); // allow coins to add in..
		//totalWaterScore+=4;
		//logTest("Checking for water heater idle temperature, must not be greater than 75C");
	    if (machine==null) {
	    	System.out.println("Machine is null");
	    }
	    if (machine.getWaterHeater()==null) {
	    	System.out.println("Machine water heater  is null");
	    }
	    
		/*if (machine.getWaterHeater().getTemperatureDegreesC()>75) {
			logError("Water temperature too hot in idle mode, wasting energy, doesn't need to be this hot to start making drink in 4 seconds temp is "+machine.getWaterHeater().getTemperatureDegreesC());
		} else {
			waterScore+=4;
            logTest("Idle temperature ok score is "+testScore);			
		}*/
		
		IHoppers hoppers=machine.getHoppers();
		// Now try and make drink 101
		hoppers.setHopperTo(Hoppers.COFFEE,20); // enough for black coffee
				
		machine.getKeyPad().addKeyCodes("101");
		if (confirm.length()>0) {
			machine.getKeyPad().addKeyCodes(confirm);
				
		}
		
		long now=System.currentTimeMillis();		
		totalWaterScore+=4;
			int count=0;
			while (machine.getWaterHeater().getTemperatureDegreesC()<95.9) {
				delay(50);
				count++;
				if (count>50) {
					break; // after ten seconds then failed
				}
			}
			double delay=System.currentTimeMillis()-now;
			System.out.println("Got to temperature in "+delay);
			if (delay>4500) {
				logError("Time to read correct temperature too slow time was "+delay/1000+" seconds");;
			} else {
				logTest("Time to heat to correct temperature ok");
				waterScore+=4;
			}
		// Now check the temperature
		// shutdown...
		
		
		
		
	}
	
	private void setCoinAmount(int amount) {
		int value=amount;
		setCoinHoppersHigh();		// make sure we can pay out
		machine.getKeyPad().addKeyCodes(""+RETURN_CHANGE);		
		System.out.println("added in key 9 ... waiting for 3");
		long oldTime=System.currentTimeMillis();
		delay(3000); // wait three seconds for balance to clear
		System.out.println("Finished wait... time waited is "+(System.currentTimeMillis()-oldTime));
		while (amount>0) {
			if (amount>=100) {
				System.out.println("Added in 100p ");
				addCoin("100p",machine.getCoinHandler());
				amount-=100;
			} else {
				if (amount>=10) {
				addCoin("10p",machine.getCoinHandler());
				amount-=10;
				} else {
					addCoin("1p",machine.getCoinHandler());
					amount-=1;						
				}
			}
		}
		delay(3000); // wait three seconds for balance to set up correctly
		machine.setBalance(value); // belts and braces!!!
	}
	
	private void testCoinLogic() {
		setIngredientsHigh();
		
		
		machine.getCoinHandler().unlockCoinHandler();
		totalScore=0;
		testScore=0;
		int sum=0;
		for (int idx=0;idx<testCoins.length;idx++) {
			int oldBalance=machine.getBalance();
			logTest("Old balance before coin added is "+oldBalance);
			addCoin(testCoins[idx],machine.getCoinHandler());
			totalScore+=2;
			delay(1000);
			logTest("New balance after coin added is "+machine.getBalance());
			if ((machine.getBalance()-oldBalance)==testBalances[idx]) {
				testScore+=2;
				logTest("Test passed for "+testCoins[idx]+" insertion score is "+testScore);
			} else {
				logError("Balance incorrect after "+testCoins[idx]+" expecting increase of "+testBalances[idx]+" got increaxse of "+(machine.getBalance()-oldBalance));
				logError("Do you call machine.setBalance() when new coins are entered");
			}
				
		}
		// first reset the balance
		setCoinHoppersHigh();		
		setCoinAmount(210);	// add in 2 pounds and 10p				
		System.out.println("Balance is "+machine.getBalance());		
		// Now do some change payout
		setCoinHoppersLow();		
		// Now try and pay this out
		machine.getKeyPad().addKeyCodes(""+RETURN_CHANGE);
		delay(1000);
		// will pay out 186p
		// leaving residue of 24p
		totalScore+=4;
		logTest("Testing balance returned with all hoppers low (1 coin each) and balance of 210p");
		int balance=machine.getBalance();
		if (balance==24) {
			testScore+=4;
			logTest("Test passed for return change score is "+testScore);			
		} else {
			logError("Test failed expected 24p balance got "+balance+" after return change from 210p and all hoppers low");
			logError("Do you call machine.setBalance() when returning change");

		}
		System.out.println("Balance after change is "+machine.getBalance());
		totalScore+=4;
		logTest("Setting all coin hoppers high 100 coins, setting balance to 210 then paying change");
		setCoinAmount(210); // £2.10
		setCoinHoppersHigh();
		// Now try and pay this out
		machine.getKeyPad().addKeyCodes(""+RETURN_CHANGE);
		delay(1000);
		// now check expected levels
		System.out.println("100p level is "+getCoinLevel("100p"));
		if  ( (getCoinLevel("100p")!=98) || (this.getCoinLevel("10p")!=99)) {
			logError("Test failed expected 98 x 100p and 99 x 10p left over levels are "+getAllLevelts());
		} else {
			testScore+=4;
			logTest("Test passed, score is "+testScore);
		}		
		// Now clear out 100p and 50p
		setCoinAmount(210);	// add in 2 pounds and 10p		
		setCoinHoppersHigh();		
		machine.getCoinHandler().setHopperLevel("ef",0);
		machine.getCoinHandler().setHopperLevel("bd",0);
		// Now try and pay this out
		totalScore+=4;
		logTest("Setting all coins high apart from 100p=0 and 50p=0, setting balance to 210 then paying change");		
		machine.getKeyPad().addKeyCodes(""+RETURN_CHANGE);
		delay(1000);
		if  ( (getCoinLevel("20p")!=90) || (getCoinLevel("10p")!=99)) {
			logError("Test failed expected 90 x 20p and 99 x 10p left over, actual levels are "+getAllLevelts());			
		} else {
			testScore+=4;
			logTest("Test passed, score is "+testScore);
		}		
		
		IHoppers hoppers=machine.getHoppers();
		logTest("Clearing all hoppers and checking for coin lockout");
		hoppers.emptyAllHoppers();
		delay(1000);
		totalScore+=4;		
		int ob=machine.getBalance();
		addCoin(testCoins[0],machine.getCoinHandler());
		delay(1000);
		
		if ( (machine.getBalance()!=ob) && (machine.getCoinHandler().isCoinHandlerOpen())) {
			logError("Test failed coins should not be accepted");			
		} else {
			testScore+=4;
			logTest("Test passed coins locked out as expected, score is "+testScore);
		}	
		hoppers.setHopperTo(Hoppers.COFFEE,2); // enough for small black coffee
		ob=machine.getBalance();
		logTest("Setting coffee to 2 grams and checking coin unlocks");
		addCoin(testCoins[0],machine.getCoinHandler());
		
		delay(1000);
		totalScore+=4;		
		if (machine.getBalance()==ob) {
			logError("Test failed coins should not be locked out");			
		} else {
			testScore+=4;
			logTest("Test passed coin mechanism unlocked as expected, score is "+testScore);
		}	
		coinScoreTotal=20*(testScore/totalScore);				
	}
	
	private void logTest(String string) {
		System.out.println(string+"\n");
		try {
			this.writer.write(string+" \r\n ");
			//this.writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	private void shutdownEnable(IMachine machine) {
		String methodName="shutdownEnable";
		tryRunMethodByName(machine, methodName);	
	}
	
	



	private void tryRunMethodByName(Object machine, String methodName) {
		Class classt=machine.getClass();
		Method [] mlist=classt.getMethods();
		for (int idx=0;idx<mlist.length;idx++) {
			Method m=mlist[idx];
			if (m.getName().equals(methodName)) {
				try {
					m.invoke(machine);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	private void resetMachine() {		
		SecurityManager oldMan=System.getSecurityManager();
		MySecurityManager secManager = new MySecurityManager();
		System.setSecurityManager(secManager);
		System.out.println("Trying to restart machine...");
		try {
			shutdownEnable(machine);
			Main.closeDown();	
			machine.shutMachineDown();
		    System.out.println("Closed down main control loop");
		    delay(5000); // 5 seconds delay
		} catch (SecurityException e) {
			secManager.setSkip();
			delay(2000);
		    //IMachine machine=new MachineSimulator();
			MachineController controller=Main.getNewController();		// we kick off again and pass this tester back in
 		    machine.start(controller,this); // start machine and pass control to machine controller
		    delay(2000);       // wait for 2 seconds
		    System.setSecurityManager(oldMan);
			System.out.println("Resstarted");
			return;
		};
		secManager.setSkip();
		delay(2000);
	    //IMachine machine=new MachineSimulator();
		MachineController controller=Main.getNewController();		// we kick off again and pass this tester back in
		    machine.start(controller,this); // start machine and pass control to machine controller
	    delay(2000);       // wait for 2 seconds
	    System.setSecurityManager(oldMan);
		System.out.println("Resstarted");
	}
	
	private void clearForceError() {
		tryRunMethodByName(machine.getWaterHeater(),"clearForceError");
	}
	
	private void testHighTemperature() {
		shutdownEnable(machine);		
		System.out.println("Testing high temp");
		SecurityManager oldMan=System.getSecurityManager();
		MySecurityManager secManager = new MySecurityManager();
		System.setSecurityManager(secManager);
		machine.getWaterHeater().forceError();
		totalWaterScore+=4;
		int ob=machine.getBalance();
		addCoin(testCoins[0],machine.getCoinHandler());
		delay(2000);
		// Fairly generous evalaution of machine shut down for this time... take away second part
		// of condition and just have machine.isDead for true test
		if ( (machine.isDead())  || (ob==machine.getBalance())) { // either called shutdown or balance stuck regardless			
			clearForceError();
			System.out.println("Machine is dead");
			logTest("Shutdown high temperature passed");
			waterScore+=4;
		} else {
			clearForceError();
			logTest("Shutdown high temperature failed, did you call machine.shutMachineDown() when water temp>=100");
			System.out.println();
			System.setSecurityManager(oldMan);
			return;
		}
	}


	private void logError(String string) {
		System.err.println("Test fail :"+string);
		try {
			this.writer.write(string+" \r\n");
			//this.writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private double showScore(String message,double score,double total,double weight) {
		 double perc=weight*score/total;
         logTest(message+" "+score+"/"+total);
         System.out.println(message+" "+score+"/"+total+" perc is "+perc+"%");
         return(perc);
	}

	private void doTests() {
				try {
					writer=new FileWriter("testlog.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String dirName=System.getProperty("user.dir");		
				int lastPos=dirName.lastIndexOf("\\");
				dirName=dirName.substring(lastPos+1);
				System.out.println("Working Directory = "+dirName);
				
				// Now allow system to start up from fail ..
				startUp();
				
				double finalScore=0.0;
				
				
				
				setIngredientsHigh();
				
				
				
				setCoinHoppersHigh();
				
				
				machine.getCoinHandler().unlockCoinHandler();
				
				testMakeDrinkLogic();
				
				double makeDrinkScore=showScore("Score for making making drinks is ",testScore,totalScore,40.0);
				
				finalScore+=makeDrinkScore;
				
				setIngredientsHigh();
				
				setCoinHoppersHigh();
				
				testCoinLogic();
				
				double coinScore=showScore("Score for making coin logic is ",testScore,totalScore,20.0);
				
				finalScore+=coinScore;
				
				
				// Use attributes are used to store the test
				// scores for the water temperature control elements
				
				totalWaterScore=0;
				waterScore=0;
				
				// Now check if water is heating fast enough
				testWaterHeaterLogic();
				
				
				// Test > 100c shutdown
				testHighTemperature();
				
				
				double waterFinalScore=showScore("Score for water heater logic is ",waterScore,totalWaterScore,20.0);
				
				finalScore+=waterFinalScore;				
				
				logTest("Summary");
				
				logTest("Score for water heating is "+waterFinalScore+"%");
				
				logTest("Score for coin logic is "+coinScore+"%");
				
				logTest("Score for making drinks is "+makeDrinkScore+"%");				
				
				logTest("Total score for code is "+finalScore+"% out of a total of 80%  (20% for state charts");
				
				// Now try and load score into spreadsheet
				
				
				
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				StudentHelper helper=new StudentHelper();
				long score=Math.round(finalScore);
				System.out.println("Updating score for "+dirName+" to "+score);
				helper.updateMark(dirName,(int)score);
				System.exit(1);
				
	}
	
	public void run() {
		doTests();
	}

	public void startTests(IMachine machine) {
		this.machine=machine;
		if (!isAlive()) {		// don't trigger more than once
			start();
		}
		
		
	}

}
