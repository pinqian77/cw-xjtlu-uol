package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.model.IWaterHeater;
import ac.liv.csc.comp201.simulate.Cup;

public class WaterHeaterController {

	private static final int MAX_DELAY_TIME = 4; // how long should we have to
													// wait for water to heat up

	private long oldTime = 0;
	private boolean atTarget=false;
	private boolean hotOn=false;
	private boolean coldOn=false;
	private Recipe recipe=null;
	private double litrePerTimeHot=9.586706433745207E-5;
	private double litrePerTimeCold=9.771986970684039E-5;
	private int readingIndex = 0;
	// The following array is a circular buffer of measurements
	private float tempReadings[] = new float[MAX_DELAY_TIME]; // take recordings
																// for the
																// number of
																// seconds to
																// calibrate for
	private float calibratedIdleTarget = 0;

	// Note all temperatures in degrees C
	private static final float DRINK_TARGET_TEMPERATURE = 80F;
	private static final float COFFEE_MAKE_TEMPERATURE = 95.9f;
	private static final float IDLE_TARGET_TEMPERATURE = 73f; // This value
																	// is
																	// calculated
																	// by
																	// running
																	// the
																	// calibration
																	// code

	private static final int CALIBRATION_STARTING = 1;
	private static final int CALIBRATING = 2;
	private static final int PRE_HEATING = 3;
	private static final int MIXING_DRINK = 4;
	private static final int TOPPING_UP = 5;
	private static final int IDLE = 6;

	//This is the make drink temperature
	private float preHeatTemperature;
	
	private int currentState = IDLE; // default to IDLE state

	private float waterTargetTemperatureC = IDLE_TARGET_TEMPERATURE;

	private long lastTime=0;
	private double cupLevelEstimate=0.0;
	
	private void estimateCupLevel(Cup cup) {
		if (lastTime==0) {
			lastTime=System.currentTimeMillis();
		    return;
		}
		long timeAdded=System.currentTimeMillis()-lastTime;
		lastTime=System.currentTimeMillis();
		double addedWater=0.0;
		if (hotOn) {
			addedWater=this.litrePerTimeHot*timeAdded;
		}
		if (coldOn) {
			addedWater+=litrePerTimeHot*timeAdded;
		}
		cupLevelEstimate+=addedWater;
	}
	
	
	/**
	 * Controls the temperature of the water
	 */
	public void controlWaterHeater(IMachine machine) {
		
		IWaterHeater heater=machine.getWaterHeater();
		estimateCupLevel(machine.getCup());
		switch (currentState) {
		case IDLE :
			waterTargetTemperatureC=IDLE_TARGET_TEMPERATURE;
		    break;
		case CALIBRATION_STARTING:
			doStartCalibration(heater);
			return;
		case CALIBRATING:
			doCalibration(heater);
			return;
		case PRE_HEATING:
			waterTargetTemperatureC = preHeatTemperature;
			break;
		case MIXING_DRINK:
			// try and keep water heater hot with first 20%
			waterTargetTemperatureC = preHeatTemperature;
			
			if (recipe!=null) {
				if (this.cupLevelEstimate>=recipe.getCupCapacity()*0.2) {
					currentState=TOPPING_UP;
				}
			} else {
				System.out.println("Recipe is null");
			}
			machine.getWaterHeater().setHotWaterTap(true);
			hotOn=true;
			//if (this.cupLevelEstimate>=machine.getCup())
			break;
		case TOPPING_UP :
			waterTargetTemperatureC = (float)(DRINK_TARGET_TEMPERATURE*1.2);
			if (machine.getCup().getTemperatureInC()>DRINK_TARGET_TEMPERATURE) {
				machine.getWaterHeater().setColdWaterTap(true);
				coldOn=true;
			} else {
				machine.getWaterHeater().setColdWaterTap(false);
				coldOn=false;
			}
			if (cupLevelEstimate>=recipe.getCupCapacity()) {
				// now all off and reset
				currentState=IDLE;
				machine.getWaterHeater().setColdWaterTap(false);
				machine.getWaterHeater().setHotWaterTap(false);
				hotOn=false;
				coldOn=false;
			    cupLevelEstimate=0;
			}
 			break;
		}
		if (heater.getTemperatureDegreesC()>=waterTargetTemperatureC) {
			heater.setHeaterOff();
			if (currentState==PRE_HEATING) {
				currentState=MIXING_DRINK;		// move to next stage of drink manufacture
			}
		} else {
			heater.setHeaterOn();
		}
		
	}
	
	
	/**
	 * Sets the water heater into pre-heat mode
	 * @param preHeatTemperature
	 */
	public void doPreHeat(Recipe recipe) {
		this.recipe=recipe;
		this.preHeatTemperature=recipe.getMakeTemperature();
		currentState=PRE_HEATING;
	}
	
	public void doMixDrink() {
		currentState=MIXING_DRINK;
	}
	
	public void setToIdle() {
		currentState=IDLE;
	}
	
	private void doStartCalibration(IWaterHeater heater) {
		oldTime = System.currentTimeMillis(); // we use this to measure the time
												// between samples
		heater.setHeaterOn();
		currentState = CALIBRATING;
	}

	public void startCalibration() {
		currentState = CALIBRATION_STARTING;
	}
	
	public void setPreheat() {
		
	}

	/**
	 * Calibrates the water heater to determine at what temperature it needs to
	 * get to target <=4 seconds
	 */
	private void doCalibration(IWaterHeater heater) {
		long currentTime = System.currentTimeMillis() - oldTime;
		if (currentTime > 1000) { // take a reading every second
			oldTime = System.currentTimeMillis(); // reset the timer
			tempReadings[readingIndex] = heater.getTemperatureDegreesC();
			// Now check if we've reached target
			if (tempReadings[readingIndex] >= COFFEE_MAKE_TEMPERATURE) {
				readingIndex = readingIndex - (MAX_DELAY_TIME - 1);
				if (readingIndex < 0) {
					readingIndex = readingIndex + MAX_DELAY_TIME;
				}
				calibratedIdleTarget = tempReadings[readingIndex];
				currentState=IDLE;
				heater.setHeaterOff();
				return; // all finished
			}
			readingIndex = (readingIndex + 1) % MAX_DELAY_TIME;
		}
	}

	/**
	 * @return the calibrating
	 */
	public final boolean isCalibrating() {
		return (currentState==CALIBRATING);
	}

	/**
	 * @return the calibratedIdleTarget
	 */
	public final float getCalibratedIdleTarget() {
		return calibratedIdleTarget;
	}


	public boolean finishedDrink() {
		return(currentState==IDLE);
	}

}
