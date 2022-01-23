package ac.liv.csc.comp201.simulate;

import ac.liv.csc.comp201.model.IWaterHeater;

public class WaterHeater implements IWaterHeater {

	private boolean heaterOn = true;
	private float waterHeaterTemp = 200;
	private float ambientTemp = 15;
	private  float targetCallback=0;
	private boolean open = false;
	private boolean coldWaterOn=false;
	private int ftemperature=0;

	private WaterHeaterUI heaterUI;

	public WaterHeater() {
		heaterUI = new WaterHeaterUI();
	}

	private float temperature = ambientTemp;

	@Override
	public float getTemperatureDegreesC() {
		if (ftemperature>0) {
			return(ftemperature);
		}
		return temperature;
	}

	@Override
	public boolean setHeaterOn() {
		heaterOn = true;
		return (true);

	}
	
	public void forceError() {
		ftemperature=120;
	}

	public void clearForceError() {
		ftemperature=0;
	}

	public void setHotWaterTap(boolean open) {
		this.open = open;
	}
	
	public void setColdWaterTap(boolean coldOn) {
		coldWaterOn=coldOn;
	}

	@Override
	public void setHeaterOff() {
		heaterOn = false;

	}

	@Override
	public boolean getHeaterOnStatus() {
		return (heaterOn);
	}

	
	private void addToCup(Cup cup) {
		if (cup == null) {
			return;
		}
		cup.addWater(Cup.MEDIUM / (4 * 10), temperature);
	}
	
	
	
	/**
	 * This is called 10x per second
	 */
	public void updateHeater(Cup cup) {
		if (coldWaterOn) {
			if (cup!=null) {
				cup.addWater(Cup.MEDIUM / (4 * 10),ambientTemp);
			}
		}
		if (heaterOn) {
			float heatFlow = (waterHeaterTemp - this.temperature) / 150;
			temperature += heatFlow;
			if (targetCallback!=0) {
				//	System.out.println("t is "+this.temperature);
					if (this.temperature>=this.targetCallback) {
						targetCallback=0;
						
					}
				}
		} else {
			float heatFlow = (ambientTemp - this.temperature) / 1000;
			temperature += heatFlow;
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (open) {
			float heatFlow = -1;
			addToCup(cup);
			temperature += heatFlow;
			if (temperature < ambientTemp) {
				temperature = ambientTemp;
			}

		}
		if (temperature > 100) {
			temperature = 100;
		}
		this.heaterUI.setTemperatureDegreesC(this.getTemperatureDegreesC());
		
	}

	/**
	 * @param targetCallback the targetCallback to set
	 */
	public final void setTargetCallback(float targetCallback) {
		this.targetCallback = targetCallback;
	}

	/**
	 * @param tester the tester to set
	 */
	

}
