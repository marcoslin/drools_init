package it.twopay.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.twopay.rules.StatefulRunner;

/*

This class was created as a sample implementation of the drools.  The idea was
to have the car object to have minimum of self enforced logic but uses drools
to implement the operational logic.

Exception Raised:
* IGNITION requires FILL_UP_TANK command, throws "Can't start the car as FILL_UP_TANK was not performed." exception
* RPM cannot exceed 7000 or throws "RPM " + new_rpm + " exceeds the maximum allowed " + MAX_RPM exception

Operational Logics
* On initial start (running = true), gear is set to neutral (null) and rpm is set to 1
* If car is not running, rpm and speed are both zero and gear is null
* upShift cannot go beyond GEAR_6 and downShift ends at null gear
* END_TRIP will set rpm = 1 and gear to null
* TURN_OFF will set running to false, rpm = 0 and gear to null

 */
public class Car {
	public static Logger log = LoggerFactory.getLogger("it.twopay.entity.Car");
	
	public class Action {
		private CarCommands command;
		private String state;
		public Action(CarCommands command, String state) {
			this.command = command;
			this.state = state;
		}
		public CarCommands getCommand() {
			return command;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
	}

	private static final double ACCELERATION_SEED = 500;
	private static final double MAX_RPM = 7000;
	private StatefulRunner runner = new StatefulRunner("CarKS");
	
	private Map<CarGears, Double> gearRatio;
	private List<Action> actions;
	private List<String> errors;
	private Set<CarCommands> commands;
	
	private CarGears gear = null;
	private boolean running = false;
	private double rpm = 0;
	private String state = "init";
	private int stateCount = 0;
	

	private void setup_gear_ratio() {
		gearRatio = new HashMap<>();
		// Ratio to convert RPM to KM/H
		gearRatio.put(CarGears.GEAR_1, 94.25);
		gearRatio.put(CarGears.GEAR_2, 63.0625);
		gearRatio.put(CarGears.GEAR_3, 46.0625);
		gearRatio.put(CarGears.GEAR_4, 35.4375);
		gearRatio.put(CarGears.GEAR_5, 26.1875);
		gearRatio.put(CarGears.GEAR_6, 17.6875);
	}

	public Car() {
		setup_gear_ratio();
		actions = new ArrayList<>();
		commands = new HashSet<>();
		errors = new ArrayList<>();
	}
	
	public void accelerate() throws Exception {
		if (running) {
			double new_rpm = rpm + ACCELERATION_SEED * Math.random();
			if (new_rpm > MAX_RPM) {
				throw new Exception("RPM " + new_rpm + " exceeds the maximum allowed " + MAX_RPM);
			}
			rpm = new_rpm;
			log.debug("Accelerated to rpm: " + rpm);
		}
	}

	public void perform(CarCommands command) throws Exception {
		switch(command) {
		case AUTO_IGNITION:
			setRunning(true);
			break;
		case TURN_OFF:
			setRunning(false);
			break;
		case END_TRIP:
			// Pretty abrupt stop, I know...
			rpm = 1;
			gear = null;
			break;
		default:
			break;
		}
		addAction(command);
		log.debug("Performing " + command.name());
	}
	
	public void upShift() {
		if (running) {
			if (gear == null) {
				gear = CarGears.GEAR_1;
			} else {
				int currentGear = gear.getValue();
				if (currentGear < CarGears.GEAR_6.getValue()) {
					gear = gear.getByValue(++currentGear);
					log.debug("Up shit to gear " + gear);
				}
			}
		}
	}
	
	public void downShift() {
		if (running && gear != null) {
			int currentGear = gear.getValue();
			if (currentGear > CarGears.GEAR_1.getValue()) {
				gear = gear.getByValue(--currentGear);
				log.debug("Down shit to gear " + gear);
			} else if (currentGear == CarGears.GEAR_1.getValue()) {
				gear = null;
			}
		}
	}
	
	public void process() {
		runner.insert(this);
		// Kickoff the rule engine referencing the rules flow
		runner.process("it.twopay.rules.carflow");
	}

	// Property Methods
	public double getSpeed(CarGears gear, double rpm) {
		if (gear == null) {
			return 0.0;
		} else {
			double ratio = gearRatio.get(gear);
			return rpm / ratio;
		}
	}
	public double getRpm(CarGears gear, double speed) {
		if (gear == null) {
			return 0.0;
		} else {
			double ratio = gearRatio.get(gear);
			return speed * ratio;
		}
	}
	
	public double getSpeed() {
		return getSpeed(gear, rpm);
	}
	private void addAction(CarCommands command) {
		Action action = new Action(command, "init");
		actions.add(action);
		commands.add(command);
		// Action must be in the working memory
		runner.insert(action);
	}
	
	
	// Properties
	private void setRunning(boolean running) throws Exception{
		if (running != this.running) {
			gear = null; // Gear always start and end as null
			if (running) {
				if (!commands.contains(CarCommands.FILL_UP_TANK)) {
					running = false;
					throw new Exception("Can't start the car as FILL_UP_TANK was not performed.");
				} else {
					rpm = 1;
				}
			} else {
				rpm = 0;
				gear = null;
			}
			this.running = running;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public double getRpm() {
		return rpm;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
	}

	public CarGears getGear() {
		return gear;
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	public Set<CarCommands> getCommands() {
		return commands;
	}
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if (state == this.state) {
			stateCount++;
		} else {
			stateCount = 1;
		}
		this.state = state;
	}

	public int getStateCount() {
		return stateCount;
	}

	public boolean isError() {
		return (errors.size() > 0);
	}
	public List<String> getErrors() {
		return errors;
	}
	public void addError(String error) {
		errors.add(error);
	}
	
	
}
