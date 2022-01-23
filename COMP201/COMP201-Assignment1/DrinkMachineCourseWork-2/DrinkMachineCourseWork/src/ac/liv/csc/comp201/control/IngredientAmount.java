package ac.liv.csc.comp201.control;

public class IngredientAmount {
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
