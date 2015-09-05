package it.twopay.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Car {
	public static Logger log = LoggerFactory.getLogger("it.twopay.entity.Car");
	
	public enum Actions {
		CHECK_MIRROR("CHECK_MIRROR"),
		RELEASE_PARKING_BRAKE("RELEASE_PARKING_BRAKE"),
		FASTEN_SEATBELT("FASTEN_SEATBELT"),
		IGNITION("IGNITION"),
		END_STRIP("END_STRIP"),
		PARK("PARK"),
		TURN_OFF("TURN_OFF"),
		APPLY_PARKING_BRAKE("APPLY_PARKING_BRAKE"),
		RELEASE_SEATBELT("RELEASE_SEATBELT");
		private String action;
		private Actions(String action) {
			this.action = action;
		}
		@Override 
        public String toString(){ 
            return action; 
        }
	}
	
	public enum Gears {
		GEAR_1(1), GEAR_2(2), GEAR_3(3),
		GEAR_4(4), GEAR_5(5), GEAR_6(6);
		private Integer gear;
		private Gears(Integer gear) {
			this.gear = gear;
		}
		@Override 
        public String toString(){ 
            return "GEAR_" + gear; 
        }
	}
	
	private static final double ACCELERATION_SEED = 500;
	
	private Map<Gears, Double> gearRatio;
	private List<Actions> actions;
	private List<Actions> delayedActions;
	
	private Gears gear = null;
	private boolean running = false;
	private double rpm = 0;
	
	private void setup_gear_ratio() {
		gearRatio = new HashMap<>();
		
		// Ratio to convert RPM to KM/H
		gearRatio.put(Gears.GEAR_1, 94.25);
		gearRatio.put(Gears.GEAR_2, 63.0625);
		gearRatio.put(Gears.GEAR_3, 46.0625);
		gearRatio.put(Gears.GEAR_4, 35.4375);
		gearRatio.put(Gears.GEAR_5, 26.1875);
		gearRatio.put(Gears.GEAR_6, 17.6875);
	}

	public Car() {
		setup_gear_ratio();
		actions = new ArrayList<>();
	}
	
	public void accelerate() {
		if (running) {
			rpm += ACCELERATION_SEED * Math.random();
			log.debug("Accelerated to rpm: " + rpm);
		}
	}
	
	public void perform(Actions action) {
		switch(action) {
		case IGNITION:
			setRunning(true);
			break;
		case TURN_OFF:
			setRunning(false);
			break;
		case END_STRIP:
			// Pretty abrupt stop, I know...
			rpm = 1;
			gear = Gears.GEAR_1;
		default:
			break;
		}
		addAction(action);
	}
	
	public void addDelayAction(Actions action) {
		// Action to be performed after END_STRIP
		delayedActions.add(action);
	}

	
	// Property Methods
	public double getSpeed(Gears gear, double rpm) {
		double ratio = gearRatio.get(gear);
		return rpm / ratio;
	}
	public double getSpeed() {
		if (gear==null) {
			return 0;
		} else {
			return getSpeed(gear, rpm);
		}
	}
	private void addAction(Actions action) {
		actions.add(action);
	}


	
	// Properties
	private void setRunning(boolean running) {
		this.running = running;
		if (running) {
			gear = Gears.GEAR_1;
			rpm = 1;
		} else {
			gear = null;
			rpm = 0;
		}
	}

	public double getRpm() {
		return rpm;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
	}

	public Gears getGear() {
		return gear;
	}
	
	public List<Actions> getActions() {
		return actions;
	}

	public List<Actions> getEndActions() {
		return delayedActions;
	}

	
}
