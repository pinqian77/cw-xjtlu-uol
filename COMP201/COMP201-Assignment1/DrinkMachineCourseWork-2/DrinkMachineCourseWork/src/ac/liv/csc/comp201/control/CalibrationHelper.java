package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.simulate.Cup;

public class CalibrationHelper {
	private boolean started=false;
	private long oldTime=0;
	public void calibrateWaterTap(IMachine machine) {
		if (!started) {
			oldTime=System.currentTimeMillis();
			machine.vendCup(Cup.MEDIUM_CUP);
			started=true;
		} else {
			Cup cup=machine.getCup();
			if (cup==null) {
				oldTime=System.currentTimeMillis();
				return;
			}
			
			machine.getWaterHeater().setColdWaterTap(true);
			//machine.getWaterHeater().setHotWaterTap(true);
			
			long time=System.currentTimeMillis()-oldTime;
			double level=cup.getWaterLevelLitres();
			if (level>=Cup.MEDIUM) {
				double litrePerTime=level/time;
				System.out.println("Litre per time tick is "+litrePerTime+" total time is "+time);
				System.exit(9);
			}
		}
	}

}
