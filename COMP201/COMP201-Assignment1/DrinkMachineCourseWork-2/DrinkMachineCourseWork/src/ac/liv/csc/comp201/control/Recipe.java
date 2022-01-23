package ac.liv.csc.comp201.control;

import java.io.Serializable;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class Recipe implements Cloneable {
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
	
	public static Recipe getRecipeFromCode(String code) {
		System.err.println("Looking for "+code);
		for (int index=0;index<allRecipes.length;index++) {
			if (allRecipes[index].drinkCode.equals(code)) {
				System.err.println("Found drink idx is "+index);
				return(allRecipes[index]);
			}
		}
		System.err.println("No drink found");
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
